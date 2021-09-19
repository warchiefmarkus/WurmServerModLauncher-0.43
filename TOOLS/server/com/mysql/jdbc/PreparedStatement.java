/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import com.mysql.jdbc.exceptions.MySQLStatementCancelledException;
/*      */ import com.mysql.jdbc.exceptions.MySQLTimeoutException;
/*      */ import com.mysql.jdbc.profiler.ProfilerEvent;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.StringReader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.net.URL;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.CharBuffer;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.CharsetEncoder;
/*      */ import java.sql.Array;
/*      */ import java.sql.BatchUpdateException;
/*      */ import java.sql.Blob;
/*      */ import java.sql.Clob;
/*      */ import java.sql.DatabaseMetaData;
/*      */ import java.sql.Date;
/*      */ import java.sql.ParameterMetaData;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.Ref;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.ResultSetMetaData;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.text.DateFormat;
/*      */ import java.text.ParsePosition;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.TimeZone;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class PreparedStatement
/*      */   extends StatementImpl
/*      */   implements PreparedStatement
/*      */ {
/*      */   private static final Constructor JDBC_4_PSTMT_2_ARG_CTOR;
/*      */   private static final Constructor JDBC_4_PSTMT_3_ARG_CTOR;
/*      */   private static final Constructor JDBC_4_PSTMT_4_ARG_CTOR;
/*      */   
/*      */   static {
/*  101 */     if (Util.isJdbc4()) {
/*      */       try {
/*  103 */         JDBC_4_PSTMT_2_ARG_CTOR = Class.forName("com.mysql.jdbc.JDBC4PreparedStatement").getConstructor(new Class[] { ConnectionImpl.class, String.class });
/*      */ 
/*      */ 
/*      */         
/*  107 */         JDBC_4_PSTMT_3_ARG_CTOR = Class.forName("com.mysql.jdbc.JDBC4PreparedStatement").getConstructor(new Class[] { ConnectionImpl.class, String.class, String.class });
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  112 */         JDBC_4_PSTMT_4_ARG_CTOR = Class.forName("com.mysql.jdbc.JDBC4PreparedStatement").getConstructor(new Class[] { ConnectionImpl.class, String.class, String.class, ParseInfo.class });
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*  117 */       catch (SecurityException e) {
/*  118 */         throw new RuntimeException(e);
/*  119 */       } catch (NoSuchMethodException e) {
/*  120 */         throw new RuntimeException(e);
/*  121 */       } catch (ClassNotFoundException e) {
/*  122 */         throw new RuntimeException(e);
/*      */       } 
/*      */     } else {
/*  125 */       JDBC_4_PSTMT_2_ARG_CTOR = null;
/*  126 */       JDBC_4_PSTMT_3_ARG_CTOR = null;
/*  127 */       JDBC_4_PSTMT_4_ARG_CTOR = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   class BatchParams
/*      */   {
/*      */     boolean[] isNull;
/*      */     
/*      */     boolean[] isStream;
/*      */     InputStream[] parameterStreams;
/*      */     byte[][] parameterStrings;
/*      */     int[] streamLengths;
/*      */     private final PreparedStatement this$0;
/*      */     
/*      */     BatchParams(PreparedStatement this$0, byte[][] strings, InputStream[] streams, boolean[] isStreamFlags, int[] lengths, boolean[] isNullFlags) {
/*  143 */       this.this$0 = this$0; this.isNull = null; this.isStream = null;
/*      */       this.parameterStreams = null;
/*      */       this.parameterStrings = (byte[][])null;
/*      */       this.streamLengths = null;
/*  147 */       this.parameterStrings = new byte[strings.length][];
/*  148 */       this.parameterStreams = new InputStream[streams.length];
/*  149 */       this.isStream = new boolean[isStreamFlags.length];
/*  150 */       this.streamLengths = new int[lengths.length];
/*  151 */       this.isNull = new boolean[isNullFlags.length];
/*  152 */       System.arraycopy(strings, 0, this.parameterStrings, 0, strings.length);
/*      */       
/*  154 */       System.arraycopy(streams, 0, this.parameterStreams, 0, streams.length);
/*      */       
/*  156 */       System.arraycopy(isStreamFlags, 0, this.isStream, 0, isStreamFlags.length);
/*      */       
/*  158 */       System.arraycopy(lengths, 0, this.streamLengths, 0, lengths.length);
/*  159 */       System.arraycopy(isNullFlags, 0, this.isNull, 0, isNullFlags.length);
/*      */     }
/*      */   }
/*      */   
/*      */   class EndPoint
/*      */   {
/*      */     int begin;
/*      */     int end;
/*      */     private final PreparedStatement this$0;
/*      */     
/*      */     EndPoint(PreparedStatement this$0, int b, int e) {
/*  170 */       this.this$0 = this$0;
/*  171 */       this.begin = b;
/*  172 */       this.end = e;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   class ParseInfo
/*      */   {
/*      */     char firstStmtChar;
/*      */     
/*      */     boolean foundLimitClause;
/*      */     
/*      */     boolean foundLoadData;
/*      */     
/*      */     long lastUsed;
/*      */     
/*      */     int statementLength;
/*      */     
/*      */     int statementStartPos;
/*      */     
/*      */     boolean canRewriteAsMultiValueInsert;
/*      */     
/*      */     byte[][] staticSql;
/*      */     
/*      */     boolean isOnDuplicateKeyUpdate;
/*      */     
/*      */     int locationOfOnDuplicateKeyUpdate;
/*      */     
/*      */     String valuesClause;
/*      */     
/*      */     boolean parametersInDuplicateKeyClause;
/*      */     
/*      */     private ParseInfo batchHead;
/*      */     
/*      */     private ParseInfo batchValues;
/*      */     private ParseInfo batchODKUClause;
/*      */     private final PreparedStatement this$0;
/*      */     
/*      */     ParseInfo(String sql, ConnectionImpl conn, DatabaseMetaData dbmd, String encoding, SingleByteCharsetConverter converter) throws SQLException {
/*  210 */       this(sql, conn, dbmd, encoding, converter, true);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ParseInfo(String sql, ConnectionImpl conn, DatabaseMetaData dbmd, String encoding, SingleByteCharsetConverter converter, boolean buildRewriteInfo) throws SQLException {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: invokespecial <init> : ()V
/*      */       //   4: aload_0
/*      */       //   5: aload_1
/*      */       //   6: putfield this$0 : Lcom/mysql/jdbc/PreparedStatement;
/*      */       //   9: aload_0
/*      */       //   10: iconst_0
/*      */       //   11: putfield firstStmtChar : C
/*      */       //   14: aload_0
/*      */       //   15: iconst_0
/*      */       //   16: putfield foundLimitClause : Z
/*      */       //   19: aload_0
/*      */       //   20: iconst_0
/*      */       //   21: putfield foundLoadData : Z
/*      */       //   24: aload_0
/*      */       //   25: lconst_0
/*      */       //   26: putfield lastUsed : J
/*      */       //   29: aload_0
/*      */       //   30: iconst_0
/*      */       //   31: putfield statementLength : I
/*      */       //   34: aload_0
/*      */       //   35: iconst_0
/*      */       //   36: putfield statementStartPos : I
/*      */       //   39: aload_0
/*      */       //   40: iconst_0
/*      */       //   41: putfield canRewriteAsMultiValueInsert : Z
/*      */       //   44: aload_0
/*      */       //   45: aconst_null
/*      */       //   46: checkcast [[B
/*      */       //   49: putfield staticSql : [[B
/*      */       //   52: aload_0
/*      */       //   53: iconst_0
/*      */       //   54: putfield isOnDuplicateKeyUpdate : Z
/*      */       //   57: aload_0
/*      */       //   58: iconst_m1
/*      */       //   59: putfield locationOfOnDuplicateKeyUpdate : I
/*      */       //   62: aload_0
/*      */       //   63: iconst_0
/*      */       //   64: putfield parametersInDuplicateKeyClause : Z
/*      */       //   67: aload_2
/*      */       //   68: ifnonnull -> 86
/*      */       //   71: ldc 'PreparedStatement.61'
/*      */       //   73: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
/*      */       //   76: ldc 'S1009'
/*      */       //   78: aload_1
/*      */       //   79: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
/*      */       //   82: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
/*      */       //   85: athrow
/*      */       //   86: aload_0
/*      */       //   87: aload_1
/*      */       //   88: aload_2
/*      */       //   89: invokevirtual getOnDuplicateKeyLocation : (Ljava/lang/String;)I
/*      */       //   92: putfield locationOfOnDuplicateKeyUpdate : I
/*      */       //   95: aload_0
/*      */       //   96: aload_0
/*      */       //   97: getfield locationOfOnDuplicateKeyUpdate : I
/*      */       //   100: iconst_m1
/*      */       //   101: if_icmpeq -> 108
/*      */       //   104: iconst_1
/*      */       //   105: goto -> 109
/*      */       //   108: iconst_0
/*      */       //   109: putfield isOnDuplicateKeyUpdate : Z
/*      */       //   112: aload_0
/*      */       //   113: invokestatic currentTimeMillis : ()J
/*      */       //   116: putfield lastUsed : J
/*      */       //   119: aload #4
/*      */       //   121: invokeinterface getIdentifierQuoteString : ()Ljava/lang/String;
/*      */       //   126: astore #8
/*      */       //   128: iconst_0
/*      */       //   129: istore #9
/*      */       //   131: aload #8
/*      */       //   133: ifnull -> 162
/*      */       //   136: aload #8
/*      */       //   138: ldc ' '
/*      */       //   140: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   143: ifne -> 162
/*      */       //   146: aload #8
/*      */       //   148: invokevirtual length : ()I
/*      */       //   151: ifle -> 162
/*      */       //   154: aload #8
/*      */       //   156: iconst_0
/*      */       //   157: invokevirtual charAt : (I)C
/*      */       //   160: istore #9
/*      */       //   162: aload_0
/*      */       //   163: aload_2
/*      */       //   164: invokevirtual length : ()I
/*      */       //   167: putfield statementLength : I
/*      */       //   170: new java/util/ArrayList
/*      */       //   173: dup
/*      */       //   174: invokespecial <init> : ()V
/*      */       //   177: astore #10
/*      */       //   179: iconst_0
/*      */       //   180: istore #11
/*      */       //   182: iconst_0
/*      */       //   183: istore #12
/*      */       //   185: iconst_0
/*      */       //   186: istore #13
/*      */       //   188: iconst_0
/*      */       //   189: istore #14
/*      */       //   191: aload_0
/*      */       //   192: getfield statementLength : I
/*      */       //   195: iconst_5
/*      */       //   196: isub
/*      */       //   197: istore #16
/*      */       //   199: aload_0
/*      */       //   200: iconst_0
/*      */       //   201: putfield foundLimitClause : Z
/*      */       //   204: aload_1
/*      */       //   205: getfield connection : Lcom/mysql/jdbc/ConnectionImpl;
/*      */       //   208: invokevirtual isNoBackslashEscapesSet : ()Z
/*      */       //   211: istore #17
/*      */       //   213: aload_0
/*      */       //   214: aload_1
/*      */       //   215: aload_2
/*      */       //   216: invokevirtual findStartOfStatement : (Ljava/lang/String;)I
/*      */       //   219: putfield statementStartPos : I
/*      */       //   222: aload_0
/*      */       //   223: getfield statementStartPos : I
/*      */       //   226: istore #15
/*      */       //   228: iload #15
/*      */       //   230: aload_0
/*      */       //   231: getfield statementLength : I
/*      */       //   234: if_icmpge -> 874
/*      */       //   237: aload_2
/*      */       //   238: iload #15
/*      */       //   240: invokevirtual charAt : (I)C
/*      */       //   243: istore #18
/*      */       //   245: aload_0
/*      */       //   246: getfield firstStmtChar : C
/*      */       //   249: ifne -> 269
/*      */       //   252: iload #18
/*      */       //   254: invokestatic isLetter : (C)Z
/*      */       //   257: ifeq -> 269
/*      */       //   260: aload_0
/*      */       //   261: iload #18
/*      */       //   263: invokestatic toUpperCase : (C)C
/*      */       //   266: putfield firstStmtChar : C
/*      */       //   269: iload #17
/*      */       //   271: ifne -> 298
/*      */       //   274: iload #18
/*      */       //   276: bipush #92
/*      */       //   278: if_icmpne -> 298
/*      */       //   281: iload #15
/*      */       //   283: aload_0
/*      */       //   284: getfield statementLength : I
/*      */       //   287: iconst_1
/*      */       //   288: isub
/*      */       //   289: if_icmpge -> 298
/*      */       //   292: iinc #15, 1
/*      */       //   295: goto -> 868
/*      */       //   298: iload #11
/*      */       //   300: ifne -> 330
/*      */       //   303: iload #9
/*      */       //   305: ifeq -> 330
/*      */       //   308: iload #18
/*      */       //   310: iload #9
/*      */       //   312: if_icmpne -> 330
/*      */       //   315: iload #13
/*      */       //   317: ifne -> 324
/*      */       //   320: iconst_1
/*      */       //   321: goto -> 325
/*      */       //   324: iconst_0
/*      */       //   325: istore #13
/*      */       //   327: goto -> 678
/*      */       //   330: iload #13
/*      */       //   332: ifne -> 678
/*      */       //   335: iload #11
/*      */       //   337: ifeq -> 448
/*      */       //   340: iload #18
/*      */       //   342: bipush #39
/*      */       //   344: if_icmpeq -> 354
/*      */       //   347: iload #18
/*      */       //   349: bipush #34
/*      */       //   351: if_icmpne -> 409
/*      */       //   354: iload #18
/*      */       //   356: iload #12
/*      */       //   358: if_icmpne -> 409
/*      */       //   361: iload #15
/*      */       //   363: aload_0
/*      */       //   364: getfield statementLength : I
/*      */       //   367: iconst_1
/*      */       //   368: isub
/*      */       //   369: if_icmpge -> 391
/*      */       //   372: aload_2
/*      */       //   373: iload #15
/*      */       //   375: iconst_1
/*      */       //   376: iadd
/*      */       //   377: invokevirtual charAt : (I)C
/*      */       //   380: iload #12
/*      */       //   382: if_icmpne -> 391
/*      */       //   385: iinc #15, 1
/*      */       //   388: goto -> 868
/*      */       //   391: iload #11
/*      */       //   393: ifne -> 400
/*      */       //   396: iconst_1
/*      */       //   397: goto -> 401
/*      */       //   400: iconst_0
/*      */       //   401: istore #11
/*      */       //   403: iconst_0
/*      */       //   404: istore #12
/*      */       //   406: goto -> 678
/*      */       //   409: iload #18
/*      */       //   411: bipush #39
/*      */       //   413: if_icmpeq -> 423
/*      */       //   416: iload #18
/*      */       //   418: bipush #34
/*      */       //   420: if_icmpne -> 678
/*      */       //   423: iload #18
/*      */       //   425: iload #12
/*      */       //   427: if_icmpne -> 678
/*      */       //   430: iload #11
/*      */       //   432: ifne -> 439
/*      */       //   435: iconst_1
/*      */       //   436: goto -> 440
/*      */       //   439: iconst_0
/*      */       //   440: istore #11
/*      */       //   442: iconst_0
/*      */       //   443: istore #12
/*      */       //   445: goto -> 678
/*      */       //   448: iload #18
/*      */       //   450: bipush #35
/*      */       //   452: if_icmpeq -> 486
/*      */       //   455: iload #18
/*      */       //   457: bipush #45
/*      */       //   459: if_icmpne -> 532
/*      */       //   462: iload #15
/*      */       //   464: iconst_1
/*      */       //   465: iadd
/*      */       //   466: aload_0
/*      */       //   467: getfield statementLength : I
/*      */       //   470: if_icmpge -> 532
/*      */       //   473: aload_2
/*      */       //   474: iload #15
/*      */       //   476: iconst_1
/*      */       //   477: iadd
/*      */       //   478: invokevirtual charAt : (I)C
/*      */       //   481: bipush #45
/*      */       //   483: if_icmpne -> 532
/*      */       //   486: aload_0
/*      */       //   487: getfield statementLength : I
/*      */       //   490: iconst_1
/*      */       //   491: isub
/*      */       //   492: istore #19
/*      */       //   494: iload #15
/*      */       //   496: iload #19
/*      */       //   498: if_icmpge -> 868
/*      */       //   501: aload_2
/*      */       //   502: iload #15
/*      */       //   504: invokevirtual charAt : (I)C
/*      */       //   507: istore #18
/*      */       //   509: iload #18
/*      */       //   511: bipush #13
/*      */       //   513: if_icmpeq -> 868
/*      */       //   516: iload #18
/*      */       //   518: bipush #10
/*      */       //   520: if_icmpne -> 526
/*      */       //   523: goto -> 868
/*      */       //   526: iinc #15, 1
/*      */       //   529: goto -> 494
/*      */       //   532: iload #18
/*      */       //   534: bipush #47
/*      */       //   536: if_icmpne -> 657
/*      */       //   539: iload #15
/*      */       //   541: iconst_1
/*      */       //   542: iadd
/*      */       //   543: aload_0
/*      */       //   544: getfield statementLength : I
/*      */       //   547: if_icmpge -> 657
/*      */       //   550: aload_2
/*      */       //   551: iload #15
/*      */       //   553: iconst_1
/*      */       //   554: iadd
/*      */       //   555: invokevirtual charAt : (I)C
/*      */       //   558: istore #19
/*      */       //   560: iload #19
/*      */       //   562: bipush #42
/*      */       //   564: if_icmpne -> 654
/*      */       //   567: iinc #15, 2
/*      */       //   570: iload #15
/*      */       //   572: istore #20
/*      */       //   574: iload #20
/*      */       //   576: aload_0
/*      */       //   577: getfield statementLength : I
/*      */       //   580: if_icmpge -> 654
/*      */       //   583: iinc #15, 1
/*      */       //   586: aload_2
/*      */       //   587: iload #20
/*      */       //   589: invokevirtual charAt : (I)C
/*      */       //   592: istore #19
/*      */       //   594: iload #19
/*      */       //   596: bipush #42
/*      */       //   598: if_icmpne -> 648
/*      */       //   601: iload #20
/*      */       //   603: iconst_1
/*      */       //   604: iadd
/*      */       //   605: aload_0
/*      */       //   606: getfield statementLength : I
/*      */       //   609: if_icmpge -> 648
/*      */       //   612: aload_2
/*      */       //   613: iload #20
/*      */       //   615: iconst_1
/*      */       //   616: iadd
/*      */       //   617: invokevirtual charAt : (I)C
/*      */       //   620: bipush #47
/*      */       //   622: if_icmpne -> 648
/*      */       //   625: iinc #15, 1
/*      */       //   628: iload #15
/*      */       //   630: aload_0
/*      */       //   631: getfield statementLength : I
/*      */       //   634: if_icmpge -> 654
/*      */       //   637: aload_2
/*      */       //   638: iload #15
/*      */       //   640: invokevirtual charAt : (I)C
/*      */       //   643: istore #18
/*      */       //   645: goto -> 654
/*      */       //   648: iinc #20, 1
/*      */       //   651: goto -> 574
/*      */       //   654: goto -> 678
/*      */       //   657: iload #18
/*      */       //   659: bipush #39
/*      */       //   661: if_icmpeq -> 671
/*      */       //   664: iload #18
/*      */       //   666: bipush #34
/*      */       //   668: if_icmpne -> 678
/*      */       //   671: iconst_1
/*      */       //   672: istore #11
/*      */       //   674: iload #18
/*      */       //   676: istore #12
/*      */       //   678: iload #18
/*      */       //   680: bipush #63
/*      */       //   682: if_icmpne -> 741
/*      */       //   685: iload #11
/*      */       //   687: ifne -> 741
/*      */       //   690: iload #13
/*      */       //   692: ifne -> 741
/*      */       //   695: aload #10
/*      */       //   697: iconst_2
/*      */       //   698: newarray int
/*      */       //   700: dup
/*      */       //   701: iconst_0
/*      */       //   702: iload #14
/*      */       //   704: iastore
/*      */       //   705: dup
/*      */       //   706: iconst_1
/*      */       //   707: iload #15
/*      */       //   709: iastore
/*      */       //   710: invokevirtual add : (Ljava/lang/Object;)Z
/*      */       //   713: pop
/*      */       //   714: iload #15
/*      */       //   716: iconst_1
/*      */       //   717: iadd
/*      */       //   718: istore #14
/*      */       //   720: aload_0
/*      */       //   721: getfield isOnDuplicateKeyUpdate : Z
/*      */       //   724: ifeq -> 741
/*      */       //   727: iload #15
/*      */       //   729: aload_0
/*      */       //   730: getfield locationOfOnDuplicateKeyUpdate : I
/*      */       //   733: if_icmple -> 741
/*      */       //   736: aload_0
/*      */       //   737: iconst_1
/*      */       //   738: putfield parametersInDuplicateKeyClause : Z
/*      */       //   741: iload #11
/*      */       //   743: ifne -> 868
/*      */       //   746: iload #15
/*      */       //   748: iload #16
/*      */       //   750: if_icmpge -> 868
/*      */       //   753: iload #18
/*      */       //   755: bipush #76
/*      */       //   757: if_icmpeq -> 767
/*      */       //   760: iload #18
/*      */       //   762: bipush #108
/*      */       //   764: if_icmpne -> 868
/*      */       //   767: aload_2
/*      */       //   768: iload #15
/*      */       //   770: iconst_1
/*      */       //   771: iadd
/*      */       //   772: invokevirtual charAt : (I)C
/*      */       //   775: istore #19
/*      */       //   777: iload #19
/*      */       //   779: bipush #73
/*      */       //   781: if_icmpeq -> 791
/*      */       //   784: iload #19
/*      */       //   786: bipush #105
/*      */       //   788: if_icmpne -> 868
/*      */       //   791: aload_2
/*      */       //   792: iload #15
/*      */       //   794: iconst_2
/*      */       //   795: iadd
/*      */       //   796: invokevirtual charAt : (I)C
/*      */       //   799: istore #20
/*      */       //   801: iload #20
/*      */       //   803: bipush #77
/*      */       //   805: if_icmpeq -> 815
/*      */       //   808: iload #20
/*      */       //   810: bipush #109
/*      */       //   812: if_icmpne -> 868
/*      */       //   815: aload_2
/*      */       //   816: iload #15
/*      */       //   818: iconst_3
/*      */       //   819: iadd
/*      */       //   820: invokevirtual charAt : (I)C
/*      */       //   823: istore #21
/*      */       //   825: iload #21
/*      */       //   827: bipush #73
/*      */       //   829: if_icmpeq -> 839
/*      */       //   832: iload #21
/*      */       //   834: bipush #105
/*      */       //   836: if_icmpne -> 868
/*      */       //   839: aload_2
/*      */       //   840: iload #15
/*      */       //   842: iconst_4
/*      */       //   843: iadd
/*      */       //   844: invokevirtual charAt : (I)C
/*      */       //   847: istore #22
/*      */       //   849: iload #22
/*      */       //   851: bipush #84
/*      */       //   853: if_icmpeq -> 863
/*      */       //   856: iload #22
/*      */       //   858: bipush #116
/*      */       //   860: if_icmpne -> 868
/*      */       //   863: aload_0
/*      */       //   864: iconst_1
/*      */       //   865: putfield foundLimitClause : Z
/*      */       //   868: iinc #15, 1
/*      */       //   871: goto -> 228
/*      */       //   874: aload_0
/*      */       //   875: getfield firstStmtChar : C
/*      */       //   878: bipush #76
/*      */       //   880: if_icmpne -> 908
/*      */       //   883: aload_2
/*      */       //   884: ldc 'LOAD DATA'
/*      */       //   886: invokestatic startsWithIgnoreCaseAndWs : (Ljava/lang/String;Ljava/lang/String;)Z
/*      */       //   889: ifeq -> 900
/*      */       //   892: aload_0
/*      */       //   893: iconst_1
/*      */       //   894: putfield foundLoadData : Z
/*      */       //   897: goto -> 913
/*      */       //   900: aload_0
/*      */       //   901: iconst_0
/*      */       //   902: putfield foundLoadData : Z
/*      */       //   905: goto -> 913
/*      */       //   908: aload_0
/*      */       //   909: iconst_0
/*      */       //   910: putfield foundLoadData : Z
/*      */       //   913: aload #10
/*      */       //   915: iconst_2
/*      */       //   916: newarray int
/*      */       //   918: dup
/*      */       //   919: iconst_0
/*      */       //   920: iload #14
/*      */       //   922: iastore
/*      */       //   923: dup
/*      */       //   924: iconst_1
/*      */       //   925: aload_0
/*      */       //   926: getfield statementLength : I
/*      */       //   929: iastore
/*      */       //   930: invokevirtual add : (Ljava/lang/Object;)Z
/*      */       //   933: pop
/*      */       //   934: aload_0
/*      */       //   935: aload #10
/*      */       //   937: invokevirtual size : ()I
/*      */       //   940: anewarray [B
/*      */       //   943: putfield staticSql : [[B
/*      */       //   946: aload_2
/*      */       //   947: invokevirtual toCharArray : ()[C
/*      */       //   950: astore #18
/*      */       //   952: iconst_0
/*      */       //   953: istore #15
/*      */       //   955: iload #15
/*      */       //   957: aload_0
/*      */       //   958: getfield staticSql : [[B
/*      */       //   961: arraylength
/*      */       //   962: if_icmpge -> 1186
/*      */       //   965: aload #10
/*      */       //   967: iload #15
/*      */       //   969: invokevirtual get : (I)Ljava/lang/Object;
/*      */       //   972: checkcast [I
/*      */       //   975: astore #19
/*      */       //   977: aload #19
/*      */       //   979: iconst_1
/*      */       //   980: iaload
/*      */       //   981: istore #20
/*      */       //   983: aload #19
/*      */       //   985: iconst_0
/*      */       //   986: iaload
/*      */       //   987: istore #21
/*      */       //   989: iload #20
/*      */       //   991: iload #21
/*      */       //   993: isub
/*      */       //   994: istore #22
/*      */       //   996: aload_0
/*      */       //   997: getfield foundLoadData : Z
/*      */       //   1000: ifeq -> 1033
/*      */       //   1003: new java/lang/String
/*      */       //   1006: dup
/*      */       //   1007: aload #18
/*      */       //   1009: iload #21
/*      */       //   1011: iload #22
/*      */       //   1013: invokespecial <init> : ([CII)V
/*      */       //   1016: astore #23
/*      */       //   1018: aload_0
/*      */       //   1019: getfield staticSql : [[B
/*      */       //   1022: iload #15
/*      */       //   1024: aload #23
/*      */       //   1026: invokevirtual getBytes : ()[B
/*      */       //   1029: aastore
/*      */       //   1030: goto -> 1180
/*      */       //   1033: aload #5
/*      */       //   1035: ifnonnull -> 1087
/*      */       //   1038: iload #22
/*      */       //   1040: newarray byte
/*      */       //   1042: astore #23
/*      */       //   1044: iconst_0
/*      */       //   1045: istore #24
/*      */       //   1047: iload #24
/*      */       //   1049: iload #22
/*      */       //   1051: if_icmpge -> 1075
/*      */       //   1054: aload #23
/*      */       //   1056: iload #24
/*      */       //   1058: aload_2
/*      */       //   1059: iload #21
/*      */       //   1061: iload #24
/*      */       //   1063: iadd
/*      */       //   1064: invokevirtual charAt : (I)C
/*      */       //   1067: i2b
/*      */       //   1068: bastore
/*      */       //   1069: iinc #24, 1
/*      */       //   1072: goto -> 1047
/*      */       //   1075: aload_0
/*      */       //   1076: getfield staticSql : [[B
/*      */       //   1079: iload #15
/*      */       //   1081: aload #23
/*      */       //   1083: aastore
/*      */       //   1084: goto -> 1180
/*      */       //   1087: aload #6
/*      */       //   1089: ifnull -> 1132
/*      */       //   1092: aload_0
/*      */       //   1093: getfield staticSql : [[B
/*      */       //   1096: iload #15
/*      */       //   1098: aload_2
/*      */       //   1099: aload #6
/*      */       //   1101: aload #5
/*      */       //   1103: aload_1
/*      */       //   1104: getfield connection : Lcom/mysql/jdbc/ConnectionImpl;
/*      */       //   1107: invokevirtual getServerCharacterEncoding : ()Ljava/lang/String;
/*      */       //   1110: iload #21
/*      */       //   1112: iload #22
/*      */       //   1114: aload_1
/*      */       //   1115: getfield connection : Lcom/mysql/jdbc/ConnectionImpl;
/*      */       //   1118: invokevirtual parserKnowsUnicode : ()Z
/*      */       //   1121: aload_1
/*      */       //   1122: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
/*      */       //   1125: invokestatic getBytes : (Ljava/lang/String;Lcom/mysql/jdbc/SingleByteCharsetConverter;Ljava/lang/String;Ljava/lang/String;IIZLcom/mysql/jdbc/ExceptionInterceptor;)[B
/*      */       //   1128: aastore
/*      */       //   1129: goto -> 1180
/*      */       //   1132: new java/lang/String
/*      */       //   1135: dup
/*      */       //   1136: aload #18
/*      */       //   1138: iload #21
/*      */       //   1140: iload #22
/*      */       //   1142: invokespecial <init> : ([CII)V
/*      */       //   1145: astore #23
/*      */       //   1147: aload_0
/*      */       //   1148: getfield staticSql : [[B
/*      */       //   1151: iload #15
/*      */       //   1153: aload #23
/*      */       //   1155: aload #5
/*      */       //   1157: aload_1
/*      */       //   1158: getfield connection : Lcom/mysql/jdbc/ConnectionImpl;
/*      */       //   1161: invokevirtual getServerCharacterEncoding : ()Ljava/lang/String;
/*      */       //   1164: aload_1
/*      */       //   1165: getfield connection : Lcom/mysql/jdbc/ConnectionImpl;
/*      */       //   1168: invokevirtual parserKnowsUnicode : ()Z
/*      */       //   1171: aload_3
/*      */       //   1172: aload_1
/*      */       //   1173: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
/*      */       //   1176: invokestatic getBytes : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLcom/mysql/jdbc/ConnectionImpl;Lcom/mysql/jdbc/ExceptionInterceptor;)[B
/*      */       //   1179: aastore
/*      */       //   1180: iinc #15, 1
/*      */       //   1183: goto -> 955
/*      */       //   1186: goto -> 1230
/*      */       //   1189: astore #8
/*      */       //   1191: new java/sql/SQLException
/*      */       //   1194: dup
/*      */       //   1195: new java/lang/StringBuffer
/*      */       //   1198: dup
/*      */       //   1199: invokespecial <init> : ()V
/*      */       //   1202: ldc 'Parse error for '
/*      */       //   1204: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */       //   1207: aload_2
/*      */       //   1208: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
/*      */       //   1211: invokevirtual toString : ()Ljava/lang/String;
/*      */       //   1214: invokespecial <init> : (Ljava/lang/String;)V
/*      */       //   1217: astore #9
/*      */       //   1219: aload #9
/*      */       //   1221: aload #8
/*      */       //   1223: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
/*      */       //   1226: pop
/*      */       //   1227: aload #9
/*      */       //   1229: athrow
/*      */       //   1230: iload #7
/*      */       //   1232: ifeq -> 1296
/*      */       //   1235: aload_0
/*      */       //   1236: aload_2
/*      */       //   1237: aload_0
/*      */       //   1238: getfield isOnDuplicateKeyUpdate : Z
/*      */       //   1241: aload_0
/*      */       //   1242: getfield locationOfOnDuplicateKeyUpdate : I
/*      */       //   1245: aload_0
/*      */       //   1246: getfield statementStartPos : I
/*      */       //   1249: invokestatic canRewrite : (Ljava/lang/String;ZII)Z
/*      */       //   1252: ifeq -> 1266
/*      */       //   1255: aload_0
/*      */       //   1256: getfield parametersInDuplicateKeyClause : Z
/*      */       //   1259: ifne -> 1266
/*      */       //   1262: iconst_1
/*      */       //   1263: goto -> 1267
/*      */       //   1266: iconst_0
/*      */       //   1267: putfield canRewriteAsMultiValueInsert : Z
/*      */       //   1270: aload_0
/*      */       //   1271: getfield canRewriteAsMultiValueInsert : Z
/*      */       //   1274: ifeq -> 1296
/*      */       //   1277: aload_3
/*      */       //   1278: invokevirtual getRewriteBatchedStatements : ()Z
/*      */       //   1281: ifeq -> 1296
/*      */       //   1284: aload_0
/*      */       //   1285: aload_2
/*      */       //   1286: aload_3
/*      */       //   1287: aload #4
/*      */       //   1289: aload #5
/*      */       //   1291: aload #6
/*      */       //   1293: invokespecial buildRewriteBatchedParams : (Ljava/lang/String;Lcom/mysql/jdbc/ConnectionImpl;Ljava/sql/DatabaseMetaData;Ljava/lang/String;Lcom/mysql/jdbc/SingleByteCharsetConverter;)V
/*      */       //   1296: return
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #215	-> 0
/*      */       //   #177	-> 9
/*      */       //   #179	-> 14
/*      */       //   #181	-> 19
/*      */       //   #183	-> 24
/*      */       //   #185	-> 29
/*      */       //   #187	-> 34
/*      */       //   #189	-> 39
/*      */       //   #191	-> 44
/*      */       //   #193	-> 52
/*      */       //   #195	-> 57
/*      */       //   #199	-> 62
/*      */       //   #217	-> 67
/*      */       //   #218	-> 71
/*      */       //   #223	-> 86
/*      */       //   #224	-> 95
/*      */       //   #226	-> 112
/*      */       //   #228	-> 119
/*      */       //   #230	-> 128
/*      */       //   #232	-> 131
/*      */       //   #235	-> 154
/*      */       //   #238	-> 162
/*      */       //   #240	-> 170
/*      */       //   #241	-> 179
/*      */       //   #242	-> 182
/*      */       //   #243	-> 185
/*      */       //   #244	-> 188
/*      */       //   #247	-> 191
/*      */       //   #249	-> 199
/*      */       //   #251	-> 204
/*      */       //   #257	-> 213
/*      */       //   #259	-> 222
/*      */       //   #260	-> 237
/*      */       //   #262	-> 245
/*      */       //   #265	-> 260
/*      */       //   #268	-> 269
/*      */       //   #270	-> 292
/*      */       //   #271	-> 295
/*      */       //   #276	-> 298
/*      */       //   #278	-> 315
/*      */       //   #279	-> 330
/*      */       //   #282	-> 335
/*      */       //   #283	-> 340
/*      */       //   #284	-> 361
/*      */       //   #285	-> 385
/*      */       //   #286	-> 388
/*      */       //   #289	-> 391
/*      */       //   #290	-> 403
/*      */       //   #291	-> 409
/*      */       //   #292	-> 430
/*      */       //   #293	-> 442
/*      */       //   #296	-> 448
/*      */       //   #301	-> 486
/*      */       //   #303	-> 494
/*      */       //   #304	-> 501
/*      */       //   #306	-> 509
/*      */       //   #307	-> 523
/*      */       //   #303	-> 526
/*      */       //   #312	-> 532
/*      */       //   #314	-> 550
/*      */       //   #316	-> 560
/*      */       //   #317	-> 567
/*      */       //   #319	-> 570
/*      */       //   #320	-> 583
/*      */       //   #321	-> 586
/*      */       //   #323	-> 594
/*      */       //   #324	-> 612
/*      */       //   #325	-> 625
/*      */       //   #327	-> 628
/*      */       //   #328	-> 637
/*      */       //   #319	-> 648
/*      */       //   #336	-> 657
/*      */       //   #337	-> 671
/*      */       //   #338	-> 674
/*      */       //   #343	-> 678
/*      */       //   #344	-> 695
/*      */       //   #345	-> 714
/*      */       //   #347	-> 720
/*      */       //   #348	-> 736
/*      */       //   #352	-> 741
/*      */       //   #353	-> 753
/*      */       //   #354	-> 767
/*      */       //   #356	-> 777
/*      */       //   #357	-> 791
/*      */       //   #359	-> 801
/*      */       //   #360	-> 815
/*      */       //   #362	-> 825
/*      */       //   #363	-> 839
/*      */       //   #365	-> 849
/*      */       //   #366	-> 863
/*      */       //   #259	-> 868
/*      */       //   #375	-> 874
/*      */       //   #376	-> 883
/*      */       //   #377	-> 892
/*      */       //   #379	-> 900
/*      */       //   #382	-> 908
/*      */       //   #385	-> 913
/*      */       //   #386	-> 934
/*      */       //   #387	-> 946
/*      */       //   #389	-> 952
/*      */       //   #390	-> 965
/*      */       //   #391	-> 977
/*      */       //   #392	-> 983
/*      */       //   #393	-> 989
/*      */       //   #395	-> 996
/*      */       //   #396	-> 1003
/*      */       //   #397	-> 1018
/*      */       //   #398	-> 1033
/*      */       //   #399	-> 1038
/*      */       //   #401	-> 1044
/*      */       //   #402	-> 1054
/*      */       //   #401	-> 1069
/*      */       //   #405	-> 1075
/*      */       //   #407	-> 1087
/*      */       //   #408	-> 1092
/*      */       //   #413	-> 1132
/*      */       //   #415	-> 1147
/*      */       //   #389	-> 1180
/*      */       //   #427	-> 1186
/*      */       //   #422	-> 1189
/*      */       //   #423	-> 1191
/*      */       //   #424	-> 1219
/*      */       //   #426	-> 1227
/*      */       //   #430	-> 1230
/*      */       //   #431	-> 1235
/*      */       //   #436	-> 1270
/*      */       //   #438	-> 1284
/*      */       //   #442	-> 1296
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   494	38	19	endOfStmt	I
/*      */       //   574	80	20	j	I
/*      */       //   560	94	19	cNext	C
/*      */       //   849	19	22	posT	C
/*      */       //   825	43	21	posI2	C
/*      */       //   801	67	20	posM	C
/*      */       //   777	91	19	posI1	C
/*      */       //   245	623	18	c	C
/*      */       //   1018	12	23	temp	Ljava/lang/String;
/*      */       //   1047	28	24	j	I
/*      */       //   1044	40	23	buf	[B
/*      */       //   1147	33	23	temp	Ljava/lang/String;
/*      */       //   977	203	19	ep	[I
/*      */       //   983	197	20	end	I
/*      */       //   989	191	21	begin	I
/*      */       //   996	184	22	len	I
/*      */       //   128	1058	8	quotedIdentifierString	Ljava/lang/String;
/*      */       //   131	1055	9	quotedIdentifierChar	C
/*      */       //   179	1007	10	endpointList	Ljava/util/ArrayList;
/*      */       //   182	1004	11	inQuotes	Z
/*      */       //   185	1001	12	quoteChar	C
/*      */       //   188	998	13	inQuotedId	Z
/*      */       //   191	995	14	lastParmEnd	I
/*      */       //   228	958	15	i	I
/*      */       //   199	987	16	stopLookingForLimitClause	I
/*      */       //   213	973	17	noBackslashEscapes	Z
/*      */       //   952	234	18	asCharArray	[C
/*      */       //   1219	11	9	sqlEx	Ljava/sql/SQLException;
/*      */       //   1191	39	8	oobEx	Ljava/lang/StringIndexOutOfBoundsException;
/*      */       //   0	1297	0	this	Lcom/mysql/jdbc/PreparedStatement$ParseInfo;
/*      */       //   0	1297	1	this$0	Lcom/mysql/jdbc/PreparedStatement;
/*      */       //   0	1297	2	sql	Ljava/lang/String;
/*      */       //   0	1297	3	conn	Lcom/mysql/jdbc/ConnectionImpl;
/*      */       //   0	1297	4	dbmd	Ljava/sql/DatabaseMetaData;
/*      */       //   0	1297	5	encoding	Ljava/lang/String;
/*      */       //   0	1297	6	converter	Lcom/mysql/jdbc/SingleByteCharsetConverter;
/*      */       //   0	1297	7	buildRewriteInfo	Z
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   67	1186	1189	java/lang/StringIndexOutOfBoundsException
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void buildRewriteBatchedParams(String sql, ConnectionImpl conn, DatabaseMetaData metadata, String encoding, SingleByteCharsetConverter converter) throws SQLException {
/*  453 */       this.valuesClause = extractValuesClause(sql);
/*  454 */       String odkuClause = this.isOnDuplicateKeyUpdate ? sql.substring(this.locationOfOnDuplicateKeyUpdate) : null;
/*      */ 
/*      */       
/*  457 */       String headSql = null;
/*      */       
/*  459 */       if (this.isOnDuplicateKeyUpdate) {
/*  460 */         headSql = sql.substring(0, this.locationOfOnDuplicateKeyUpdate);
/*      */       } else {
/*  462 */         headSql = sql;
/*      */       } 
/*      */       
/*  465 */       this.batchHead = new ParseInfo(headSql, conn, metadata, encoding, converter, false);
/*      */       
/*  467 */       this.batchValues = new ParseInfo("," + this.valuesClause, conn, metadata, encoding, converter, false);
/*      */       
/*  469 */       this.batchODKUClause = null;
/*      */       
/*  471 */       if (odkuClause != null && odkuClause.length() > 0) {
/*  472 */         this.batchODKUClause = new ParseInfo("," + this.valuesClause + " " + odkuClause, conn, metadata, encoding, converter, false);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private String extractValuesClause(String sql) throws SQLException {
/*  479 */       String quoteCharStr = PreparedStatement.this.connection.getMetaData().getIdentifierQuoteString();
/*      */ 
/*      */       
/*  482 */       int indexOfValues = -1;
/*  483 */       int valuesSearchStart = this.statementStartPos;
/*      */       
/*  485 */       while (indexOfValues == -1) {
/*  486 */         if (quoteCharStr.length() > 0) {
/*  487 */           indexOfValues = StringUtils.indexOfIgnoreCaseRespectQuotes(valuesSearchStart, PreparedStatement.this.originalSql, "VALUES ", quoteCharStr.charAt(0), false);
/*      */         }
/*      */         else {
/*      */           
/*  491 */           indexOfValues = StringUtils.indexOfIgnoreCase(valuesSearchStart, PreparedStatement.this.originalSql, "VALUES ");
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  496 */         if (indexOfValues > 0) {
/*  497 */           char c = PreparedStatement.this.originalSql.charAt(indexOfValues - 1);
/*  498 */           switch (c) {
/*      */             case '\t':
/*      */             case '\n':
/*      */             case ' ':
/*      */             case ')':
/*      */             case '`':
/*      */               continue;
/*      */           } 
/*  506 */           valuesSearchStart = indexOfValues + 7;
/*  507 */           indexOfValues = -1;
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  515 */       if (indexOfValues == -1) {
/*  516 */         return null;
/*      */       }
/*      */       
/*  519 */       int indexOfFirstParen = sql.indexOf('(', indexOfValues + 7);
/*      */       
/*  521 */       if (indexOfFirstParen == -1) {
/*  522 */         return null;
/*      */       }
/*      */       
/*  525 */       int endOfValuesClause = sql.lastIndexOf(')');
/*      */       
/*  527 */       if (endOfValuesClause == -1) {
/*  528 */         return null;
/*      */       }
/*      */       
/*  531 */       if (this.isOnDuplicateKeyUpdate) {
/*  532 */         endOfValuesClause = this.locationOfOnDuplicateKeyUpdate - 1;
/*      */       }
/*      */       
/*  535 */       return sql.substring(indexOfFirstParen, endOfValuesClause + 1);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     synchronized ParseInfo getParseInfoForBatch(int numBatch) {
/*  542 */       PreparedStatement.AppendingBatchVisitor apv = new PreparedStatement.AppendingBatchVisitor(PreparedStatement.this);
/*  543 */       buildInfoForBatch(numBatch, apv);
/*      */       
/*  545 */       ParseInfo batchParseInfo = new ParseInfo(apv.getStaticSqlStrings(), this.firstStmtChar, this.foundLimitClause, this.foundLoadData, this.isOnDuplicateKeyUpdate, this.locationOfOnDuplicateKeyUpdate, this.statementLength, this.statementStartPos);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  551 */       return batchParseInfo;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     String getSqlForBatch(int numBatch) throws UnsupportedEncodingException {
/*  560 */       ParseInfo batchInfo = getParseInfoForBatch(numBatch);
/*  561 */       int size = 0;
/*  562 */       byte[][] sqlStrings = batchInfo.staticSql;
/*  563 */       int sqlStringsLength = sqlStrings.length;
/*      */       
/*  565 */       for (int i = 0; i < sqlStringsLength; i++) {
/*  566 */         size += (sqlStrings[i]).length;
/*  567 */         size++;
/*      */       } 
/*      */       
/*  570 */       StringBuffer buf = new StringBuffer(size);
/*      */       
/*  572 */       for (int j = 0; j < sqlStringsLength - 1; j++) {
/*  573 */         buf.append(new String(sqlStrings[j], PreparedStatement.this.charEncoding));
/*  574 */         buf.append("?");
/*      */       } 
/*      */       
/*  577 */       buf.append(new String(sqlStrings[sqlStringsLength - 1]));
/*      */       
/*  579 */       return buf.toString();
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
/*      */     private void buildInfoForBatch(int numBatch, PreparedStatement.BatchVisitor visitor) {
/*  591 */       byte[][] headStaticSql = this.batchHead.staticSql;
/*  592 */       int headStaticSqlLength = headStaticSql.length;
/*      */       
/*  594 */       if (headStaticSqlLength > 1) {
/*  595 */         for (int j = 0; j < headStaticSqlLength - 1; j++) {
/*  596 */           visitor.append(headStaticSql[j]).increment();
/*      */         }
/*      */       }
/*      */ 
/*      */       
/*  601 */       byte[] endOfHead = headStaticSql[headStaticSqlLength - 1];
/*  602 */       byte[][] valuesStaticSql = this.batchValues.staticSql;
/*  603 */       byte[] beginOfValues = valuesStaticSql[0];
/*      */       
/*  605 */       visitor.merge(endOfHead, beginOfValues).increment();
/*      */       
/*  607 */       int numValueRepeats = numBatch - 1;
/*      */       
/*  609 */       if (this.batchODKUClause != null) {
/*  610 */         numValueRepeats--;
/*      */       }
/*      */       
/*  613 */       int valuesStaticSqlLength = valuesStaticSql.length;
/*  614 */       byte[] endOfValues = valuesStaticSql[valuesStaticSqlLength - 1];
/*      */       
/*  616 */       for (int i = 0; i < numValueRepeats; i++) {
/*  617 */         for (int j = 1; j < valuesStaticSqlLength - 1; j++) {
/*  618 */           visitor.append(valuesStaticSql[j]).increment();
/*      */         }
/*  620 */         visitor.merge(endOfValues, beginOfValues).increment();
/*      */       } 
/*      */       
/*  623 */       if (this.batchODKUClause != null) {
/*  624 */         byte[][] batchOdkuStaticSql = this.batchODKUClause.staticSql;
/*  625 */         byte[] beginOfOdku = batchOdkuStaticSql[0];
/*  626 */         visitor.decrement().merge(endOfValues, beginOfOdku).increment();
/*      */         
/*  628 */         int batchOdkuStaticSqlLength = batchOdkuStaticSql.length;
/*      */         
/*  630 */         if (numBatch > 1) {
/*  631 */           for (int j = 1; j < batchOdkuStaticSqlLength; j++) {
/*  632 */             visitor.append(batchOdkuStaticSql[j]).increment();
/*      */           }
/*      */         } else {
/*      */           
/*  636 */           visitor.decrement().append(batchOdkuStaticSql[batchOdkuStaticSqlLength - 1]);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  641 */         visitor.decrement().append(this.staticSql[this.staticSql.length - 1]);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private ParseInfo(byte[][] staticSql, char firstStmtChar, boolean foundLimitClause, boolean foundLoadData, boolean isOnDuplicateKeyUpdate, int locationOfOnDuplicateKeyUpdate, int statementLength, int statementStartPos) {
/*  649 */       PreparedStatement.this = PreparedStatement.this; this.firstStmtChar = Character.MIN_VALUE; this.foundLimitClause = false; this.foundLoadData = false; this.lastUsed = 0L; this.statementLength = 0; this.statementStartPos = 0; this.canRewriteAsMultiValueInsert = false; this.staticSql = (byte[][])null; this.isOnDuplicateKeyUpdate = false; this.locationOfOnDuplicateKeyUpdate = -1; this.parametersInDuplicateKeyClause = false;
/*  650 */       this.firstStmtChar = firstStmtChar;
/*  651 */       this.foundLimitClause = foundLimitClause;
/*  652 */       this.foundLoadData = foundLoadData;
/*  653 */       this.isOnDuplicateKeyUpdate = isOnDuplicateKeyUpdate;
/*  654 */       this.locationOfOnDuplicateKeyUpdate = locationOfOnDuplicateKeyUpdate;
/*  655 */       this.statementLength = statementLength;
/*  656 */       this.statementStartPos = statementStartPos;
/*  657 */       this.staticSql = staticSql;
/*      */     }
/*      */   }
/*      */   static interface BatchVisitor { BatchVisitor increment();
/*      */     BatchVisitor decrement();
/*      */     
/*      */     BatchVisitor append(byte[] param1ArrayOfbyte);
/*      */     
/*      */     BatchVisitor merge(byte[] param1ArrayOfbyte1, byte[] param1ArrayOfbyte2); }
/*      */   
/*      */   class AppendingBatchVisitor implements BatchVisitor { LinkedList statementComponents;
/*      */     private final PreparedStatement this$0;
/*      */     
/*      */     AppendingBatchVisitor(PreparedStatement this$0) {
/*  671 */       this.this$0 = this$0;
/*  672 */       this.statementComponents = new LinkedList();
/*      */     }
/*      */     public PreparedStatement.BatchVisitor append(byte[] values) {
/*  675 */       this.statementComponents.addLast(values);
/*      */       
/*  677 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public PreparedStatement.BatchVisitor increment() {
/*  682 */       return this;
/*      */     }
/*      */     
/*      */     public PreparedStatement.BatchVisitor decrement() {
/*  686 */       this.statementComponents.removeLast();
/*      */       
/*  688 */       return this;
/*      */     }
/*      */     
/*      */     public PreparedStatement.BatchVisitor merge(byte[] front, byte[] back) {
/*  692 */       int mergedLength = front.length + back.length;
/*  693 */       byte[] merged = new byte[mergedLength];
/*  694 */       System.arraycopy(front, 0, merged, 0, front.length);
/*  695 */       System.arraycopy(back, 0, merged, front.length, back.length);
/*  696 */       this.statementComponents.addLast(merged);
/*  697 */       return this;
/*      */     }
/*      */     
/*      */     public byte[][] getStaticSqlStrings() {
/*  701 */       byte[][] asBytes = new byte[this.statementComponents.size()][];
/*  702 */       this.statementComponents.toArray((Object[])asBytes);
/*      */       
/*  704 */       return asBytes;
/*      */     }
/*      */     
/*      */     public String toString() {
/*  708 */       StringBuffer buf = new StringBuffer();
/*  709 */       Iterator iter = this.statementComponents.iterator();
/*  710 */       while (iter.hasNext()) {
/*  711 */         buf.append(new String(iter.next()));
/*      */       }
/*      */       
/*  714 */       return buf.toString();
/*      */     } }
/*      */ 
/*      */ 
/*      */   
/*  719 */   private static final byte[] HEX_DIGITS = new byte[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static int readFully(Reader reader, char[] buf, int length) throws IOException {
/*  742 */     int numCharsRead = 0;
/*      */     
/*  744 */     while (numCharsRead < length) {
/*  745 */       int count = reader.read(buf, numCharsRead, length - numCharsRead);
/*      */       
/*  747 */       if (count < 0) {
/*      */         break;
/*      */       }
/*      */       
/*  751 */       numCharsRead += count;
/*      */     } 
/*      */     
/*  754 */     return numCharsRead;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean batchHasPlainStatements = false;
/*      */ 
/*      */ 
/*      */   
/*  765 */   private DatabaseMetaData dbmd = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  771 */   protected char firstCharOfStmt = Character.MIN_VALUE;
/*      */ 
/*      */   
/*      */   protected boolean hasLimitClause = false;
/*      */ 
/*      */   
/*      */   protected boolean isLoadDataQuery = false;
/*      */   
/*  779 */   private boolean[] isNull = null;
/*      */   
/*  781 */   private boolean[] isStream = null;
/*      */   
/*  783 */   protected int numberOfExecutions = 0;
/*      */ 
/*      */   
/*  786 */   protected String originalSql = null;
/*      */ 
/*      */   
/*      */   protected int parameterCount;
/*      */   
/*      */   protected MysqlParameterMetadata parameterMetaData;
/*      */   
/*  793 */   private InputStream[] parameterStreams = null;
/*      */   
/*  795 */   private byte[][] parameterValues = (byte[][])null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  801 */   protected int[] parameterTypes = null;
/*      */   
/*      */   protected ParseInfo parseInfo;
/*      */   
/*      */   private ResultSetMetaData pstmtResultMetaData;
/*      */   
/*  807 */   private byte[][] staticSqlStrings = (byte[][])null;
/*      */   
/*  809 */   private byte[] streamConvertBuf = new byte[4096];
/*      */   
/*  811 */   private int[] streamLengths = null;
/*      */   
/*  813 */   private SimpleDateFormat tsdf = null;
/*      */ 
/*      */   
/*      */   protected boolean useTrueBoolean = false;
/*      */ 
/*      */   
/*      */   protected boolean usingAnsiMode;
/*      */ 
/*      */   
/*      */   protected String batchedValuesClause;
/*      */   
/*      */   private boolean doPingInstead;
/*      */   
/*      */   private SimpleDateFormat ddf;
/*      */   
/*      */   private SimpleDateFormat tdf;
/*      */   
/*      */   private boolean compensateForOnDuplicateKeyUpdate = false;
/*      */   
/*      */   private CharsetEncoder charsetEncoder;
/*      */   
/*  834 */   private int batchCommandIndex = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static PreparedStatement getInstance(ConnectionImpl conn, String catalog) throws SQLException {
/*  845 */     if (!Util.isJdbc4()) {
/*  846 */       return new PreparedStatement(conn, catalog);
/*      */     }
/*      */     
/*  849 */     return (PreparedStatement)Util.handleNewInstance(JDBC_4_PSTMT_2_ARG_CTOR, new Object[] { conn, catalog }, conn.getExceptionInterceptor());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static PreparedStatement getInstance(ConnectionImpl conn, String sql, String catalog) throws SQLException {
/*  862 */     if (!Util.isJdbc4()) {
/*  863 */       return new PreparedStatement(conn, sql, catalog);
/*      */     }
/*      */     
/*  866 */     return (PreparedStatement)Util.handleNewInstance(JDBC_4_PSTMT_3_ARG_CTOR, new Object[] { conn, sql, catalog }, conn.getExceptionInterceptor());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static PreparedStatement getInstance(ConnectionImpl conn, String sql, String catalog, ParseInfo cachedParseInfo) throws SQLException {
/*  879 */     if (!Util.isJdbc4()) {
/*  880 */       return new PreparedStatement(conn, sql, catalog, cachedParseInfo);
/*      */     }
/*      */     
/*  883 */     return (PreparedStatement)Util.handleNewInstance(JDBC_4_PSTMT_4_ARG_CTOR, new Object[] { conn, sql, catalog, cachedParseInfo }, conn.getExceptionInterceptor());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PreparedStatement(ConnectionImpl conn, String catalog) throws SQLException {
/*  901 */     super(conn, catalog);
/*      */     
/*  903 */     this.compensateForOnDuplicateKeyUpdate = this.connection.getCompensateOnDuplicateKeyUpdateCounts();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PreparedStatement(ConnectionImpl conn, String sql, String catalog) throws SQLException {
/*  921 */     super(conn, catalog);
/*      */     
/*  923 */     if (sql == null) {
/*  924 */       throw SQLError.createSQLException(Messages.getString("PreparedStatement.0"), "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */     
/*  928 */     this.originalSql = sql;
/*      */     
/*  930 */     if (this.originalSql.startsWith("/* ping */")) {
/*  931 */       this.doPingInstead = true;
/*      */     } else {
/*  933 */       this.doPingInstead = false;
/*      */     } 
/*      */     
/*  936 */     this.dbmd = this.connection.getMetaData();
/*      */     
/*  938 */     this.useTrueBoolean = this.connection.versionMeetsMinimum(3, 21, 23);
/*      */     
/*  940 */     this.parseInfo = new ParseInfo(sql, this.connection, this.dbmd, this.charEncoding, this.charConverter);
/*      */ 
/*      */     
/*  943 */     initializeFromParseInfo();
/*      */     
/*  945 */     this.compensateForOnDuplicateKeyUpdate = this.connection.getCompensateOnDuplicateKeyUpdateCounts();
/*      */     
/*  947 */     if (conn.getRequiresEscapingEncoder()) {
/*  948 */       this.charsetEncoder = Charset.forName(conn.getEncoding()).newEncoder();
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
/*      */   public PreparedStatement(ConnectionImpl conn, String sql, String catalog, ParseInfo cachedParseInfo) throws SQLException {
/*  968 */     super(conn, catalog);
/*      */     
/*  970 */     if (sql == null) {
/*  971 */       throw SQLError.createSQLException(Messages.getString("PreparedStatement.1"), "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */     
/*  975 */     this.originalSql = sql;
/*      */     
/*  977 */     this.dbmd = this.connection.getMetaData();
/*      */     
/*  979 */     this.useTrueBoolean = this.connection.versionMeetsMinimum(3, 21, 23);
/*      */     
/*  981 */     this.parseInfo = cachedParseInfo;
/*      */     
/*  983 */     this.usingAnsiMode = !this.connection.useAnsiQuotedIdentifiers();
/*      */     
/*  985 */     initializeFromParseInfo();
/*      */     
/*  987 */     this.compensateForOnDuplicateKeyUpdate = this.connection.getCompensateOnDuplicateKeyUpdateCounts();
/*      */     
/*  989 */     if (conn.getRequiresEscapingEncoder()) {
/*  990 */       this.charsetEncoder = Charset.forName(conn.getEncoding()).newEncoder();
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
/*      */   public void addBatch() throws SQLException {
/* 1002 */     if (this.batchedArgs == null) {
/* 1003 */       this.batchedArgs = new ArrayList();
/*      */     }
/*      */     
/* 1006 */     for (int i = 0; i < this.parameterValues.length; i++) {
/* 1007 */       checkAllParametersSet(this.parameterValues[i], this.parameterStreams[i], i);
/*      */     }
/*      */ 
/*      */     
/* 1011 */     this.batchedArgs.add(new BatchParams(this, this.parameterValues, this.parameterStreams, this.isStream, this.streamLengths, this.isNull));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void addBatch(String sql) throws SQLException {
/* 1017 */     this.batchHasPlainStatements = true;
/*      */     
/* 1019 */     super.addBatch(sql);
/*      */   }
/*      */   
/*      */   protected String asSql() throws SQLException {
/* 1023 */     return asSql(false);
/*      */   }
/*      */   
/*      */   protected String asSql(boolean quoteStreamsAndUnknowns) throws SQLException {
/* 1027 */     if (this.isClosed) {
/* 1028 */       return "statement has been closed, no further internal information available";
/*      */     }
/*      */     
/* 1031 */     StringBuffer buf = new StringBuffer();
/*      */     
/*      */     try {
/* 1034 */       int realParameterCount = this.parameterCount + getParameterIndexOffset();
/* 1035 */       Object batchArg = null;
/* 1036 */       if (this.batchCommandIndex != -1) {
/* 1037 */         batchArg = this.batchedArgs.get(this.batchCommandIndex);
/*      */       }
/* 1039 */       for (int i = 0; i < realParameterCount; i++) {
/* 1040 */         if (this.charEncoding != null) {
/* 1041 */           buf.append(new String(this.staticSqlStrings[i], this.charEncoding));
/*      */         } else {
/*      */           
/* 1044 */           buf.append(new String(this.staticSqlStrings[i]));
/*      */         } 
/*      */         
/* 1047 */         byte[] val = null;
/* 1048 */         if (batchArg != null && batchArg instanceof String) {
/* 1049 */           buf.append((String)batchArg);
/*      */         } else {
/*      */           
/* 1052 */           if (this.batchCommandIndex == -1) {
/* 1053 */             val = this.parameterValues[i];
/*      */           } else {
/* 1055 */             val = ((BatchParams)batchArg).parameterStrings[i];
/*      */           } 
/* 1057 */           boolean isStreamParam = false;
/* 1058 */           if (this.batchCommandIndex == -1) {
/* 1059 */             isStreamParam = this.isStream[i];
/*      */           } else {
/* 1061 */             isStreamParam = ((BatchParams)batchArg).isStream[i];
/*      */           } 
/* 1063 */           if (val == null && !isStreamParam) {
/* 1064 */             if (quoteStreamsAndUnknowns) {
/* 1065 */               buf.append("'");
/*      */             }
/*      */             
/* 1068 */             buf.append("** NOT SPECIFIED **");
/*      */             
/* 1070 */             if (quoteStreamsAndUnknowns) {
/* 1071 */               buf.append("'");
/*      */             }
/* 1073 */           } else if (isStreamParam) {
/* 1074 */             if (quoteStreamsAndUnknowns) {
/* 1075 */               buf.append("'");
/*      */             }
/*      */             
/* 1078 */             buf.append("** STREAM DATA **");
/*      */             
/* 1080 */             if (quoteStreamsAndUnknowns) {
/* 1081 */               buf.append("'");
/*      */             }
/*      */           }
/* 1084 */           else if (this.charConverter != null) {
/* 1085 */             buf.append(this.charConverter.toString(val));
/*      */           }
/* 1087 */           else if (this.charEncoding != null) {
/* 1088 */             buf.append(new String(val, this.charEncoding));
/*      */           } else {
/* 1090 */             buf.append(StringUtils.toAsciiString(val));
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1096 */       if (this.charEncoding != null) {
/* 1097 */         buf.append(new String(this.staticSqlStrings[this.parameterCount + getParameterIndexOffset()], this.charEncoding));
/*      */       }
/*      */       else {
/*      */         
/* 1101 */         buf.append(StringUtils.toAsciiString(this.staticSqlStrings[this.parameterCount + getParameterIndexOffset()]));
/*      */       }
/*      */     
/*      */     }
/* 1105 */     catch (UnsupportedEncodingException uue) {
/* 1106 */       throw new RuntimeException(Messages.getString("PreparedStatement.32") + this.charEncoding + Messages.getString("PreparedStatement.33"));
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1112 */     return buf.toString();
/*      */   }
/*      */   
/*      */   public synchronized void clearBatch() throws SQLException {
/* 1116 */     this.batchHasPlainStatements = false;
/*      */     
/* 1118 */     super.clearBatch();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void clearParameters() throws SQLException {
/* 1132 */     checkClosed();
/*      */     
/* 1134 */     for (int i = 0; i < this.parameterValues.length; i++) {
/* 1135 */       this.parameterValues[i] = null;
/* 1136 */       this.parameterStreams[i] = null;
/* 1137 */       this.isStream[i] = false;
/* 1138 */       this.isNull[i] = false;
/* 1139 */       this.parameterTypes[i] = 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void close() throws SQLException {
/* 1150 */     realClose(true, true);
/*      */   }
/*      */ 
/*      */   
/*      */   private final void escapeblockFast(byte[] buf, Buffer packet, int size) throws SQLException {
/* 1155 */     int lastwritten = 0;
/*      */     
/* 1157 */     for (int i = 0; i < size; i++) {
/* 1158 */       byte b = buf[i];
/*      */       
/* 1160 */       if (b == 0) {
/*      */         
/* 1162 */         if (i > lastwritten) {
/* 1163 */           packet.writeBytesNoNull(buf, lastwritten, i - lastwritten);
/*      */         }
/*      */ 
/*      */         
/* 1167 */         packet.writeByte((byte)92);
/* 1168 */         packet.writeByte((byte)48);
/* 1169 */         lastwritten = i + 1;
/*      */       }
/* 1171 */       else if (b == 92 || b == 39 || (!this.usingAnsiMode && b == 34)) {
/*      */ 
/*      */         
/* 1174 */         if (i > lastwritten) {
/* 1175 */           packet.writeBytesNoNull(buf, lastwritten, i - lastwritten);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 1180 */         packet.writeByte((byte)92);
/* 1181 */         lastwritten = i;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1187 */     if (lastwritten < size) {
/* 1188 */       packet.writeBytesNoNull(buf, lastwritten, size - lastwritten);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private final void escapeblockFast(byte[] buf, ByteArrayOutputStream bytesOut, int size) {
/* 1194 */     int lastwritten = 0;
/*      */     
/* 1196 */     for (int i = 0; i < size; i++) {
/* 1197 */       byte b = buf[i];
/*      */       
/* 1199 */       if (b == 0) {
/*      */         
/* 1201 */         if (i > lastwritten) {
/* 1202 */           bytesOut.write(buf, lastwritten, i - lastwritten);
/*      */         }
/*      */ 
/*      */         
/* 1206 */         bytesOut.write(92);
/* 1207 */         bytesOut.write(48);
/* 1208 */         lastwritten = i + 1;
/*      */       }
/* 1210 */       else if (b == 92 || b == 39 || (!this.usingAnsiMode && b == 34)) {
/*      */ 
/*      */         
/* 1213 */         if (i > lastwritten) {
/* 1214 */           bytesOut.write(buf, lastwritten, i - lastwritten);
/*      */         }
/*      */ 
/*      */         
/* 1218 */         bytesOut.write(92);
/* 1219 */         lastwritten = i;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1225 */     if (lastwritten < size) {
/* 1226 */       bytesOut.write(buf, lastwritten, size - lastwritten);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean checkReadOnlySafeStatement() throws SQLException {
/* 1237 */     return (!this.connection.isReadOnly() || this.firstCharOfStmt == 'S');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean execute() throws SQLException {
/* 1252 */     checkClosed();
/*      */     
/* 1254 */     ConnectionImpl locallyScopedConn = this.connection;
/*      */     
/* 1256 */     if (!checkReadOnlySafeStatement()) {
/* 1257 */       throw SQLError.createSQLException(Messages.getString("PreparedStatement.20") + Messages.getString("PreparedStatement.21"), "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1262 */     ResultSetInternalMethods rs = null;
/*      */     
/* 1264 */     CachedResultSetMetaData cachedMetadata = null;
/*      */     
/* 1266 */     synchronized (locallyScopedConn.getMutex()) {
/* 1267 */       this.lastQueryIsOnDupKeyUpdate = false;
/* 1268 */       if (this.retrieveGeneratedKeys)
/* 1269 */         this.lastQueryIsOnDupKeyUpdate = containsOnDuplicateKeyUpdateInSQL(); 
/* 1270 */       boolean doStreaming = createStreamingResultSet();
/*      */       
/* 1272 */       clearWarnings();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1282 */       if (doStreaming && this.connection.getNetTimeoutForStreamingResults() > 0)
/*      */       {
/* 1284 */         executeSimpleNonQuery(locallyScopedConn, "SET net_write_timeout=" + this.connection.getNetTimeoutForStreamingResults());
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1290 */       this.batchedGeneratedKeys = null;
/*      */       
/* 1292 */       Buffer sendPacket = fillSendPacket();
/*      */       
/* 1294 */       String oldCatalog = null;
/*      */       
/* 1296 */       if (!locallyScopedConn.getCatalog().equals(this.currentCatalog)) {
/* 1297 */         oldCatalog = locallyScopedConn.getCatalog();
/* 1298 */         locallyScopedConn.setCatalog(this.currentCatalog);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1304 */       if (locallyScopedConn.getCacheResultSetMetadata()) {
/* 1305 */         cachedMetadata = locallyScopedConn.getCachedMetaData(this.originalSql);
/*      */       }
/*      */       
/* 1308 */       Field[] metadataFromCache = null;
/*      */       
/* 1310 */       if (cachedMetadata != null) {
/* 1311 */         metadataFromCache = cachedMetadata.fields;
/*      */       }
/*      */       
/* 1314 */       boolean oldInfoMsgState = false;
/*      */       
/* 1316 */       if (this.retrieveGeneratedKeys) {
/* 1317 */         oldInfoMsgState = locallyScopedConn.isReadInfoMsgEnabled();
/* 1318 */         locallyScopedConn.setReadInfoMsgEnabled(true);
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
/* 1330 */       if (locallyScopedConn.useMaxRows()) {
/* 1331 */         int rowLimit = -1;
/*      */         
/* 1333 */         if (this.firstCharOfStmt == 'S') {
/* 1334 */           if (this.hasLimitClause) {
/* 1335 */             rowLimit = this.maxRows;
/*      */           }
/* 1337 */           else if (this.maxRows <= 0) {
/* 1338 */             executeSimpleNonQuery(locallyScopedConn, "SET OPTION SQL_SELECT_LIMIT=DEFAULT");
/*      */           } else {
/*      */             
/* 1341 */             executeSimpleNonQuery(locallyScopedConn, "SET OPTION SQL_SELECT_LIMIT=" + this.maxRows);
/*      */           }
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 1347 */           executeSimpleNonQuery(locallyScopedConn, "SET OPTION SQL_SELECT_LIMIT=DEFAULT");
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1352 */         rs = executeInternal(rowLimit, sendPacket, doStreaming, (this.firstCharOfStmt == 'S'), metadataFromCache, false);
/*      */       }
/*      */       else {
/*      */         
/* 1356 */         rs = executeInternal(-1, sendPacket, doStreaming, (this.firstCharOfStmt == 'S'), metadataFromCache, false);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1361 */       if (cachedMetadata != null) {
/* 1362 */         locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, cachedMetadata, this.results);
/*      */       
/*      */       }
/* 1365 */       else if (rs.reallyResult() && locallyScopedConn.getCacheResultSetMetadata()) {
/* 1366 */         locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, (CachedResultSetMetaData)null, rs);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1371 */       if (this.retrieveGeneratedKeys) {
/* 1372 */         locallyScopedConn.setReadInfoMsgEnabled(oldInfoMsgState);
/* 1373 */         rs.setFirstCharOfQuery(this.firstCharOfStmt);
/*      */       } 
/*      */       
/* 1376 */       if (oldCatalog != null) {
/* 1377 */         locallyScopedConn.setCatalog(oldCatalog);
/*      */       }
/*      */       
/* 1380 */       if (rs != null) {
/* 1381 */         this.lastInsertId = rs.getUpdateID();
/*      */         
/* 1383 */         this.results = rs;
/*      */       } 
/*      */     } 
/*      */     
/* 1387 */     return (rs != null && rs.reallyResult());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int[] executeBatch() throws SQLException {
/* 1405 */     checkClosed();
/*      */     
/* 1407 */     if (this.connection.isReadOnly()) {
/* 1408 */       throw new SQLException(Messages.getString("PreparedStatement.25") + Messages.getString("PreparedStatement.26"), "S1009");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1413 */     synchronized (this.connection.getMutex()) {
/* 1414 */       if (this.batchedArgs == null || this.batchedArgs.size() == 0) {
/* 1415 */         return new int[0];
/*      */       }
/*      */ 
/*      */       
/* 1419 */       int batchTimeout = this.timeoutInMillis;
/* 1420 */       this.timeoutInMillis = 0;
/*      */       
/* 1422 */       resetCancelledState();
/*      */       
/*      */       try {
/* 1425 */         clearWarnings();
/*      */         
/* 1427 */         if (!this.batchHasPlainStatements && this.connection.getRewriteBatchedStatements()) {
/*      */ 
/*      */ 
/*      */           
/* 1431 */           if (canRewriteAsMultiValueInsertAtSqlLevel()) {
/* 1432 */             return executeBatchedInserts(batchTimeout);
/*      */           }
/*      */           
/* 1435 */           if (this.connection.versionMeetsMinimum(4, 1, 0) && !this.batchHasPlainStatements && this.batchedArgs != null && this.batchedArgs.size() > 3)
/*      */           {
/*      */ 
/*      */             
/* 1439 */             return executePreparedBatchAsMultiStatement(batchTimeout);
/*      */           }
/*      */         } 
/*      */         
/* 1443 */         return executeBatchSerially(batchTimeout);
/*      */       } finally {
/* 1445 */         clearBatch();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean canRewriteAsMultiValueInsertAtSqlLevel() throws SQLException {
/* 1451 */     return this.parseInfo.canRewriteAsMultiValueInsert;
/*      */   }
/*      */   
/*      */   protected int getLocationOfOnDuplicateKeyUpdate() {
/* 1455 */     return this.parseInfo.locationOfOnDuplicateKeyUpdate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int[] executePreparedBatchAsMultiStatement(int batchTimeout) throws SQLException {
/* 1469 */     synchronized (this.connection.getMutex()) {
/*      */       
/* 1471 */       if (this.batchedValuesClause == null) {
/* 1472 */         this.batchedValuesClause = this.originalSql + ";";
/*      */       }
/*      */       
/* 1475 */       ConnectionImpl locallyScopedConn = this.connection;
/*      */       
/* 1477 */       boolean multiQueriesEnabled = locallyScopedConn.getAllowMultiQueries();
/* 1478 */       StatementImpl.CancelTask timeoutTask = null;
/*      */       
/*      */       try {
/* 1481 */         clearWarnings();
/*      */         
/* 1483 */         int numBatchedArgs = this.batchedArgs.size();
/*      */         
/* 1485 */         if (this.retrieveGeneratedKeys) {
/* 1486 */           this.batchedGeneratedKeys = new ArrayList(numBatchedArgs);
/*      */         }
/*      */         
/* 1489 */         int numValuesPerBatch = computeBatchSize(numBatchedArgs);
/*      */         
/* 1491 */         if (numBatchedArgs < numValuesPerBatch) {
/* 1492 */           numValuesPerBatch = numBatchedArgs;
/*      */         }
/*      */         
/* 1495 */         PreparedStatement batchedStatement = null;
/*      */         
/* 1497 */         int batchedParamIndex = 1;
/* 1498 */         int numberToExecuteAsMultiValue = 0;
/* 1499 */         int batchCounter = 0;
/* 1500 */         int updateCountCounter = 0;
/* 1501 */         int[] updateCounts = new int[numBatchedArgs];
/* 1502 */         SQLException sqlEx = null;
/*      */         
/*      */         try {
/* 1505 */           if (!multiQueriesEnabled) {
/* 1506 */             locallyScopedConn.getIO().enableMultiQueries();
/*      */           }
/*      */           
/* 1509 */           if (this.retrieveGeneratedKeys) {
/* 1510 */             batchedStatement = locallyScopedConn.prepareStatement(generateMultiStatementForBatch(numValuesPerBatch), 1);
/*      */           }
/*      */           else {
/*      */             
/* 1514 */             batchedStatement = locallyScopedConn.prepareStatement(generateMultiStatementForBatch(numValuesPerBatch));
/*      */           } 
/*      */ 
/*      */           
/* 1518 */           if (locallyScopedConn.getEnableQueryTimeouts() && batchTimeout != 0 && locallyScopedConn.versionMeetsMinimum(5, 0, 0)) {
/*      */ 
/*      */             
/* 1521 */             timeoutTask = new StatementImpl.CancelTask(this, (StatementImpl)batchedStatement);
/* 1522 */             ConnectionImpl.getCancelTimer().schedule(timeoutTask, batchTimeout);
/*      */           } 
/*      */ 
/*      */           
/* 1526 */           if (numBatchedArgs < numValuesPerBatch) {
/* 1527 */             numberToExecuteAsMultiValue = numBatchedArgs;
/*      */           } else {
/* 1529 */             numberToExecuteAsMultiValue = numBatchedArgs / numValuesPerBatch;
/*      */           } 
/*      */           
/* 1532 */           int numberArgsToExecute = numberToExecuteAsMultiValue * numValuesPerBatch;
/*      */           
/* 1534 */           for (int i = 0; i < numberArgsToExecute; i++) {
/* 1535 */             if (i != 0 && i % numValuesPerBatch == 0) {
/*      */               try {
/* 1537 */                 batchedStatement.execute();
/* 1538 */               } catch (SQLException ex) {
/* 1539 */                 sqlEx = handleExceptionForBatch(batchCounter, numValuesPerBatch, updateCounts, ex);
/*      */               } 
/*      */ 
/*      */               
/* 1543 */               updateCountCounter = processMultiCountsAndKeys((StatementImpl)batchedStatement, updateCountCounter, updateCounts);
/*      */ 
/*      */ 
/*      */               
/* 1547 */               batchedStatement.clearParameters();
/* 1548 */               batchedParamIndex = 1;
/*      */             } 
/*      */             
/* 1551 */             batchedParamIndex = setOneBatchedParameterSet(batchedStatement, batchedParamIndex, this.batchedArgs.get(batchCounter++));
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*      */           try {
/* 1557 */             batchedStatement.execute();
/* 1558 */           } catch (SQLException ex) {
/* 1559 */             sqlEx = handleExceptionForBatch(batchCounter - 1, numValuesPerBatch, updateCounts, ex);
/*      */           } 
/*      */ 
/*      */           
/* 1563 */           updateCountCounter = processMultiCountsAndKeys((StatementImpl)batchedStatement, updateCountCounter, updateCounts);
/*      */ 
/*      */ 
/*      */           
/* 1567 */           batchedStatement.clearParameters();
/*      */           
/* 1569 */           numValuesPerBatch = numBatchedArgs - batchCounter;
/*      */         } finally {
/* 1571 */           if (batchedStatement != null) {
/* 1572 */             batchedStatement.close();
/*      */           }
/*      */         } 
/*      */         
/*      */         try {
/* 1577 */           if (numValuesPerBatch > 0) {
/*      */             
/* 1579 */             if (this.retrieveGeneratedKeys) {
/* 1580 */               batchedStatement = locallyScopedConn.prepareStatement(generateMultiStatementForBatch(numValuesPerBatch), 1);
/*      */             }
/*      */             else {
/*      */               
/* 1584 */               batchedStatement = locallyScopedConn.prepareStatement(generateMultiStatementForBatch(numValuesPerBatch));
/*      */             } 
/*      */ 
/*      */             
/* 1588 */             if (timeoutTask != null) {
/* 1589 */               timeoutTask.toCancel = (StatementImpl)batchedStatement;
/*      */             }
/*      */             
/* 1592 */             batchedParamIndex = 1;
/*      */             
/* 1594 */             while (batchCounter < numBatchedArgs) {
/* 1595 */               batchedParamIndex = setOneBatchedParameterSet(batchedStatement, batchedParamIndex, this.batchedArgs.get(batchCounter++));
/*      */             }
/*      */ 
/*      */ 
/*      */             
/*      */             try {
/* 1601 */               batchedStatement.execute();
/* 1602 */             } catch (SQLException ex) {
/* 1603 */               sqlEx = handleExceptionForBatch(batchCounter - 1, numValuesPerBatch, updateCounts, ex);
/*      */             } 
/*      */ 
/*      */             
/* 1607 */             updateCountCounter = processMultiCountsAndKeys((StatementImpl)batchedStatement, updateCountCounter, updateCounts);
/*      */ 
/*      */ 
/*      */             
/* 1611 */             batchedStatement.clearParameters();
/*      */           } 
/*      */           
/* 1614 */           if (timeoutTask != null) {
/* 1615 */             if (timeoutTask.caughtWhileCancelling != null) {
/* 1616 */               throw timeoutTask.caughtWhileCancelling;
/*      */             }
/*      */             
/* 1619 */             timeoutTask.cancel();
/* 1620 */             timeoutTask = null;
/*      */           } 
/*      */           
/* 1623 */           if (sqlEx != null) {
/* 1624 */             throw new BatchUpdateException(sqlEx.getMessage(), sqlEx.getSQLState(), sqlEx.getErrorCode(), updateCounts);
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 1629 */           return updateCounts;
/*      */         } finally {
/* 1631 */           if (batchedStatement != null) {
/* 1632 */             batchedStatement.close();
/*      */           }
/*      */         } 
/*      */       } finally {
/* 1636 */         if (timeoutTask != null) {
/* 1637 */           timeoutTask.cancel();
/*      */         }
/*      */         
/* 1640 */         resetCancelledState();
/*      */         
/* 1642 */         if (!multiQueriesEnabled) {
/* 1643 */           locallyScopedConn.getIO().disableMultiQueries();
/*      */         }
/*      */         
/* 1646 */         clearBatch();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private String generateMultiStatementForBatch(int numBatches) {
/* 1652 */     StringBuffer newStatementSql = new StringBuffer((this.originalSql.length() + 1) * numBatches);
/*      */ 
/*      */     
/* 1655 */     newStatementSql.append(this.originalSql);
/*      */     
/* 1657 */     for (int i = 0; i < numBatches - 1; i++) {
/* 1658 */       newStatementSql.append(';');
/* 1659 */       newStatementSql.append(this.originalSql);
/*      */     } 
/*      */     
/* 1662 */     return newStatementSql.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int[] executeBatchedInserts(int batchTimeout) throws SQLException {
/* 1675 */     String valuesClause = getValuesClause();
/*      */     
/* 1677 */     Connection locallyScopedConn = this.connection;
/*      */     
/* 1679 */     if (valuesClause == null) {
/* 1680 */       return executeBatchSerially(batchTimeout);
/*      */     }
/*      */     
/* 1683 */     int numBatchedArgs = this.batchedArgs.size();
/*      */     
/* 1685 */     if (this.retrieveGeneratedKeys) {
/* 1686 */       this.batchedGeneratedKeys = new ArrayList(numBatchedArgs);
/*      */     }
/*      */     
/* 1689 */     int numValuesPerBatch = computeBatchSize(numBatchedArgs);
/*      */     
/* 1691 */     if (numBatchedArgs < numValuesPerBatch) {
/* 1692 */       numValuesPerBatch = numBatchedArgs;
/*      */     }
/*      */     
/* 1695 */     PreparedStatement batchedStatement = null;
/*      */     
/* 1697 */     int batchedParamIndex = 1;
/* 1698 */     int updateCountRunningTotal = 0;
/* 1699 */     int numberToExecuteAsMultiValue = 0;
/* 1700 */     int batchCounter = 0;
/* 1701 */     StatementImpl.CancelTask timeoutTask = null;
/* 1702 */     SQLException sqlEx = null;
/*      */     
/* 1704 */     int[] updateCounts = new int[numBatchedArgs];
/*      */     
/* 1706 */     for (int i = 0; i < this.batchedArgs.size(); i++) {
/* 1707 */       updateCounts[i] = 1;
/*      */     }
/*      */     
/*      */     try {
/*      */       try {
/* 1712 */         batchedStatement = prepareBatchedInsertSQL((ConnectionImpl)locallyScopedConn, numValuesPerBatch);
/*      */ 
/*      */         
/* 1715 */         if (this.connection.getEnableQueryTimeouts() && batchTimeout != 0 && this.connection.versionMeetsMinimum(5, 0, 0)) {
/*      */ 
/*      */           
/* 1718 */           timeoutTask = new StatementImpl.CancelTask(this, (StatementImpl)batchedStatement);
/*      */           
/* 1720 */           ConnectionImpl.getCancelTimer().schedule(timeoutTask, batchTimeout);
/*      */         } 
/*      */ 
/*      */         
/* 1724 */         if (numBatchedArgs < numValuesPerBatch) {
/* 1725 */           numberToExecuteAsMultiValue = numBatchedArgs;
/*      */         } else {
/* 1727 */           numberToExecuteAsMultiValue = numBatchedArgs / numValuesPerBatch;
/*      */         } 
/*      */ 
/*      */         
/* 1731 */         int numberArgsToExecute = numberToExecuteAsMultiValue * numValuesPerBatch;
/*      */ 
/*      */         
/* 1734 */         for (int j = 0; j < numberArgsToExecute; j++) {
/* 1735 */           if (j != 0 && j % numValuesPerBatch == 0) {
/*      */             try {
/* 1737 */               updateCountRunningTotal += batchedStatement.executeUpdate();
/*      */             }
/* 1739 */             catch (SQLException ex) {
/* 1740 */               sqlEx = handleExceptionForBatch(batchCounter - 1, numValuesPerBatch, updateCounts, ex);
/*      */             } 
/*      */ 
/*      */             
/* 1744 */             getBatchedGeneratedKeys(batchedStatement);
/* 1745 */             batchedStatement.clearParameters();
/* 1746 */             batchedParamIndex = 1;
/*      */           } 
/*      */ 
/*      */           
/* 1750 */           batchedParamIndex = setOneBatchedParameterSet(batchedStatement, batchedParamIndex, this.batchedArgs.get(batchCounter++));
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*      */         try {
/* 1756 */           updateCountRunningTotal += batchedStatement.executeUpdate();
/* 1757 */         } catch (SQLException ex) {
/* 1758 */           sqlEx = handleExceptionForBatch(batchCounter - 1, numValuesPerBatch, updateCounts, ex);
/*      */         } 
/*      */ 
/*      */         
/* 1762 */         getBatchedGeneratedKeys(batchedStatement);
/*      */         
/* 1764 */         numValuesPerBatch = numBatchedArgs - batchCounter;
/*      */       } finally {
/* 1766 */         if (batchedStatement != null) {
/* 1767 */           batchedStatement.close();
/*      */         }
/*      */       } 
/*      */       
/*      */       try {
/* 1772 */         if (numValuesPerBatch > 0) {
/* 1773 */           batchedStatement = prepareBatchedInsertSQL((ConnectionImpl)locallyScopedConn, numValuesPerBatch);
/*      */ 
/*      */ 
/*      */           
/* 1777 */           if (timeoutTask != null) {
/* 1778 */             timeoutTask.toCancel = (StatementImpl)batchedStatement;
/*      */           }
/*      */           
/* 1781 */           batchedParamIndex = 1;
/*      */           
/* 1783 */           while (batchCounter < numBatchedArgs) {
/* 1784 */             batchedParamIndex = setOneBatchedParameterSet(batchedStatement, batchedParamIndex, this.batchedArgs.get(batchCounter++));
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*      */           try {
/* 1790 */             updateCountRunningTotal += batchedStatement.executeUpdate();
/* 1791 */           } catch (SQLException ex) {
/* 1792 */             sqlEx = handleExceptionForBatch(batchCounter - 1, numValuesPerBatch, updateCounts, ex);
/*      */           } 
/*      */ 
/*      */           
/* 1796 */           getBatchedGeneratedKeys(batchedStatement);
/*      */         } 
/*      */         
/* 1799 */         if (sqlEx != null) {
/* 1800 */           throw new BatchUpdateException(sqlEx.getMessage(), sqlEx.getSQLState(), sqlEx.getErrorCode(), updateCounts);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 1805 */         return updateCounts;
/*      */       } finally {
/* 1807 */         if (batchedStatement != null) {
/* 1808 */           batchedStatement.close();
/*      */         }
/*      */       } 
/*      */     } finally {
/* 1812 */       if (timeoutTask != null) {
/* 1813 */         timeoutTask.cancel();
/*      */       }
/*      */       
/* 1816 */       resetCancelledState();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected String getValuesClause() throws SQLException {
/* 1821 */     return this.parseInfo.valuesClause;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int computeBatchSize(int numBatchedArgs) throws SQLException {
/* 1833 */     long[] combinedValues = computeMaxParameterSetSizeAndBatchSize(numBatchedArgs);
/*      */     
/* 1835 */     long maxSizeOfParameterSet = combinedValues[0];
/* 1836 */     long sizeOfEntireBatch = combinedValues[1];
/*      */     
/* 1838 */     int maxAllowedPacket = this.connection.getMaxAllowedPacket();
/*      */     
/* 1840 */     if (sizeOfEntireBatch < (maxAllowedPacket - this.originalSql.length())) {
/* 1841 */       return numBatchedArgs;
/*      */     }
/*      */     
/* 1844 */     return (int)Math.max(1L, (maxAllowedPacket - this.originalSql.length()) / maxSizeOfParameterSet);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected long[] computeMaxParameterSetSizeAndBatchSize(int numBatchedArgs) throws SQLException {
/* 1853 */     long sizeOfEntireBatch = 0L;
/* 1854 */     long maxSizeOfParameterSet = 0L;
/*      */     
/* 1856 */     for (int i = 0; i < numBatchedArgs; i++) {
/* 1857 */       BatchParams paramArg = this.batchedArgs.get(i);
/*      */ 
/*      */       
/* 1860 */       boolean[] isNullBatch = paramArg.isNull;
/* 1861 */       boolean[] isStreamBatch = paramArg.isStream;
/*      */       
/* 1863 */       long sizeOfParameterSet = 0L;
/*      */       
/* 1865 */       for (int j = 0; j < isNullBatch.length; j++) {
/* 1866 */         if (!isNullBatch[j]) {
/*      */           
/* 1868 */           if (isStreamBatch[j]) {
/* 1869 */             int streamLength = paramArg.streamLengths[j];
/*      */             
/* 1871 */             if (streamLength != -1) {
/* 1872 */               sizeOfParameterSet += (streamLength * 2);
/*      */             } else {
/* 1874 */               int paramLength = (paramArg.parameterStrings[j]).length;
/* 1875 */               sizeOfParameterSet += paramLength;
/*      */             } 
/*      */           } else {
/* 1878 */             sizeOfParameterSet += (paramArg.parameterStrings[j]).length;
/*      */           } 
/*      */         } else {
/* 1881 */           sizeOfParameterSet += 4L;
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
/* 1893 */       if (getValuesClause() != null) {
/* 1894 */         sizeOfParameterSet += (getValuesClause().length() + 1);
/*      */       } else {
/* 1896 */         sizeOfParameterSet += (this.originalSql.length() + 1);
/*      */       } 
/*      */       
/* 1899 */       sizeOfEntireBatch += sizeOfParameterSet;
/*      */       
/* 1901 */       if (sizeOfParameterSet > maxSizeOfParameterSet) {
/* 1902 */         maxSizeOfParameterSet = sizeOfParameterSet;
/*      */       }
/*      */     } 
/*      */     
/* 1906 */     return new long[] { maxSizeOfParameterSet, sizeOfEntireBatch };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int[] executeBatchSerially(int batchTimeout) throws SQLException {
/* 1919 */     Connection locallyScopedConn = this.connection;
/*      */     
/* 1921 */     if (locallyScopedConn == null) {
/* 1922 */       checkClosed();
/*      */     }
/*      */     
/* 1925 */     int[] updateCounts = null;
/*      */     
/* 1927 */     if (this.batchedArgs != null) {
/* 1928 */       int nbrCommands = this.batchedArgs.size();
/* 1929 */       updateCounts = new int[nbrCommands];
/*      */       
/* 1931 */       for (int i = 0; i < nbrCommands; i++) {
/* 1932 */         updateCounts[i] = -3;
/*      */       }
/*      */       
/* 1935 */       SQLException sqlEx = null;
/*      */       
/* 1937 */       StatementImpl.CancelTask timeoutTask = null;
/*      */       
/*      */       try {
/* 1940 */         if (this.connection.getEnableQueryTimeouts() && batchTimeout != 0 && this.connection.versionMeetsMinimum(5, 0, 0)) {
/*      */ 
/*      */           
/* 1943 */           timeoutTask = new StatementImpl.CancelTask(this, this);
/* 1944 */           ConnectionImpl.getCancelTimer().schedule(timeoutTask, batchTimeout);
/*      */         } 
/*      */ 
/*      */         
/* 1948 */         if (this.retrieveGeneratedKeys) {
/* 1949 */           this.batchedGeneratedKeys = new ArrayList(nbrCommands);
/*      */         }
/*      */         
/* 1952 */         for (this.batchCommandIndex = 0; this.batchCommandIndex < nbrCommands; this.batchCommandIndex++) {
/* 1953 */           Object arg = this.batchedArgs.get(this.batchCommandIndex);
/*      */           
/* 1955 */           if (arg instanceof String) {
/* 1956 */             updateCounts[this.batchCommandIndex] = executeUpdate((String)arg);
/*      */           } else {
/* 1958 */             BatchParams paramArg = (BatchParams)arg;
/*      */             
/*      */             try {
/* 1961 */               updateCounts[this.batchCommandIndex] = executeUpdate(paramArg.parameterStrings, paramArg.parameterStreams, paramArg.isStream, paramArg.streamLengths, paramArg.isNull, true);
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 1966 */               if (this.retrieveGeneratedKeys) {
/* 1967 */                 ResultSet rs = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             }
/* 1985 */             catch (SQLException ex) {
/* 1986 */               updateCounts[this.batchCommandIndex] = -3;
/*      */               
/* 1988 */               if (this.continueBatchOnError && !(ex instanceof MySQLTimeoutException) && !(ex instanceof MySQLStatementCancelledException) && !hasDeadlockOrTimeoutRolledBackTx(ex)) {
/*      */ 
/*      */ 
/*      */                 
/* 1992 */                 sqlEx = ex;
/*      */               } else {
/* 1994 */                 int[] newUpdateCounts = new int[this.batchCommandIndex];
/* 1995 */                 System.arraycopy(updateCounts, 0, newUpdateCounts, 0, this.batchCommandIndex);
/*      */ 
/*      */                 
/* 1998 */                 throw new BatchUpdateException(ex.getMessage(), ex.getSQLState(), ex.getErrorCode(), newUpdateCounts);
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 2006 */         if (sqlEx != null) {
/* 2007 */           throw new BatchUpdateException(sqlEx.getMessage(), sqlEx.getSQLState(), sqlEx.getErrorCode(), updateCounts);
/*      */         }
/*      */       } finally {
/*      */         
/* 2011 */         this.batchCommandIndex = -1;
/*      */         
/* 2013 */         if (timeoutTask != null) {
/* 2014 */           timeoutTask.cancel();
/*      */         }
/*      */         
/* 2017 */         resetCancelledState();
/*      */       } 
/*      */     } 
/*      */     
/* 2021 */     return (updateCounts != null) ? updateCounts : new int[0];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ResultSetInternalMethods executeInternal(int maxRowsToRetrieve, Buffer sendPacket, boolean createStreamingResultSet, boolean queryIsSelectOnly, Field[] metadataFromCache, boolean isBatch) throws SQLException {
/*      */     try {
/*      */       ResultSetInternalMethods rs;
/* 2052 */       resetCancelledState();
/*      */       
/* 2054 */       ConnectionImpl locallyScopedConnection = this.connection;
/*      */       
/* 2056 */       this.numberOfExecutions++;
/*      */       
/* 2058 */       if (this.doPingInstead) {
/* 2059 */         doPingInstead();
/*      */         
/* 2061 */         return this.results;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2066 */       StatementImpl.CancelTask timeoutTask = null;
/*      */       
/*      */       try {
/* 2069 */         if (locallyScopedConnection.getEnableQueryTimeouts() && this.timeoutInMillis != 0 && locallyScopedConnection.versionMeetsMinimum(5, 0, 0)) {
/*      */ 
/*      */           
/* 2072 */           timeoutTask = new StatementImpl.CancelTask(this, this);
/* 2073 */           ConnectionImpl.getCancelTimer().schedule(timeoutTask, this.timeoutInMillis);
/*      */         } 
/*      */ 
/*      */         
/* 2077 */         rs = locallyScopedConnection.execSQL(this, (String)null, maxRowsToRetrieve, sendPacket, this.resultSetType, this.resultSetConcurrency, createStreamingResultSet, this.currentCatalog, metadataFromCache, isBatch);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2082 */         if (timeoutTask != null) {
/* 2083 */           timeoutTask.cancel();
/*      */           
/* 2085 */           if (timeoutTask.caughtWhileCancelling != null) {
/* 2086 */             throw timeoutTask.caughtWhileCancelling;
/*      */           }
/*      */           
/* 2089 */           timeoutTask = null;
/*      */         } 
/*      */         
/* 2092 */         synchronized (this.cancelTimeoutMutex) {
/* 2093 */           if (this.wasCancelled) {
/* 2094 */             MySQLStatementCancelledException mySQLStatementCancelledException; SQLException cause = null;
/*      */             
/* 2096 */             if (this.wasCancelledByTimeout) {
/* 2097 */               MySQLTimeoutException mySQLTimeoutException = new MySQLTimeoutException();
/*      */             } else {
/* 2099 */               mySQLStatementCancelledException = new MySQLStatementCancelledException();
/*      */             } 
/*      */             
/* 2102 */             resetCancelledState();
/*      */             
/* 2104 */             throw mySQLStatementCancelledException;
/*      */           } 
/*      */         } 
/*      */       } finally {
/* 2108 */         if (timeoutTask != null) {
/* 2109 */           timeoutTask.cancel();
/*      */         }
/*      */       } 
/*      */       
/* 2113 */       return rs;
/* 2114 */     } catch (NullPointerException npe) {
/* 2115 */       checkClosed();
/*      */ 
/*      */ 
/*      */       
/* 2119 */       throw npe;
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
/*      */   public ResultSet executeQuery() throws SQLException {
/* 2133 */     checkClosed();
/*      */     
/* 2135 */     ConnectionImpl locallyScopedConn = this.connection;
/*      */     
/* 2137 */     checkForDml(this.originalSql, this.firstCharOfStmt);
/*      */     
/* 2139 */     CachedResultSetMetaData cachedMetadata = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2145 */     synchronized (locallyScopedConn.getMutex()) {
/* 2146 */       clearWarnings();
/*      */       
/* 2148 */       boolean doStreaming = createStreamingResultSet();
/*      */       
/* 2150 */       this.batchedGeneratedKeys = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2160 */       if (doStreaming && this.connection.getNetTimeoutForStreamingResults() > 0)
/*      */       {
/* 2162 */         locallyScopedConn.execSQL(this, "SET net_write_timeout=" + this.connection.getNetTimeoutForStreamingResults(), -1, (Buffer)null, 1003, 1007, false, this.currentCatalog, (Field[])null, false);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2169 */       Buffer sendPacket = fillSendPacket();
/*      */       
/* 2171 */       if (this.results != null && 
/* 2172 */         !this.connection.getHoldResultsOpenOverStatementClose() && 
/* 2173 */         !this.holdResultsOpenOverClose) {
/* 2174 */         this.results.realClose(false);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 2179 */       String oldCatalog = null;
/*      */       
/* 2181 */       if (!locallyScopedConn.getCatalog().equals(this.currentCatalog)) {
/* 2182 */         oldCatalog = locallyScopedConn.getCatalog();
/* 2183 */         locallyScopedConn.setCatalog(this.currentCatalog);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2189 */       if (locallyScopedConn.getCacheResultSetMetadata()) {
/* 2190 */         cachedMetadata = locallyScopedConn.getCachedMetaData(this.originalSql);
/*      */       }
/*      */       
/* 2193 */       Field[] metadataFromCache = null;
/*      */       
/* 2195 */       if (cachedMetadata != null) {
/* 2196 */         metadataFromCache = cachedMetadata.fields;
/*      */       }
/*      */       
/* 2199 */       if (locallyScopedConn.useMaxRows()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2206 */         if (this.hasLimitClause) {
/* 2207 */           this.results = executeInternal(this.maxRows, sendPacket, createStreamingResultSet(), true, metadataFromCache, false);
/*      */         }
/*      */         else {
/*      */           
/* 2211 */           if (this.maxRows <= 0) {
/* 2212 */             executeSimpleNonQuery(locallyScopedConn, "SET OPTION SQL_SELECT_LIMIT=DEFAULT");
/*      */           } else {
/*      */             
/* 2215 */             executeSimpleNonQuery(locallyScopedConn, "SET OPTION SQL_SELECT_LIMIT=" + this.maxRows);
/*      */           } 
/*      */ 
/*      */           
/* 2219 */           this.results = executeInternal(-1, sendPacket, doStreaming, true, metadataFromCache, false);
/*      */ 
/*      */ 
/*      */           
/* 2223 */           if (oldCatalog != null) {
/* 2224 */             this.connection.setCatalog(oldCatalog);
/*      */           }
/*      */         } 
/*      */       } else {
/* 2228 */         this.results = executeInternal(-1, sendPacket, doStreaming, true, metadataFromCache, false);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2233 */       if (oldCatalog != null) {
/* 2234 */         locallyScopedConn.setCatalog(oldCatalog);
/*      */       }
/*      */       
/* 2237 */       if (cachedMetadata != null) {
/* 2238 */         locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, cachedMetadata, this.results);
/*      */       
/*      */       }
/* 2241 */       else if (locallyScopedConn.getCacheResultSetMetadata()) {
/* 2242 */         locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, (CachedResultSetMetaData)null, this.results);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2248 */     this.lastInsertId = this.results.getUpdateID();
/*      */     
/* 2250 */     return this.results;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int executeUpdate() throws SQLException {
/* 2265 */     return executeUpdate(true, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int executeUpdate(boolean clearBatchedGeneratedKeysAndWarnings, boolean isBatch) throws SQLException {
/* 2275 */     if (clearBatchedGeneratedKeysAndWarnings) {
/* 2276 */       clearWarnings();
/* 2277 */       this.batchedGeneratedKeys = null;
/*      */     } 
/*      */     
/* 2280 */     return executeUpdate(this.parameterValues, this.parameterStreams, this.isStream, this.streamLengths, this.isNull, isBatch);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int executeUpdate(byte[][] batchedParameterStrings, InputStream[] batchedParameterStreams, boolean[] batchedIsStream, int[] batchedStreamLengths, boolean[] batchedIsNull, boolean isReallyBatch) throws SQLException {
/* 2308 */     checkClosed();
/*      */     
/* 2310 */     ConnectionImpl locallyScopedConn = this.connection;
/*      */     
/* 2312 */     if (locallyScopedConn.isReadOnly()) {
/* 2313 */       throw SQLError.createSQLException(Messages.getString("PreparedStatement.34") + Messages.getString("PreparedStatement.35"), "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 2318 */     if (this.firstCharOfStmt == 'S' && isSelectQuery())
/*      */     {
/* 2320 */       throw SQLError.createSQLException(Messages.getString("PreparedStatement.37"), "01S03", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */     
/* 2324 */     if (this.results != null && 
/* 2325 */       !locallyScopedConn.getHoldResultsOpenOverStatementClose()) {
/* 2326 */       this.results.realClose(false);
/*      */     }
/*      */ 
/*      */     
/* 2330 */     ResultSetInternalMethods rs = null;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2335 */     synchronized (locallyScopedConn.getMutex()) {
/* 2336 */       Buffer sendPacket = fillSendPacket(batchedParameterStrings, batchedParameterStreams, batchedIsStream, batchedStreamLengths);
/*      */ 
/*      */ 
/*      */       
/* 2340 */       String oldCatalog = null;
/*      */       
/* 2342 */       if (!locallyScopedConn.getCatalog().equals(this.currentCatalog)) {
/* 2343 */         oldCatalog = locallyScopedConn.getCatalog();
/* 2344 */         locallyScopedConn.setCatalog(this.currentCatalog);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2350 */       if (locallyScopedConn.useMaxRows()) {
/* 2351 */         executeSimpleNonQuery(locallyScopedConn, "SET OPTION SQL_SELECT_LIMIT=DEFAULT");
/*      */       }
/*      */ 
/*      */       
/* 2355 */       boolean oldInfoMsgState = false;
/*      */       
/* 2357 */       if (this.retrieveGeneratedKeys) {
/* 2358 */         oldInfoMsgState = locallyScopedConn.isReadInfoMsgEnabled();
/* 2359 */         locallyScopedConn.setReadInfoMsgEnabled(true);
/*      */       } 
/*      */       
/* 2362 */       rs = executeInternal(-1, sendPacket, false, false, (Field[])null, isReallyBatch);
/*      */ 
/*      */       
/* 2365 */       if (this.retrieveGeneratedKeys) {
/* 2366 */         locallyScopedConn.setReadInfoMsgEnabled(oldInfoMsgState);
/* 2367 */         rs.setFirstCharOfQuery(this.firstCharOfStmt);
/*      */       } 
/*      */       
/* 2370 */       if (oldCatalog != null) {
/* 2371 */         locallyScopedConn.setCatalog(oldCatalog);
/*      */       }
/*      */     } 
/*      */     
/* 2375 */     this.results = rs;
/*      */     
/* 2377 */     this.updateCount = rs.getUpdateCount();
/*      */     
/* 2379 */     if (containsOnDuplicateKeyUpdateInSQL() && this.compensateForOnDuplicateKeyUpdate)
/*      */     {
/* 2381 */       if (this.updateCount == 2L || this.updateCount == 0L) {
/* 2382 */         this.updateCount = 1L;
/*      */       }
/*      */     }
/*      */     
/* 2386 */     int truncatedUpdateCount = 0;
/*      */     
/* 2388 */     if (this.updateCount > 2147483647L) {
/* 2389 */       truncatedUpdateCount = Integer.MAX_VALUE;
/*      */     } else {
/* 2391 */       truncatedUpdateCount = (int)this.updateCount;
/*      */     } 
/*      */     
/* 2394 */     this.lastInsertId = rs.getUpdateID();
/*      */     
/* 2396 */     return truncatedUpdateCount;
/*      */   }
/*      */   
/*      */   protected boolean containsOnDuplicateKeyUpdateInSQL() {
/* 2400 */     return this.parseInfo.isOnDuplicateKeyUpdate;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Buffer fillSendPacket() throws SQLException {
/* 2415 */     return fillSendPacket(this.parameterValues, this.parameterStreams, this.isStream, this.streamLengths);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Buffer fillSendPacket(byte[][] batchedParameterStrings, InputStream[] batchedParameterStreams, boolean[] batchedIsStream, int[] batchedStreamLengths) throws SQLException {
/* 2439 */     Buffer sendPacket = this.connection.getIO().getSharedSendPacket();
/*      */     
/* 2441 */     sendPacket.clear();
/*      */     
/* 2443 */     sendPacket.writeByte((byte)3);
/*      */     
/* 2445 */     boolean useStreamLengths = this.connection.getUseStreamLengthsInPrepStmts();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2452 */     int ensurePacketSize = 0;
/*      */     
/* 2454 */     String statementComment = this.connection.getStatementComment();
/*      */     
/* 2456 */     byte[] commentAsBytes = null;
/*      */     
/* 2458 */     if (statementComment != null) {
/* 2459 */       if (this.charConverter != null) {
/* 2460 */         commentAsBytes = this.charConverter.toBytes(statementComment);
/*      */       } else {
/* 2462 */         commentAsBytes = StringUtils.getBytes(statementComment, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2468 */       ensurePacketSize += commentAsBytes.length;
/* 2469 */       ensurePacketSize += 6;
/*      */     } 
/*      */     int i;
/* 2472 */     for (i = 0; i < batchedParameterStrings.length; i++) {
/* 2473 */       if (batchedIsStream[i] && useStreamLengths) {
/* 2474 */         ensurePacketSize += batchedStreamLengths[i];
/*      */       }
/*      */     } 
/*      */     
/* 2478 */     if (ensurePacketSize != 0) {
/* 2479 */       sendPacket.ensureCapacity(ensurePacketSize);
/*      */     }
/*      */     
/* 2482 */     if (commentAsBytes != null) {
/* 2483 */       sendPacket.writeBytesNoNull(Constants.SLASH_STAR_SPACE_AS_BYTES);
/* 2484 */       sendPacket.writeBytesNoNull(commentAsBytes);
/* 2485 */       sendPacket.writeBytesNoNull(Constants.SPACE_STAR_SLASH_SPACE_AS_BYTES);
/*      */     } 
/*      */     
/* 2488 */     for (i = 0; i < batchedParameterStrings.length; i++) {
/* 2489 */       checkAllParametersSet(batchedParameterStrings[i], batchedParameterStreams[i], i);
/*      */ 
/*      */       
/* 2492 */       sendPacket.writeBytesNoNull(this.staticSqlStrings[i]);
/*      */       
/* 2494 */       if (batchedIsStream[i]) {
/* 2495 */         streamToBytes(sendPacket, batchedParameterStreams[i], true, batchedStreamLengths[i], useStreamLengths);
/*      */       } else {
/*      */         
/* 2498 */         sendPacket.writeBytesNoNull(batchedParameterStrings[i]);
/*      */       } 
/*      */     } 
/*      */     
/* 2502 */     sendPacket.writeBytesNoNull(this.staticSqlStrings[batchedParameterStrings.length]);
/*      */ 
/*      */     
/* 2505 */     return sendPacket;
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkAllParametersSet(byte[] parameterString, InputStream parameterStream, int columnIndex) throws SQLException {
/* 2510 */     if (parameterString == null && parameterStream == null) {
/*      */       
/* 2512 */       System.out.println(toString());
/* 2513 */       throw SQLError.createSQLException(Messages.getString("PreparedStatement.40") + (columnIndex + 1), "07001", getExceptionInterceptor());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected PreparedStatement prepareBatchedInsertSQL(ConnectionImpl localConn, int numBatches) throws SQLException {
/* 2523 */     PreparedStatement pstmt = new PreparedStatement(localConn, "batch statement, no sql available", this.currentCatalog, this.parseInfo.getParseInfoForBatch(numBatches));
/* 2524 */     pstmt.setRetrieveGeneratedKeys(this.retrieveGeneratedKeys);
/*      */     
/* 2526 */     return pstmt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getBytesRepresentation(int parameterIndex) throws SQLException {
/* 2542 */     if (this.isStream[parameterIndex]) {
/* 2543 */       return streamToBytes(this.parameterStreams[parameterIndex], false, this.streamLengths[parameterIndex], this.connection.getUseStreamLengthsInPrepStmts());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 2548 */     byte[] parameterVal = this.parameterValues[parameterIndex];
/*      */     
/* 2550 */     if (parameterVal == null) {
/* 2551 */       return null;
/*      */     }
/*      */     
/* 2554 */     if (parameterVal[0] == 39 && parameterVal[parameterVal.length - 1] == 39) {
/*      */       
/* 2556 */       byte[] valNoQuotes = new byte[parameterVal.length - 2];
/* 2557 */       System.arraycopy(parameterVal, 1, valNoQuotes, 0, parameterVal.length - 2);
/*      */ 
/*      */       
/* 2560 */       return valNoQuotes;
/*      */     } 
/*      */     
/* 2563 */     return parameterVal;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected byte[] getBytesRepresentationForBatch(int parameterIndex, int commandIndex) throws SQLException {
/* 2575 */     Object batchedArg = this.batchedArgs.get(commandIndex);
/* 2576 */     if (batchedArg instanceof String) {
/*      */       try {
/* 2578 */         return ((String)batchedArg).getBytes(this.charEncoding);
/*      */       }
/* 2580 */       catch (UnsupportedEncodingException uue) {
/* 2581 */         throw new RuntimeException(Messages.getString("PreparedStatement.32") + this.charEncoding + Messages.getString("PreparedStatement.33"));
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2588 */     BatchParams params = (BatchParams)batchedArg;
/* 2589 */     if (params.isStream[parameterIndex]) {
/* 2590 */       return streamToBytes(params.parameterStreams[parameterIndex], false, params.streamLengths[parameterIndex], this.connection.getUseStreamLengthsInPrepStmts());
/*      */     }
/*      */     
/* 2593 */     byte[] parameterVal = params.parameterStrings[parameterIndex];
/* 2594 */     if (parameterVal == null) {
/* 2595 */       return null;
/*      */     }
/* 2597 */     if (parameterVal[0] == 39 && parameterVal[parameterVal.length - 1] == 39) {
/*      */       
/* 2599 */       byte[] valNoQuotes = new byte[parameterVal.length - 2];
/* 2600 */       System.arraycopy(parameterVal, 1, valNoQuotes, 0, parameterVal.length - 2);
/*      */ 
/*      */       
/* 2603 */       return valNoQuotes;
/*      */     } 
/*      */     
/* 2606 */     return parameterVal;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final String getDateTimePattern(String dt, boolean toTime) throws Exception {
/* 2616 */     int dtLength = (dt != null) ? dt.length() : 0;
/*      */     
/* 2618 */     if (dtLength >= 8 && dtLength <= 10) {
/* 2619 */       int dashCount = 0;
/* 2620 */       boolean isDateOnly = true;
/*      */       
/* 2622 */       for (int k = 0; k < dtLength; k++) {
/* 2623 */         char c = dt.charAt(k);
/*      */         
/* 2625 */         if (!Character.isDigit(c) && c != '-') {
/* 2626 */           isDateOnly = false;
/*      */           
/*      */           break;
/*      */         } 
/*      */         
/* 2631 */         if (c == '-') {
/* 2632 */           dashCount++;
/*      */         }
/*      */       } 
/*      */       
/* 2636 */       if (isDateOnly && dashCount == 2) {
/* 2637 */         return "yyyy-MM-dd";
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2644 */     boolean colonsOnly = true;
/*      */     
/* 2646 */     for (int i = 0; i < dtLength; i++) {
/* 2647 */       char c = dt.charAt(i);
/*      */       
/* 2649 */       if (!Character.isDigit(c) && c != ':') {
/* 2650 */         colonsOnly = false;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*      */     
/* 2656 */     if (colonsOnly) {
/* 2657 */       return "HH:mm:ss";
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2666 */     StringReader reader = new StringReader(dt + " ");
/* 2667 */     ArrayList vec = new ArrayList();
/* 2668 */     ArrayList vecRemovelist = new ArrayList();
/* 2669 */     Object[] nv = new Object[3];
/*      */     
/* 2671 */     nv[0] = Constants.characterValueOf('y');
/* 2672 */     nv[1] = new StringBuffer();
/* 2673 */     nv[2] = Constants.integerValueOf(0);
/* 2674 */     vec.add(nv);
/*      */     
/* 2676 */     if (toTime) {
/* 2677 */       nv = new Object[3];
/* 2678 */       nv[0] = Constants.characterValueOf('h');
/* 2679 */       nv[1] = new StringBuffer();
/* 2680 */       nv[2] = Constants.integerValueOf(0);
/* 2681 */       vec.add(nv);
/*      */     } 
/*      */     int z;
/* 2684 */     while ((z = reader.read()) != -1) {
/* 2685 */       char separator = (char)z;
/* 2686 */       int maxvecs = vec.size();
/*      */       
/* 2688 */       for (int count = 0; count < maxvecs; count++) {
/* 2689 */         Object[] arrayOfObject = vec.get(count);
/* 2690 */         int n = ((Integer)arrayOfObject[2]).intValue();
/* 2691 */         char c = getSuccessor(((Character)arrayOfObject[0]).charValue(), n);
/*      */         
/* 2693 */         if (!Character.isLetterOrDigit(separator)) {
/* 2694 */           if (c == ((Character)arrayOfObject[0]).charValue() && c != 'S') {
/* 2695 */             vecRemovelist.add(arrayOfObject);
/*      */           } else {
/* 2697 */             ((StringBuffer)arrayOfObject[1]).append(separator);
/*      */             
/* 2699 */             if (c == 'X' || c == 'Y') {
/* 2700 */               arrayOfObject[2] = Constants.integerValueOf(4);
/*      */             }
/*      */           } 
/*      */         } else {
/* 2704 */           if (c == 'X') {
/* 2705 */             c = 'y';
/* 2706 */             nv = new Object[3];
/* 2707 */             nv[1] = (new StringBuffer(((StringBuffer)arrayOfObject[1]).toString())).append('M');
/*      */             
/* 2709 */             nv[0] = Constants.characterValueOf('M');
/* 2710 */             nv[2] = Constants.integerValueOf(1);
/* 2711 */             vec.add(nv);
/* 2712 */           } else if (c == 'Y') {
/* 2713 */             c = 'M';
/* 2714 */             nv = new Object[3];
/* 2715 */             nv[1] = (new StringBuffer(((StringBuffer)arrayOfObject[1]).toString())).append('d');
/*      */             
/* 2717 */             nv[0] = Constants.characterValueOf('d');
/* 2718 */             nv[2] = Constants.integerValueOf(1);
/* 2719 */             vec.add(nv);
/*      */           } 
/*      */           
/* 2722 */           ((StringBuffer)arrayOfObject[1]).append(c);
/*      */           
/* 2724 */           if (c == ((Character)arrayOfObject[0]).charValue()) {
/* 2725 */             arrayOfObject[2] = Constants.integerValueOf(n + 1);
/*      */           } else {
/* 2727 */             arrayOfObject[0] = Constants.characterValueOf(c);
/* 2728 */             arrayOfObject[2] = Constants.integerValueOf(1);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 2733 */       int k = vecRemovelist.size();
/*      */       
/* 2735 */       for (int m = 0; m < k; m++) {
/* 2736 */         Object[] arrayOfObject = vecRemovelist.get(m);
/* 2737 */         vec.remove(arrayOfObject);
/*      */       } 
/*      */       
/* 2740 */       vecRemovelist.clear();
/*      */     } 
/*      */     
/* 2743 */     int size = vec.size();
/*      */     int j;
/* 2745 */     for (j = 0; j < size; j++) {
/* 2746 */       Object[] arrayOfObject = vec.get(j);
/* 2747 */       char c = ((Character)arrayOfObject[0]).charValue();
/* 2748 */       int n = ((Integer)arrayOfObject[2]).intValue();
/*      */       
/* 2750 */       boolean bk = (getSuccessor(c, n) != c);
/* 2751 */       boolean atEnd = ((c == 's' || c == 'm' || (c == 'h' && toTime)) && bk);
/* 2752 */       boolean finishesAtDate = (bk && c == 'd' && !toTime);
/* 2753 */       boolean containsEnd = (((StringBuffer)arrayOfObject[1]).toString().indexOf('W') != -1);
/*      */ 
/*      */       
/* 2756 */       if ((!atEnd && !finishesAtDate) || containsEnd) {
/* 2757 */         vecRemovelist.add(arrayOfObject);
/*      */       }
/*      */     } 
/*      */     
/* 2761 */     size = vecRemovelist.size();
/*      */     
/* 2763 */     for (j = 0; j < size; j++) {
/* 2764 */       vec.remove(vecRemovelist.get(j));
/*      */     }
/*      */     
/* 2767 */     vecRemovelist.clear();
/* 2768 */     Object[] v = vec.get(0);
/*      */     
/* 2770 */     StringBuffer format = (StringBuffer)v[1];
/* 2771 */     format.setLength(format.length() - 1);
/*      */     
/* 2773 */     return format.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 2799 */     if (!isSelectQuery()) {
/* 2800 */       return null;
/*      */     }
/*      */     
/* 2803 */     PreparedStatement mdStmt = null;
/* 2804 */     ResultSet mdRs = null;
/*      */     
/* 2806 */     if (this.pstmtResultMetaData == null) {
/*      */       try {
/* 2808 */         mdStmt = new PreparedStatement(this.connection, this.originalSql, this.currentCatalog, this.parseInfo);
/*      */ 
/*      */         
/* 2811 */         mdStmt.setMaxRows(0);
/*      */         
/* 2813 */         int paramCount = this.parameterValues.length;
/*      */         
/* 2815 */         for (int i = 1; i <= paramCount; i++) {
/* 2816 */           mdStmt.setString(i, "");
/*      */         }
/*      */         
/* 2819 */         boolean hadResults = mdStmt.execute();
/*      */         
/* 2821 */         if (hadResults) {
/* 2822 */           mdRs = mdStmt.getResultSet();
/*      */           
/* 2824 */           this.pstmtResultMetaData = mdRs.getMetaData();
/*      */         } else {
/* 2826 */           this.pstmtResultMetaData = new ResultSetMetaData(new Field[0], this.connection.getUseOldAliasMetadataBehavior(), getExceptionInterceptor());
/*      */         }
/*      */       
/*      */       } finally {
/*      */         
/* 2831 */         SQLException sqlExRethrow = null;
/*      */         
/* 2833 */         if (mdRs != null) {
/*      */           try {
/* 2835 */             mdRs.close();
/* 2836 */           } catch (SQLException sqlEx) {
/* 2837 */             sqlExRethrow = sqlEx;
/*      */           } 
/*      */           
/* 2840 */           mdRs = null;
/*      */         } 
/*      */         
/* 2843 */         if (mdStmt != null) {
/*      */           try {
/* 2845 */             mdStmt.close();
/* 2846 */           } catch (SQLException sqlEx) {
/* 2847 */             sqlExRethrow = sqlEx;
/*      */           } 
/*      */           
/* 2850 */           mdStmt = null;
/*      */         } 
/*      */         
/* 2853 */         if (sqlExRethrow != null) {
/* 2854 */           throw sqlExRethrow;
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/* 2859 */     return this.pstmtResultMetaData;
/*      */   }
/*      */   
/*      */   protected boolean isSelectQuery() {
/* 2863 */     return StringUtils.startsWithIgnoreCaseAndWs(StringUtils.stripComments(this.originalSql, "'\"", "'\"", true, false, true, true), "SELECT");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ParameterMetaData getParameterMetaData() throws SQLException {
/* 2874 */     if (this.parameterMetaData == null) {
/* 2875 */       if (this.connection.getGenerateSimpleParameterMetadata()) {
/* 2876 */         this.parameterMetaData = new MysqlParameterMetadata(this.parameterCount);
/*      */       } else {
/* 2878 */         this.parameterMetaData = new MysqlParameterMetadata(null, this.parameterCount, getExceptionInterceptor());
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/* 2883 */     return this.parameterMetaData;
/*      */   }
/*      */   
/*      */   ParseInfo getParseInfo() {
/* 2887 */     return this.parseInfo;
/*      */   }
/*      */   
/*      */   private final char getSuccessor(char c, int n) {
/* 2891 */     return (c == 'y' && n == 2) ? 'X' : ((c == 'y' && n < 4) ? 'y' : ((c == 'y') ? 'M' : ((c == 'M' && n == 2) ? 'Y' : ((c == 'M' && n < 3) ? 'M' : ((c == 'M') ? 'd' : ((c == 'd' && n < 2) ? 'd' : ((c == 'd') ? 'H' : ((c == 'H' && n < 2) ? 'H' : ((c == 'H') ? 'm' : ((c == 'm' && n < 2) ? 'm' : ((c == 'm') ? 's' : ((c == 's' && n < 2) ? 's' : 'W'))))))))))));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final void hexEscapeBlock(byte[] buf, Buffer packet, int size) throws SQLException {
/* 2917 */     for (int i = 0; i < size; i++) {
/* 2918 */       byte b = buf[i];
/* 2919 */       int lowBits = (b & 0xFF) / 16;
/* 2920 */       int highBits = (b & 0xFF) % 16;
/*      */       
/* 2922 */       packet.writeByte(HEX_DIGITS[lowBits]);
/* 2923 */       packet.writeByte(HEX_DIGITS[highBits]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void initializeFromParseInfo() throws SQLException {
/* 2928 */     this.staticSqlStrings = this.parseInfo.staticSql;
/* 2929 */     this.hasLimitClause = this.parseInfo.foundLimitClause;
/* 2930 */     this.isLoadDataQuery = this.parseInfo.foundLoadData;
/* 2931 */     this.firstCharOfStmt = this.parseInfo.firstStmtChar;
/*      */     
/* 2933 */     this.parameterCount = this.staticSqlStrings.length - 1;
/*      */     
/* 2935 */     this.parameterValues = new byte[this.parameterCount][];
/* 2936 */     this.parameterStreams = new InputStream[this.parameterCount];
/* 2937 */     this.isStream = new boolean[this.parameterCount];
/* 2938 */     this.streamLengths = new int[this.parameterCount];
/* 2939 */     this.isNull = new boolean[this.parameterCount];
/* 2940 */     this.parameterTypes = new int[this.parameterCount];
/*      */     
/* 2942 */     clearParameters();
/*      */     
/* 2944 */     for (int j = 0; j < this.parameterCount; j++) {
/* 2945 */       this.isStream[j] = false;
/*      */     }
/*      */   }
/*      */   
/*      */   boolean isNull(int paramIndex) {
/* 2950 */     return this.isNull[paramIndex];
/*      */   }
/*      */   
/*      */   private final int readblock(InputStream i, byte[] b) throws SQLException {
/*      */     try {
/* 2955 */       return i.read(b);
/* 2956 */     } catch (Throwable ex) {
/* 2957 */       SQLException sqlEx = SQLError.createSQLException(Messages.getString("PreparedStatement.56") + ex.getClass().getName(), "S1000", getExceptionInterceptor());
/*      */       
/* 2959 */       sqlEx.initCause(ex);
/*      */       
/* 2961 */       throw sqlEx;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private final int readblock(InputStream i, byte[] b, int length) throws SQLException {
/*      */     try {
/* 2968 */       int lengthToRead = length;
/*      */       
/* 2970 */       if (lengthToRead > b.length) {
/* 2971 */         lengthToRead = b.length;
/*      */       }
/*      */       
/* 2974 */       return i.read(b, 0, lengthToRead);
/* 2975 */     } catch (Throwable ex) {
/* 2976 */       SQLException sqlEx = SQLError.createSQLException(Messages.getString("PreparedStatement.56") + ex.getClass().getName(), "S1000", getExceptionInterceptor());
/*      */       
/* 2978 */       sqlEx.initCause(ex);
/*      */       
/* 2980 */       throw sqlEx;
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
/*      */   protected void realClose(boolean calledExplicitly, boolean closeOpenResults) throws SQLException {
/* 2995 */     if (this.useUsageAdvisor && 
/* 2996 */       this.numberOfExecutions <= 1) {
/* 2997 */       String message = Messages.getString("PreparedStatement.43");
/*      */       
/* 2999 */       this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", this.currentCatalog, this.connectionId, getId(), -1, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, message));
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3008 */     super.realClose(calledExplicitly, closeOpenResults);
/*      */     
/* 3010 */     this.dbmd = null;
/* 3011 */     this.originalSql = null;
/* 3012 */     this.staticSqlStrings = (byte[][])null;
/* 3013 */     this.parameterValues = (byte[][])null;
/* 3014 */     this.parameterStreams = null;
/* 3015 */     this.isStream = null;
/* 3016 */     this.streamLengths = null;
/* 3017 */     this.isNull = null;
/* 3018 */     this.streamConvertBuf = null;
/* 3019 */     this.parameterTypes = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setArray(int i, Array x) throws SQLException {
/* 3036 */     throw SQLError.notImplemented();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
/* 3063 */     if (x == null) {
/* 3064 */       setNull(parameterIndex, 12);
/*      */     } else {
/* 3066 */       setBinaryStream(parameterIndex, x, length);
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
/*      */   public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
/* 3084 */     if (x == null) {
/* 3085 */       setNull(parameterIndex, 3);
/*      */     } else {
/* 3087 */       setInternal(parameterIndex, StringUtils.fixDecimalExponent(StringUtils.consistentToString(x)));
/*      */ 
/*      */       
/* 3090 */       this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 3;
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
/*      */   public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
/* 3116 */     if (x == null) {
/* 3117 */       setNull(parameterIndex, -2);
/*      */     } else {
/* 3119 */       int parameterIndexOffset = getParameterIndexOffset();
/*      */       
/* 3121 */       if (parameterIndex < 1 || parameterIndex > this.staticSqlStrings.length)
/*      */       {
/* 3123 */         throw SQLError.createSQLException(Messages.getString("PreparedStatement.2") + parameterIndex + Messages.getString("PreparedStatement.3") + this.staticSqlStrings.length + Messages.getString("PreparedStatement.4"), "S1009", getExceptionInterceptor());
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 3128 */       if (parameterIndexOffset == -1 && parameterIndex == 1) {
/* 3129 */         throw SQLError.createSQLException("Can't set IN parameter for return value of stored function call.", "S1009", getExceptionInterceptor());
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 3134 */       this.parameterStreams[parameterIndex - 1 + parameterIndexOffset] = x;
/* 3135 */       this.isStream[parameterIndex - 1 + parameterIndexOffset] = true;
/* 3136 */       this.streamLengths[parameterIndex - 1 + parameterIndexOffset] = length;
/* 3137 */       this.isNull[parameterIndex - 1 + parameterIndexOffset] = false;
/* 3138 */       this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 2004;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
/* 3144 */     setBinaryStream(parameterIndex, inputStream, (int)length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBlob(int i, Blob x) throws SQLException {
/* 3159 */     if (x == null) {
/* 3160 */       setNull(i, 2004);
/*      */     } else {
/* 3162 */       ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
/*      */       
/* 3164 */       bytesOut.write(39);
/* 3165 */       escapeblockFast(x.getBytes(1L, (int)x.length()), bytesOut, (int)x.length());
/*      */       
/* 3167 */       bytesOut.write(39);
/*      */       
/* 3169 */       setInternal(i, bytesOut.toByteArray());
/*      */       
/* 3171 */       this.parameterTypes[i - 1 + getParameterIndexOffset()] = 2004;
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
/*      */   public void setBoolean(int parameterIndex, boolean x) throws SQLException {
/* 3188 */     if (this.useTrueBoolean) {
/* 3189 */       setInternal(parameterIndex, x ? "1" : "0");
/*      */     } else {
/* 3191 */       setInternal(parameterIndex, x ? "'t'" : "'f'");
/*      */       
/* 3193 */       this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 16;
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
/*      */   public void setByte(int parameterIndex, byte x) throws SQLException {
/* 3210 */     setInternal(parameterIndex, String.valueOf(x));
/*      */     
/* 3212 */     this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = -6;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBytes(int parameterIndex, byte[] x) throws SQLException {
/* 3229 */     setBytes(parameterIndex, x, true, true);
/*      */     
/* 3231 */     if (x != null) {
/* 3232 */       this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = -2;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setBytes(int parameterIndex, byte[] x, boolean checkForIntroducer, boolean escapeForMBChars) throws SQLException {
/* 3239 */     if (x == null) {
/* 3240 */       setNull(parameterIndex, -2);
/*      */     } else {
/* 3242 */       String connectionEncoding = this.connection.getEncoding();
/*      */       
/* 3244 */       if (this.connection.isNoBackslashEscapesSet() || (escapeForMBChars && this.connection.getUseUnicode() && connectionEncoding != null && CharsetMapping.isMultibyteCharset(connectionEncoding))) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 3252 */         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(x.length * 2 + 3);
/*      */         
/* 3254 */         byteArrayOutputStream.write(120);
/* 3255 */         byteArrayOutputStream.write(39);
/*      */         
/* 3257 */         for (int j = 0; j < x.length; j++) {
/* 3258 */           int lowBits = (x[j] & 0xFF) / 16;
/* 3259 */           int highBits = (x[j] & 0xFF) % 16;
/*      */           
/* 3261 */           byteArrayOutputStream.write(HEX_DIGITS[lowBits]);
/* 3262 */           byteArrayOutputStream.write(HEX_DIGITS[highBits]);
/*      */         } 
/*      */         
/* 3265 */         byteArrayOutputStream.write(39);
/*      */         
/* 3267 */         setInternal(parameterIndex, byteArrayOutputStream.toByteArray());
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/* 3273 */       int numBytes = x.length;
/*      */       
/* 3275 */       int pad = 2;
/*      */       
/* 3277 */       boolean needsIntroducer = (checkForIntroducer && this.connection.versionMeetsMinimum(4, 1, 0));
/*      */ 
/*      */       
/* 3280 */       if (needsIntroducer) {
/* 3281 */         pad += 7;
/*      */       }
/*      */       
/* 3284 */       ByteArrayOutputStream bOut = new ByteArrayOutputStream(numBytes + pad);
/*      */ 
/*      */       
/* 3287 */       if (needsIntroducer) {
/* 3288 */         bOut.write(95);
/* 3289 */         bOut.write(98);
/* 3290 */         bOut.write(105);
/* 3291 */         bOut.write(110);
/* 3292 */         bOut.write(97);
/* 3293 */         bOut.write(114);
/* 3294 */         bOut.write(121);
/*      */       } 
/* 3296 */       bOut.write(39);
/*      */       
/* 3298 */       for (int i = 0; i < numBytes; i++) {
/* 3299 */         byte b = x[i];
/*      */         
/* 3301 */         switch (b) {
/*      */           case 0:
/* 3303 */             bOut.write(92);
/* 3304 */             bOut.write(48);
/*      */             break;
/*      */ 
/*      */           
/*      */           case 10:
/* 3309 */             bOut.write(92);
/* 3310 */             bOut.write(110);
/*      */             break;
/*      */ 
/*      */           
/*      */           case 13:
/* 3315 */             bOut.write(92);
/* 3316 */             bOut.write(114);
/*      */             break;
/*      */ 
/*      */           
/*      */           case 92:
/* 3321 */             bOut.write(92);
/* 3322 */             bOut.write(92);
/*      */             break;
/*      */ 
/*      */           
/*      */           case 39:
/* 3327 */             bOut.write(92);
/* 3328 */             bOut.write(39);
/*      */             break;
/*      */ 
/*      */           
/*      */           case 34:
/* 3333 */             bOut.write(92);
/* 3334 */             bOut.write(34);
/*      */             break;
/*      */ 
/*      */           
/*      */           case 26:
/* 3339 */             bOut.write(92);
/* 3340 */             bOut.write(90);
/*      */             break;
/*      */ 
/*      */           
/*      */           default:
/* 3345 */             bOut.write(b);
/*      */             break;
/*      */         } 
/*      */       } 
/* 3349 */       bOut.write(39);
/*      */       
/* 3351 */       setInternal(parameterIndex, bOut.toByteArray());
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
/*      */   protected void setBytesNoEscape(int parameterIndex, byte[] parameterAsBytes) throws SQLException {
/* 3369 */     byte[] parameterWithQuotes = new byte[parameterAsBytes.length + 2];
/* 3370 */     parameterWithQuotes[0] = 39;
/* 3371 */     System.arraycopy(parameterAsBytes, 0, parameterWithQuotes, 1, parameterAsBytes.length);
/*      */     
/* 3373 */     parameterWithQuotes[parameterAsBytes.length + 1] = 39;
/*      */     
/* 3375 */     setInternal(parameterIndex, parameterWithQuotes);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void setBytesNoEscapeNoQuotes(int parameterIndex, byte[] parameterAsBytes) throws SQLException {
/* 3380 */     setInternal(parameterIndex, parameterAsBytes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
/*      */     try {
/* 3408 */       if (reader == null) {
/* 3409 */         setNull(parameterIndex, -1);
/*      */       } else {
/* 3411 */         char[] c = null;
/* 3412 */         int len = 0;
/*      */         
/* 3414 */         boolean useLength = this.connection.getUseStreamLengthsInPrepStmts();
/*      */ 
/*      */         
/* 3417 */         String forcedEncoding = this.connection.getClobCharacterEncoding();
/*      */         
/* 3419 */         if (useLength && length != -1) {
/* 3420 */           c = new char[length];
/*      */           
/* 3422 */           int numCharsRead = readFully(reader, c, length);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3427 */           if (forcedEncoding == null) {
/* 3428 */             setString(parameterIndex, new String(c, 0, numCharsRead));
/*      */           } else {
/*      */             try {
/* 3431 */               setBytes(parameterIndex, (new String(c, 0, numCharsRead)).getBytes(forcedEncoding));
/*      */             
/*      */             }
/* 3434 */             catch (UnsupportedEncodingException uee) {
/* 3435 */               throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, "S1009", getExceptionInterceptor());
/*      */             } 
/*      */           } 
/*      */         } else {
/*      */           
/* 3440 */           c = new char[4096];
/*      */           
/* 3442 */           StringBuffer buf = new StringBuffer();
/*      */           
/* 3444 */           while ((len = reader.read(c)) != -1) {
/* 3445 */             buf.append(c, 0, len);
/*      */           }
/*      */           
/* 3448 */           if (forcedEncoding == null) {
/* 3449 */             setString(parameterIndex, buf.toString());
/*      */           } else {
/*      */             try {
/* 3452 */               setBytes(parameterIndex, buf.toString().getBytes(forcedEncoding));
/*      */             }
/* 3454 */             catch (UnsupportedEncodingException uee) {
/* 3455 */               throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, "S1009", getExceptionInterceptor());
/*      */             } 
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 3461 */         this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 2005;
/*      */       } 
/* 3463 */     } catch (IOException ioEx) {
/* 3464 */       throw SQLError.createSQLException(ioEx.toString(), "S1000", getExceptionInterceptor());
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
/*      */   public void setClob(int i, Clob x) throws SQLException {
/* 3481 */     if (x == null) {
/* 3482 */       setNull(i, 2005);
/*      */     } else {
/*      */       
/* 3485 */       String forcedEncoding = this.connection.getClobCharacterEncoding();
/*      */       
/* 3487 */       if (forcedEncoding == null) {
/* 3488 */         setString(i, x.getSubString(1L, (int)x.length()));
/*      */       } else {
/*      */         try {
/* 3491 */           setBytes(i, x.getSubString(1L, (int)x.length()).getBytes(forcedEncoding));
/*      */         }
/* 3493 */         catch (UnsupportedEncodingException uee) {
/* 3494 */           throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, "S1009", getExceptionInterceptor());
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 3499 */       this.parameterTypes[i - 1 + getParameterIndexOffset()] = 2005;
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
/*      */   public void setDate(int parameterIndex, Date x) throws SQLException {
/* 3517 */     setDate(parameterIndex, x, (Calendar)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
/* 3536 */     if (x == null) {
/* 3537 */       setNull(parameterIndex, 91);
/*      */     } else {
/* 3539 */       checkClosed();
/*      */       
/* 3541 */       if (!this.useLegacyDatetimeCode) {
/* 3542 */         newSetDateInternal(parameterIndex, x, cal);
/*      */       }
/*      */       else {
/*      */         
/* 3546 */         SimpleDateFormat dateFormatter = new SimpleDateFormat("''yyyy-MM-dd''", Locale.US);
/*      */         
/* 3548 */         setInternal(parameterIndex, dateFormatter.format(x));
/*      */         
/* 3550 */         this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 91;
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
/*      */   public void setDouble(int parameterIndex, double x) throws SQLException {
/* 3569 */     if (!this.connection.getAllowNanAndInf() && (x == Double.POSITIVE_INFINITY || x == Double.NEGATIVE_INFINITY || Double.isNaN(x)))
/*      */     {
/*      */       
/* 3572 */       throw SQLError.createSQLException("'" + x + "' is not a valid numeric or approximate numeric value", "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3578 */     setInternal(parameterIndex, StringUtils.fixDecimalExponent(String.valueOf(x)));
/*      */ 
/*      */     
/* 3581 */     this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 8;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFloat(int parameterIndex, float x) throws SQLException {
/* 3597 */     setInternal(parameterIndex, StringUtils.fixDecimalExponent(String.valueOf(x)));
/*      */ 
/*      */     
/* 3600 */     this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 6;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInt(int parameterIndex, int x) throws SQLException {
/* 3616 */     setInternal(parameterIndex, String.valueOf(x));
/*      */     
/* 3618 */     this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 4;
/*      */   }
/*      */ 
/*      */   
/*      */   protected final void setInternal(int paramIndex, byte[] val) throws SQLException {
/* 3623 */     if (this.isClosed) {
/* 3624 */       throw SQLError.createSQLException(Messages.getString("PreparedStatement.48"), "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */     
/* 3628 */     int parameterIndexOffset = getParameterIndexOffset();
/*      */     
/* 3630 */     checkBounds(paramIndex, parameterIndexOffset);
/*      */     
/* 3632 */     this.isStream[paramIndex - 1 + parameterIndexOffset] = false;
/* 3633 */     this.isNull[paramIndex - 1 + parameterIndexOffset] = false;
/* 3634 */     this.parameterStreams[paramIndex - 1 + parameterIndexOffset] = null;
/* 3635 */     this.parameterValues[paramIndex - 1 + parameterIndexOffset] = val;
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkBounds(int paramIndex, int parameterIndexOffset) throws SQLException {
/* 3640 */     if (paramIndex < 1) {
/* 3641 */       throw SQLError.createSQLException(Messages.getString("PreparedStatement.49") + paramIndex + Messages.getString("PreparedStatement.50"), "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */     
/* 3645 */     if (paramIndex > this.parameterCount) {
/* 3646 */       throw SQLError.createSQLException(Messages.getString("PreparedStatement.51") + paramIndex + Messages.getString("PreparedStatement.52") + this.parameterValues.length + Messages.getString("PreparedStatement.53"), "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 3651 */     if (parameterIndexOffset == -1 && paramIndex == 1) {
/* 3652 */       throw SQLError.createSQLException("Can't set IN parameter for return value of stored function call.", "S1009", getExceptionInterceptor());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void setInternal(int paramIndex, String val) throws SQLException {
/* 3659 */     checkClosed();
/*      */     
/* 3661 */     byte[] parameterAsBytes = null;
/*      */     
/* 3663 */     if (this.charConverter != null) {
/* 3664 */       parameterAsBytes = this.charConverter.toBytes(val);
/*      */     } else {
/* 3666 */       parameterAsBytes = StringUtils.getBytes(val, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3672 */     setInternal(paramIndex, parameterAsBytes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLong(int parameterIndex, long x) throws SQLException {
/* 3688 */     setInternal(parameterIndex, String.valueOf(x));
/*      */     
/* 3690 */     this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = -5;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNull(int parameterIndex, int sqlType) throws SQLException {
/* 3710 */     setInternal(parameterIndex, "null");
/* 3711 */     this.isNull[parameterIndex - 1 + getParameterIndexOffset()] = true;
/*      */     
/* 3713 */     this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNull(int parameterIndex, int sqlType, String arg) throws SQLException {
/* 3735 */     setNull(parameterIndex, sqlType);
/*      */     
/* 3737 */     this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   private void setNumericObject(int parameterIndex, Object parameterObj, int targetSqlType, int scale) throws SQLException {
/*      */     Number parameterAsNum;
/* 3743 */     if (parameterObj instanceof Boolean) {
/* 3744 */       parameterAsNum = ((Boolean)parameterObj).booleanValue() ? Constants.integerValueOf(1) : Constants.integerValueOf(0);
/*      */     
/*      */     }
/* 3747 */     else if (parameterObj instanceof String) {
/* 3748 */       boolean parameterAsBoolean; switch (targetSqlType) {
/*      */         case -7:
/* 3750 */           if ("1".equals(parameterObj) || "0".equals(parameterObj)) {
/*      */             
/* 3752 */             Number number = Integer.valueOf((String)parameterObj); break;
/*      */           } 
/* 3754 */           parameterAsBoolean = "true".equalsIgnoreCase((String)parameterObj);
/*      */ 
/*      */           
/* 3757 */           parameterAsNum = parameterAsBoolean ? Constants.integerValueOf(1) : Constants.integerValueOf(0);
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case -6:
/*      */         case 4:
/*      */         case 5:
/* 3766 */           parameterAsNum = Integer.valueOf((String)parameterObj);
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case -5:
/* 3772 */           parameterAsNum = Long.valueOf((String)parameterObj);
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case 7:
/* 3778 */           parameterAsNum = Float.valueOf((String)parameterObj);
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case 6:
/*      */         case 8:
/* 3785 */           parameterAsNum = Double.valueOf((String)parameterObj);
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         default:
/* 3793 */           parameterAsNum = new BigDecimal((String)parameterObj);
/*      */           break;
/*      */       } 
/*      */     } else {
/* 3797 */       parameterAsNum = (Number)parameterObj;
/*      */     } 
/*      */     
/* 3800 */     switch (targetSqlType) {
/*      */       case -7:
/*      */       case -6:
/*      */       case 4:
/*      */       case 5:
/* 3805 */         setInt(parameterIndex, parameterAsNum.intValue());
/*      */         break;
/*      */ 
/*      */       
/*      */       case -5:
/* 3810 */         setLong(parameterIndex, parameterAsNum.longValue());
/*      */         break;
/*      */ 
/*      */       
/*      */       case 7:
/* 3815 */         setFloat(parameterIndex, parameterAsNum.floatValue());
/*      */         break;
/*      */ 
/*      */       
/*      */       case 6:
/*      */       case 8:
/* 3821 */         setDouble(parameterIndex, parameterAsNum.doubleValue());
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 2:
/*      */       case 3:
/* 3828 */         if (parameterAsNum instanceof BigDecimal) {
/* 3829 */           BigDecimal scaledBigDecimal = null;
/*      */           
/*      */           try {
/* 3832 */             scaledBigDecimal = ((BigDecimal)parameterAsNum).setScale(scale);
/*      */           }
/* 3834 */           catch (ArithmeticException ex) {
/*      */             try {
/* 3836 */               scaledBigDecimal = ((BigDecimal)parameterAsNum).setScale(scale, 4);
/*      */             
/*      */             }
/* 3839 */             catch (ArithmeticException arEx) {
/* 3840 */               throw SQLError.createSQLException("Can't set scale of '" + scale + "' for DECIMAL argument '" + parameterAsNum + "'", "S1009", getExceptionInterceptor());
/*      */             } 
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 3849 */           setBigDecimal(parameterIndex, scaledBigDecimal); break;
/* 3850 */         }  if (parameterAsNum instanceof BigInteger) {
/* 3851 */           setBigDecimal(parameterIndex, new BigDecimal((BigInteger)parameterAsNum, scale));
/*      */ 
/*      */           
/*      */           break;
/*      */         } 
/*      */         
/* 3857 */         setBigDecimal(parameterIndex, new BigDecimal(parameterAsNum.doubleValue()));
/*      */         break;
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
/*      */   public void setObject(int parameterIndex, Object parameterObj) throws SQLException {
/* 3879 */     if (parameterObj == null) {
/* 3880 */       setNull(parameterIndex, 1111);
/*      */     }
/* 3882 */     else if (parameterObj instanceof Byte) {
/* 3883 */       setInt(parameterIndex, ((Byte)parameterObj).intValue());
/* 3884 */     } else if (parameterObj instanceof String) {
/* 3885 */       setString(parameterIndex, (String)parameterObj);
/* 3886 */     } else if (parameterObj instanceof BigDecimal) {
/* 3887 */       setBigDecimal(parameterIndex, (BigDecimal)parameterObj);
/* 3888 */     } else if (parameterObj instanceof Short) {
/* 3889 */       setShort(parameterIndex, ((Short)parameterObj).shortValue());
/* 3890 */     } else if (parameterObj instanceof Integer) {
/* 3891 */       setInt(parameterIndex, ((Integer)parameterObj).intValue());
/* 3892 */     } else if (parameterObj instanceof Long) {
/* 3893 */       setLong(parameterIndex, ((Long)parameterObj).longValue());
/* 3894 */     } else if (parameterObj instanceof Float) {
/* 3895 */       setFloat(parameterIndex, ((Float)parameterObj).floatValue());
/* 3896 */     } else if (parameterObj instanceof Double) {
/* 3897 */       setDouble(parameterIndex, ((Double)parameterObj).doubleValue());
/* 3898 */     } else if (parameterObj instanceof byte[]) {
/* 3899 */       setBytes(parameterIndex, (byte[])parameterObj);
/* 3900 */     } else if (parameterObj instanceof Date) {
/* 3901 */       setDate(parameterIndex, (Date)parameterObj);
/* 3902 */     } else if (parameterObj instanceof Time) {
/* 3903 */       setTime(parameterIndex, (Time)parameterObj);
/* 3904 */     } else if (parameterObj instanceof Timestamp) {
/* 3905 */       setTimestamp(parameterIndex, (Timestamp)parameterObj);
/* 3906 */     } else if (parameterObj instanceof Boolean) {
/* 3907 */       setBoolean(parameterIndex, ((Boolean)parameterObj).booleanValue());
/*      */     }
/* 3909 */     else if (parameterObj instanceof InputStream) {
/* 3910 */       setBinaryStream(parameterIndex, (InputStream)parameterObj, -1);
/* 3911 */     } else if (parameterObj instanceof Blob) {
/* 3912 */       setBlob(parameterIndex, (Blob)parameterObj);
/* 3913 */     } else if (parameterObj instanceof Clob) {
/* 3914 */       setClob(parameterIndex, (Clob)parameterObj);
/* 3915 */     } else if (this.connection.getTreatUtilDateAsTimestamp() && parameterObj instanceof Date) {
/*      */       
/* 3917 */       setTimestamp(parameterIndex, new Timestamp(((Date)parameterObj).getTime()));
/*      */     }
/* 3919 */     else if (parameterObj instanceof BigInteger) {
/* 3920 */       setString(parameterIndex, parameterObj.toString());
/*      */     } else {
/* 3922 */       setSerializableObject(parameterIndex, parameterObj);
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
/*      */   public void setObject(int parameterIndex, Object parameterObj, int targetSqlType) throws SQLException {
/* 3943 */     if (!(parameterObj instanceof BigDecimal)) {
/* 3944 */       setObject(parameterIndex, parameterObj, targetSqlType, 0);
/*      */     } else {
/* 3946 */       setObject(parameterIndex, parameterObj, targetSqlType, ((BigDecimal)parameterObj).scale());
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
/*      */   public void setObject(int parameterIndex, Object parameterObj, int targetSqlType, int scale) throws SQLException {
/* 3982 */     if (parameterObj == null) {
/* 3983 */       setNull(parameterIndex, 1111);
/*      */     } else {
/*      */       try {
/* 3986 */         Date parameterAsDate; switch (targetSqlType) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           case 16:
/* 4006 */             if (parameterObj instanceof Boolean) {
/* 4007 */               setBoolean(parameterIndex, ((Boolean)parameterObj).booleanValue());
/*      */             
/*      */             }
/* 4010 */             else if (parameterObj instanceof String) {
/* 4011 */               setBoolean(parameterIndex, ("true".equalsIgnoreCase((String)parameterObj) || !"0".equalsIgnoreCase((String)parameterObj)));
/*      */ 
/*      */             
/*      */             }
/* 4015 */             else if (parameterObj instanceof Number) {
/* 4016 */               int intValue = ((Number)parameterObj).intValue();
/*      */               
/* 4018 */               setBoolean(parameterIndex, (intValue != 0));
/*      */             }
/*      */             else {
/*      */               
/* 4022 */               throw SQLError.createSQLException("No conversion from " + parameterObj.getClass().getName() + " to Types.BOOLEAN possible.", "S1009", getExceptionInterceptor());
/*      */             } 
/*      */             return;
/*      */ 
/*      */ 
/*      */           
/*      */           case -7:
/*      */           case -6:
/*      */           case -5:
/*      */           case 2:
/*      */           case 3:
/*      */           case 4:
/*      */           case 5:
/*      */           case 6:
/*      */           case 7:
/*      */           case 8:
/* 4038 */             setNumericObject(parameterIndex, parameterObj, targetSqlType, scale);
/*      */             return;
/*      */ 
/*      */           
/*      */           case -1:
/*      */           case 1:
/*      */           case 12:
/* 4045 */             if (parameterObj instanceof BigDecimal) {
/* 4046 */               setString(parameterIndex, StringUtils.fixDecimalExponent(StringUtils.consistentToString((BigDecimal)parameterObj)));
/*      */             
/*      */             }
/*      */             else {
/*      */ 
/*      */               
/* 4052 */               setString(parameterIndex, parameterObj.toString());
/*      */             } 
/*      */             return;
/*      */ 
/*      */ 
/*      */           
/*      */           case 2005:
/* 4059 */             if (parameterObj instanceof Clob) {
/* 4060 */               setClob(parameterIndex, (Clob)parameterObj);
/*      */             } else {
/* 4062 */               setString(parameterIndex, parameterObj.toString());
/*      */             } 
/*      */             return;
/*      */ 
/*      */ 
/*      */           
/*      */           case -4:
/*      */           case -3:
/*      */           case -2:
/*      */           case 2004:
/* 4072 */             if (parameterObj instanceof byte[]) {
/* 4073 */               setBytes(parameterIndex, (byte[])parameterObj);
/* 4074 */             } else if (parameterObj instanceof Blob) {
/* 4075 */               setBlob(parameterIndex, (Blob)parameterObj);
/*      */             } else {
/* 4077 */               setBytes(parameterIndex, StringUtils.getBytes(parameterObj.toString(), this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor()));
/*      */             } 
/*      */             return;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           case 91:
/*      */           case 93:
/* 4091 */             if (parameterObj instanceof String) {
/* 4092 */               ParsePosition pp = new ParsePosition(0);
/* 4093 */               DateFormat sdf = new SimpleDateFormat(getDateTimePattern((String)parameterObj, false), Locale.US);
/*      */               
/* 4095 */               parameterAsDate = sdf.parse((String)parameterObj, pp);
/*      */             } else {
/* 4097 */               parameterAsDate = (Date)parameterObj;
/*      */             } 
/*      */             
/* 4100 */             switch (targetSqlType) {
/*      */               
/*      */               case 91:
/* 4103 */                 if (parameterAsDate instanceof Date) {
/* 4104 */                   setDate(parameterIndex, (Date)parameterAsDate);
/*      */                   break;
/*      */                 } 
/* 4107 */                 setDate(parameterIndex, new Date(parameterAsDate.getTime()));
/*      */                 break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               case 93:
/* 4115 */                 if (parameterAsDate instanceof Timestamp) {
/* 4116 */                   setTimestamp(parameterIndex, (Timestamp)parameterAsDate);
/*      */                   break;
/*      */                 } 
/* 4119 */                 setTimestamp(parameterIndex, new Timestamp(parameterAsDate.getTime()));
/*      */                 break;
/*      */             } 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             return;
/*      */ 
/*      */ 
/*      */           
/*      */           case 92:
/* 4131 */             if (parameterObj instanceof String) {
/* 4132 */               DateFormat sdf = new SimpleDateFormat(getDateTimePattern((String)parameterObj, true), Locale.US);
/*      */               
/* 4134 */               setTime(parameterIndex, new Time(sdf.parse((String)parameterObj).getTime()));
/*      */             }
/* 4136 */             else if (parameterObj instanceof Timestamp) {
/* 4137 */               Timestamp xT = (Timestamp)parameterObj;
/* 4138 */               setTime(parameterIndex, new Time(xT.getTime()));
/*      */             } else {
/* 4140 */               setTime(parameterIndex, (Time)parameterObj);
/*      */             } 
/*      */             return;
/*      */ 
/*      */           
/*      */           case 1111:
/* 4146 */             setSerializableObject(parameterIndex, parameterObj);
/*      */             return;
/*      */         } 
/*      */ 
/*      */         
/* 4151 */         throw SQLError.createSQLException(Messages.getString("PreparedStatement.16"), "S1000", getExceptionInterceptor());
/*      */ 
/*      */       
/*      */       }
/* 4155 */       catch (Exception ex) {
/* 4156 */         if (ex instanceof SQLException) {
/* 4157 */           throw (SQLException)ex;
/*      */         }
/*      */         
/* 4160 */         SQLException sqlEx = SQLError.createSQLException(Messages.getString("PreparedStatement.17") + parameterObj.getClass().toString() + Messages.getString("PreparedStatement.18") + ex.getClass().getName() + Messages.getString("PreparedStatement.19") + ex.getMessage(), "S1000", getExceptionInterceptor());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 4168 */         sqlEx.initCause(ex);
/*      */         
/* 4170 */         throw sqlEx;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected int setOneBatchedParameterSet(PreparedStatement batchedStatement, int batchedParamIndex, Object paramSet) throws SQLException {
/* 4178 */     BatchParams paramArg = (BatchParams)paramSet;
/*      */     
/* 4180 */     boolean[] isNullBatch = paramArg.isNull;
/* 4181 */     boolean[] isStreamBatch = paramArg.isStream;
/*      */     
/* 4183 */     for (int j = 0; j < isNullBatch.length; j++) {
/* 4184 */       if (isNullBatch[j]) {
/* 4185 */         batchedStatement.setNull(batchedParamIndex++, 0);
/*      */       }
/* 4187 */       else if (isStreamBatch[j]) {
/* 4188 */         batchedStatement.setBinaryStream(batchedParamIndex++, paramArg.parameterStreams[j], paramArg.streamLengths[j]);
/*      */       }
/*      */       else {
/*      */         
/* 4192 */         ((PreparedStatement)batchedStatement).setBytesNoEscapeNoQuotes(batchedParamIndex++, paramArg.parameterStrings[j]);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4199 */     return batchedParamIndex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRef(int i, Ref x) throws SQLException {
/* 4216 */     throw SQLError.notImplemented();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setResultSetConcurrency(int concurrencyFlag) {
/* 4226 */     this.resultSetConcurrency = concurrencyFlag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setResultSetType(int typeFlag) {
/* 4236 */     this.resultSetType = typeFlag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setRetrieveGeneratedKeys(boolean retrieveGeneratedKeys) {
/* 4245 */     this.retrieveGeneratedKeys = retrieveGeneratedKeys;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final void setSerializableObject(int parameterIndex, Object parameterObj) throws SQLException {
/*      */     try {
/* 4265 */       ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
/* 4266 */       ObjectOutputStream objectOut = new ObjectOutputStream(bytesOut);
/* 4267 */       objectOut.writeObject(parameterObj);
/* 4268 */       objectOut.flush();
/* 4269 */       objectOut.close();
/* 4270 */       bytesOut.flush();
/* 4271 */       bytesOut.close();
/*      */       
/* 4273 */       byte[] buf = bytesOut.toByteArray();
/* 4274 */       ByteArrayInputStream bytesIn = new ByteArrayInputStream(buf);
/* 4275 */       setBinaryStream(parameterIndex, bytesIn, buf.length);
/* 4276 */       this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = -2;
/* 4277 */     } catch (Exception ex) {
/* 4278 */       SQLException sqlEx = SQLError.createSQLException(Messages.getString("PreparedStatement.54") + ex.getClass().getName(), "S1009", getExceptionInterceptor());
/*      */ 
/*      */       
/* 4281 */       sqlEx.initCause(ex);
/*      */       
/* 4283 */       throw sqlEx;
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
/*      */   public void setShort(int parameterIndex, short x) throws SQLException {
/* 4300 */     setInternal(parameterIndex, String.valueOf(x));
/*      */     
/* 4302 */     this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 5;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setString(int parameterIndex, String x) throws SQLException {
/* 4320 */     if (x == null) {
/* 4321 */       setNull(parameterIndex, 1);
/*      */     } else {
/* 4323 */       checkClosed();
/*      */       
/* 4325 */       int stringLength = x.length();
/*      */       
/* 4327 */       if (this.connection.isNoBackslashEscapesSet()) {
/*      */ 
/*      */         
/* 4330 */         boolean needsHexEscape = isEscapeNeededForString(x, stringLength);
/*      */ 
/*      */         
/* 4333 */         if (!needsHexEscape) {
/* 4334 */           byte[] arrayOfByte = null;
/*      */           
/* 4336 */           StringBuffer quotedString = new StringBuffer(x.length() + 2);
/* 4337 */           quotedString.append('\'');
/* 4338 */           quotedString.append(x);
/* 4339 */           quotedString.append('\'');
/*      */           
/* 4341 */           if (!this.isLoadDataQuery) {
/* 4342 */             arrayOfByte = StringUtils.getBytes(quotedString.toString(), this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
/*      */           
/*      */           }
/*      */           else {
/*      */ 
/*      */             
/* 4348 */             arrayOfByte = quotedString.toString().getBytes();
/*      */           } 
/*      */           
/* 4351 */           setInternal(parameterIndex, arrayOfByte);
/*      */         } else {
/* 4353 */           byte[] arrayOfByte = null;
/*      */           
/* 4355 */           if (!this.isLoadDataQuery) {
/* 4356 */             arrayOfByte = StringUtils.getBytes(x, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
/*      */           
/*      */           }
/*      */           else {
/*      */ 
/*      */             
/* 4362 */             arrayOfByte = x.getBytes();
/*      */           } 
/*      */           
/* 4365 */           setBytes(parameterIndex, arrayOfByte);
/*      */         } 
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/* 4371 */       String parameterAsString = x;
/* 4372 */       boolean needsQuoted = true;
/*      */       
/* 4374 */       if (this.isLoadDataQuery || isEscapeNeededForString(x, stringLength)) {
/* 4375 */         needsQuoted = false;
/*      */         
/* 4377 */         StringBuffer buf = new StringBuffer((int)(x.length() * 1.1D));
/*      */         
/* 4379 */         buf.append('\'');
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 4388 */         for (int i = 0; i < stringLength; i++) {
/* 4389 */           char c = x.charAt(i);
/*      */           
/* 4391 */           switch (c) {
/*      */             case '\000':
/* 4393 */               buf.append('\\');
/* 4394 */               buf.append('0');
/*      */               break;
/*      */ 
/*      */             
/*      */             case '\n':
/* 4399 */               buf.append('\\');
/* 4400 */               buf.append('n');
/*      */               break;
/*      */ 
/*      */             
/*      */             case '\r':
/* 4405 */               buf.append('\\');
/* 4406 */               buf.append('r');
/*      */               break;
/*      */ 
/*      */             
/*      */             case '\\':
/* 4411 */               buf.append('\\');
/* 4412 */               buf.append('\\');
/*      */               break;
/*      */ 
/*      */             
/*      */             case '\'':
/* 4417 */               buf.append('\\');
/* 4418 */               buf.append('\'');
/*      */               break;
/*      */ 
/*      */             
/*      */             case '"':
/* 4423 */               if (this.usingAnsiMode) {
/* 4424 */                 buf.append('\\');
/*      */               }
/*      */               
/* 4427 */               buf.append('"');
/*      */               break;
/*      */ 
/*      */             
/*      */             case '\032':
/* 4432 */               buf.append('\\');
/* 4433 */               buf.append('Z');
/*      */               break;
/*      */ 
/*      */ 
/*      */             
/*      */             case '¥':
/*      */             case '₩':
/* 4440 */               if (this.charsetEncoder != null) {
/* 4441 */                 CharBuffer cbuf = CharBuffer.allocate(1);
/* 4442 */                 ByteBuffer bbuf = ByteBuffer.allocate(1);
/* 4443 */                 cbuf.put(c);
/* 4444 */                 cbuf.position(0);
/* 4445 */                 this.charsetEncoder.encode(cbuf, bbuf, true);
/* 4446 */                 if (bbuf.get(0) == 92) {
/* 4447 */                   buf.append('\\');
/*      */                 }
/*      */               } 
/*      */ 
/*      */             
/*      */             default:
/* 4453 */               buf.append(c);
/*      */               break;
/*      */           } 
/*      */         } 
/* 4457 */         buf.append('\'');
/*      */         
/* 4459 */         parameterAsString = buf.toString();
/*      */       } 
/*      */       
/* 4462 */       byte[] parameterAsBytes = null;
/*      */       
/* 4464 */       if (!this.isLoadDataQuery) {
/* 4465 */         if (needsQuoted) {
/* 4466 */           parameterAsBytes = StringUtils.getBytesWrapped(parameterAsString, '\'', '\'', this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 4471 */           parameterAsBytes = StringUtils.getBytes(parameterAsString, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 4478 */         parameterAsBytes = parameterAsString.getBytes();
/*      */       } 
/*      */       
/* 4481 */       setInternal(parameterIndex, parameterAsBytes);
/*      */       
/* 4483 */       this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 12;
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean isEscapeNeededForString(String x, int stringLength) {
/* 4488 */     boolean needsHexEscape = false;
/*      */     
/* 4490 */     for (int i = 0; i < stringLength; i++) {
/* 4491 */       char c = x.charAt(i);
/*      */       
/* 4493 */       switch (c) {
/*      */         
/*      */         case '\000':
/* 4496 */           needsHexEscape = true;
/*      */           break;
/*      */         
/*      */         case '\n':
/* 4500 */           needsHexEscape = true;
/*      */           break;
/*      */ 
/*      */         
/*      */         case '\r':
/* 4505 */           needsHexEscape = true;
/*      */           break;
/*      */         
/*      */         case '\\':
/* 4509 */           needsHexEscape = true;
/*      */           break;
/*      */ 
/*      */         
/*      */         case '\'':
/* 4514 */           needsHexEscape = true;
/*      */           break;
/*      */ 
/*      */         
/*      */         case '"':
/* 4519 */           needsHexEscape = true;
/*      */           break;
/*      */ 
/*      */         
/*      */         case '\032':
/* 4524 */           needsHexEscape = true;
/*      */           break;
/*      */       } 
/*      */       
/* 4528 */       if (needsHexEscape) {
/*      */         break;
/*      */       }
/*      */     } 
/* 4532 */     return needsHexEscape;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
/* 4551 */     setTimeInternal(parameterIndex, x, cal, cal.getTimeZone(), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTime(int parameterIndex, Time x) throws SQLException {
/* 4568 */     setTimeInternal(parameterIndex, x, (Calendar)null, Util.getDefaultTimeZone(), false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setTimeInternal(int parameterIndex, Time x, Calendar targetCalendar, TimeZone tz, boolean rollForward) throws SQLException {
/* 4589 */     if (x == null) {
/* 4590 */       setNull(parameterIndex, 92);
/*      */     } else {
/* 4592 */       checkClosed();
/*      */       
/* 4594 */       if (!this.useLegacyDatetimeCode) {
/* 4595 */         newSetTimeInternal(parameterIndex, x, targetCalendar);
/*      */       } else {
/* 4597 */         Calendar sessionCalendar = getCalendarInstanceForSessionOrNew();
/*      */         
/* 4599 */         synchronized (sessionCalendar) {
/* 4600 */           x = TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, x, tz, this.connection.getServerTimezoneTZ(), rollForward);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 4607 */         setInternal(parameterIndex, "'" + x.toString() + "'");
/*      */       } 
/*      */       
/* 4610 */       this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 92;
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
/*      */   public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
/* 4630 */     setTimestampInternal(parameterIndex, x, cal, cal.getTimeZone(), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
/* 4647 */     setTimestampInternal(parameterIndex, x, (Calendar)null, Util.getDefaultTimeZone(), false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setTimestampInternal(int parameterIndex, Timestamp x, Calendar targetCalendar, TimeZone tz, boolean rollForward) throws SQLException {
/* 4667 */     if (x == null) {
/* 4668 */       setNull(parameterIndex, 93);
/*      */     } else {
/* 4670 */       checkClosed();
/*      */       
/* 4672 */       if (!this.useLegacyDatetimeCode) {
/* 4673 */         newSetTimestampInternal(parameterIndex, x, targetCalendar);
/*      */       } else {
/* 4675 */         String timestampString = null;
/*      */         
/* 4677 */         Calendar sessionCalendar = this.connection.getUseJDBCCompliantTimezoneShift() ? this.connection.getUtcCalendar() : getCalendarInstanceForSessionOrNew();
/*      */ 
/*      */ 
/*      */         
/* 4681 */         synchronized (sessionCalendar) {
/* 4682 */           x = TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, x, tz, this.connection.getServerTimezoneTZ(), rollForward);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 4689 */         if (this.connection.getUseSSPSCompatibleTimezoneShift()) {
/* 4690 */           doSSPSCompatibleTimezoneShift(parameterIndex, x, sessionCalendar);
/*      */         } else {
/* 4692 */           synchronized (this) {
/* 4693 */             if (this.tsdf == null) {
/* 4694 */               this.tsdf = new SimpleDateFormat("''yyyy-MM-dd HH:mm:ss", Locale.US);
/*      */             }
/*      */             
/* 4697 */             timestampString = this.tsdf.format(x);
/* 4698 */             StringBuffer buf = new StringBuffer();
/* 4699 */             buf.append(timestampString);
/* 4700 */             buf.append('.');
/* 4701 */             buf.append(formatNanos(x.getNanos()));
/* 4702 */             buf.append('\'');
/*      */             
/* 4704 */             setInternal(parameterIndex, buf.toString());
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 4710 */       this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 93;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private synchronized void newSetTimestampInternal(int parameterIndex, Timestamp x, Calendar targetCalendar) throws SQLException {
/* 4716 */     if (this.tsdf == null) {
/* 4717 */       this.tsdf = new SimpleDateFormat("''yyyy-MM-dd HH:mm:ss", Locale.US);
/*      */     }
/*      */     
/* 4720 */     String timestampString = null;
/*      */     
/* 4722 */     if (targetCalendar != null) {
/* 4723 */       targetCalendar.setTime(x);
/* 4724 */       this.tsdf.setTimeZone(targetCalendar.getTimeZone());
/*      */       
/* 4726 */       timestampString = this.tsdf.format(x);
/*      */     } else {
/* 4728 */       this.tsdf.setTimeZone(this.connection.getServerTimezoneTZ());
/* 4729 */       timestampString = this.tsdf.format(x);
/*      */     } 
/*      */     
/* 4732 */     StringBuffer buf = new StringBuffer();
/* 4733 */     buf.append(timestampString);
/* 4734 */     buf.append('.');
/* 4735 */     buf.append(formatNanos(x.getNanos()));
/* 4736 */     buf.append('\'');
/*      */     
/* 4738 */     setInternal(parameterIndex, buf.toString());
/*      */   }
/*      */ 
/*      */   
/*      */   private String formatNanos(int nanos) {
/* 4743 */     return "0";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private synchronized void newSetTimeInternal(int parameterIndex, Time x, Calendar targetCalendar) throws SQLException {
/* 4773 */     if (this.tdf == null) {
/* 4774 */       this.tdf = new SimpleDateFormat("''HH:mm:ss''", Locale.US);
/*      */     }
/*      */ 
/*      */     
/* 4778 */     String timeString = null;
/*      */     
/* 4780 */     if (targetCalendar != null) {
/* 4781 */       targetCalendar.setTime(x);
/* 4782 */       this.tdf.setTimeZone(targetCalendar.getTimeZone());
/*      */       
/* 4784 */       timeString = this.tdf.format(x);
/*      */     } else {
/* 4786 */       this.tdf.setTimeZone(this.connection.getServerTimezoneTZ());
/* 4787 */       timeString = this.tdf.format(x);
/*      */     } 
/*      */     
/* 4790 */     setInternal(parameterIndex, timeString);
/*      */   }
/*      */ 
/*      */   
/*      */   private synchronized void newSetDateInternal(int parameterIndex, Date x, Calendar targetCalendar) throws SQLException {
/* 4795 */     if (this.ddf == null) {
/* 4796 */       this.ddf = new SimpleDateFormat("''yyyy-MM-dd''", Locale.US);
/*      */     }
/*      */ 
/*      */     
/* 4800 */     String timeString = null;
/*      */     
/* 4802 */     if (targetCalendar != null) {
/* 4803 */       targetCalendar.setTime(x);
/* 4804 */       this.ddf.setTimeZone(targetCalendar.getTimeZone());
/*      */       
/* 4806 */       timeString = this.ddf.format(x);
/*      */     } else {
/* 4808 */       this.ddf.setTimeZone(this.connection.getServerTimezoneTZ());
/* 4809 */       timeString = this.ddf.format(x);
/*      */     } 
/*      */     
/* 4812 */     setInternal(parameterIndex, timeString);
/*      */   }
/*      */   
/*      */   private void doSSPSCompatibleTimezoneShift(int parameterIndex, Timestamp x, Calendar sessionCalendar) throws SQLException {
/* 4816 */     Calendar sessionCalendar2 = this.connection.getUseJDBCCompliantTimezoneShift() ? this.connection.getUtcCalendar() : getCalendarInstanceForSessionOrNew();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4821 */     synchronized (sessionCalendar2) {
/* 4822 */       Date oldTime = sessionCalendar2.getTime();
/*      */       
/*      */       try {
/* 4825 */         sessionCalendar2.setTime(x);
/*      */         
/* 4827 */         int year = sessionCalendar2.get(1);
/* 4828 */         int month = sessionCalendar2.get(2) + 1;
/* 4829 */         int date = sessionCalendar2.get(5);
/*      */         
/* 4831 */         int hour = sessionCalendar2.get(11);
/* 4832 */         int minute = sessionCalendar2.get(12);
/* 4833 */         int seconds = sessionCalendar2.get(13);
/*      */         
/* 4835 */         StringBuffer tsBuf = new StringBuffer();
/*      */         
/* 4837 */         tsBuf.append('\'');
/* 4838 */         tsBuf.append(year);
/*      */         
/* 4840 */         tsBuf.append("-");
/*      */         
/* 4842 */         if (month < 10) {
/* 4843 */           tsBuf.append('0');
/*      */         }
/*      */         
/* 4846 */         tsBuf.append(month);
/*      */         
/* 4848 */         tsBuf.append('-');
/*      */         
/* 4850 */         if (date < 10) {
/* 4851 */           tsBuf.append('0');
/*      */         }
/*      */         
/* 4854 */         tsBuf.append(date);
/*      */         
/* 4856 */         tsBuf.append(' ');
/*      */         
/* 4858 */         if (hour < 10) {
/* 4859 */           tsBuf.append('0');
/*      */         }
/*      */         
/* 4862 */         tsBuf.append(hour);
/*      */         
/* 4864 */         tsBuf.append(':');
/*      */         
/* 4866 */         if (minute < 10) {
/* 4867 */           tsBuf.append('0');
/*      */         }
/*      */         
/* 4870 */         tsBuf.append(minute);
/*      */         
/* 4872 */         tsBuf.append(':');
/*      */         
/* 4874 */         if (seconds < 10) {
/* 4875 */           tsBuf.append('0');
/*      */         }
/*      */         
/* 4878 */         tsBuf.append(seconds);
/*      */         
/* 4880 */         tsBuf.append('.');
/* 4881 */         tsBuf.append(formatNanos(x.getNanos()));
/* 4882 */         tsBuf.append('\'');
/*      */         
/* 4884 */         setInternal(parameterIndex, tsBuf.toString());
/*      */       } finally {
/*      */         
/* 4887 */         sessionCalendar.setTime(oldTime);
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
/*      */   public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
/* 4918 */     if (x == null) {
/* 4919 */       setNull(parameterIndex, 12);
/*      */     } else {
/* 4921 */       setBinaryStream(parameterIndex, x, length);
/*      */       
/* 4923 */       this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 2005;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setURL(int parameterIndex, URL arg) throws SQLException {
/* 4931 */     if (arg != null) {
/* 4932 */       setString(parameterIndex, arg.toString());
/*      */       
/* 4934 */       this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 70;
/*      */     } else {
/* 4936 */       setNull(parameterIndex, 1);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final void streamToBytes(Buffer packet, InputStream in, boolean escape, int streamLength, boolean useLength) throws SQLException {
/*      */     try {
/* 4944 */       String connectionEncoding = this.connection.getEncoding();
/*      */       
/* 4946 */       boolean hexEscape = false;
/*      */       
/* 4948 */       if (this.connection.isNoBackslashEscapesSet() || (this.connection.getUseUnicode() && connectionEncoding != null && CharsetMapping.isMultibyteCharset(connectionEncoding) && !this.connection.parserKnowsUnicode()))
/*      */       {
/*      */ 
/*      */ 
/*      */         
/* 4953 */         hexEscape = true;
/*      */       }
/*      */       
/* 4956 */       if (streamLength == -1) {
/* 4957 */         useLength = false;
/*      */       }
/*      */       
/* 4960 */       int bc = -1;
/*      */       
/* 4962 */       if (useLength) {
/* 4963 */         bc = readblock(in, this.streamConvertBuf, streamLength);
/*      */       } else {
/* 4965 */         bc = readblock(in, this.streamConvertBuf);
/*      */       } 
/*      */       
/* 4968 */       int lengthLeftToRead = streamLength - bc;
/*      */       
/* 4970 */       if (hexEscape) {
/* 4971 */         packet.writeStringNoNull("x");
/* 4972 */       } else if (this.connection.getIO().versionMeetsMinimum(4, 1, 0)) {
/* 4973 */         packet.writeStringNoNull("_binary");
/*      */       } 
/*      */       
/* 4976 */       if (escape) {
/* 4977 */         packet.writeByte((byte)39);
/*      */       }
/*      */       
/* 4980 */       while (bc > 0) {
/* 4981 */         if (hexEscape) {
/* 4982 */           hexEscapeBlock(this.streamConvertBuf, packet, bc);
/* 4983 */         } else if (escape) {
/* 4984 */           escapeblockFast(this.streamConvertBuf, packet, bc);
/*      */         } else {
/* 4986 */           packet.writeBytesNoNull(this.streamConvertBuf, 0, bc);
/*      */         } 
/*      */         
/* 4989 */         if (useLength) {
/* 4990 */           bc = readblock(in, this.streamConvertBuf, lengthLeftToRead);
/*      */           
/* 4992 */           if (bc > 0)
/* 4993 */             lengthLeftToRead -= bc; 
/*      */           continue;
/*      */         } 
/* 4996 */         bc = readblock(in, this.streamConvertBuf);
/*      */       } 
/*      */ 
/*      */       
/* 5000 */       if (escape) {
/* 5001 */         packet.writeByte((byte)39);
/*      */       }
/*      */     } finally {
/* 5004 */       if (this.connection.getAutoClosePStmtStreams()) {
/*      */         try {
/* 5006 */           in.close();
/* 5007 */         } catch (IOException ioEx) {}
/*      */ 
/*      */ 
/*      */         
/* 5011 */         in = null;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private final byte[] streamToBytes(InputStream in, boolean escape, int streamLength, boolean useLength) throws SQLException {
/*      */     try {
/* 5019 */       if (streamLength == -1) {
/* 5020 */         useLength = false;
/*      */       }
/*      */       
/* 5023 */       ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
/*      */       
/* 5025 */       int bc = -1;
/*      */       
/* 5027 */       if (useLength) {
/* 5028 */         bc = readblock(in, this.streamConvertBuf, streamLength);
/*      */       } else {
/* 5030 */         bc = readblock(in, this.streamConvertBuf);
/*      */       } 
/*      */       
/* 5033 */       int lengthLeftToRead = streamLength - bc;
/*      */       
/* 5035 */       if (escape) {
/* 5036 */         if (this.connection.versionMeetsMinimum(4, 1, 0)) {
/* 5037 */           bytesOut.write(95);
/* 5038 */           bytesOut.write(98);
/* 5039 */           bytesOut.write(105);
/* 5040 */           bytesOut.write(110);
/* 5041 */           bytesOut.write(97);
/* 5042 */           bytesOut.write(114);
/* 5043 */           bytesOut.write(121);
/*      */         } 
/*      */         
/* 5046 */         bytesOut.write(39);
/*      */       } 
/*      */       
/* 5049 */       while (bc > 0) {
/* 5050 */         if (escape) {
/* 5051 */           escapeblockFast(this.streamConvertBuf, bytesOut, bc);
/*      */         } else {
/* 5053 */           bytesOut.write(this.streamConvertBuf, 0, bc);
/*      */         } 
/*      */         
/* 5056 */         if (useLength) {
/* 5057 */           bc = readblock(in, this.streamConvertBuf, lengthLeftToRead);
/*      */           
/* 5059 */           if (bc > 0)
/* 5060 */             lengthLeftToRead -= bc; 
/*      */           continue;
/*      */         } 
/* 5063 */         bc = readblock(in, this.streamConvertBuf);
/*      */       } 
/*      */ 
/*      */       
/* 5067 */       if (escape) {
/* 5068 */         bytesOut.write(39);
/*      */       }
/*      */       
/* 5071 */       return bytesOut.toByteArray();
/*      */     } finally {
/* 5073 */       if (this.connection.getAutoClosePStmtStreams()) {
/*      */         try {
/* 5075 */           in.close();
/* 5076 */         } catch (IOException ioEx) {}
/*      */ 
/*      */ 
/*      */         
/* 5080 */         in = null;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 5091 */     StringBuffer buf = new StringBuffer();
/* 5092 */     buf.append(super.toString());
/* 5093 */     buf.append(": ");
/*      */     
/*      */     try {
/* 5096 */       buf.append(asSql());
/* 5097 */     } catch (SQLException sqlEx) {
/* 5098 */       buf.append("EXCEPTION: " + sqlEx.toString());
/*      */     } 
/*      */     
/* 5101 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized boolean isClosed() throws SQLException {
/* 5107 */     return this.isClosed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int getParameterIndexOffset() {
/* 5118 */     return 0;
/*      */   }
/*      */   
/*      */   public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
/* 5122 */     setAsciiStream(parameterIndex, x, -1);
/*      */   }
/*      */   
/*      */   public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
/* 5126 */     setAsciiStream(parameterIndex, x, (int)length);
/* 5127 */     this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 2005;
/*      */   }
/*      */   
/*      */   public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
/* 5131 */     setBinaryStream(parameterIndex, x, -1);
/*      */   }
/*      */   
/*      */   public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
/* 5135 */     setBinaryStream(parameterIndex, x, (int)length);
/*      */   }
/*      */   
/*      */   public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
/* 5139 */     setBinaryStream(parameterIndex, inputStream);
/*      */   }
/*      */   
/*      */   public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
/* 5143 */     setCharacterStream(parameterIndex, reader, -1);
/*      */   }
/*      */   
/*      */   public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
/* 5147 */     setCharacterStream(parameterIndex, reader, (int)length);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setClob(int parameterIndex, Reader reader) throws SQLException {
/* 5152 */     setCharacterStream(parameterIndex, reader);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
/* 5158 */     setCharacterStream(parameterIndex, reader, length);
/*      */   }
/*      */   
/*      */   public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
/* 5162 */     setNCharacterStream(parameterIndex, value, -1L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNString(int parameterIndex, String x) throws SQLException {
/* 5180 */     if (this.charEncoding.equalsIgnoreCase("UTF-8") || this.charEncoding.equalsIgnoreCase("utf8")) {
/*      */       
/* 5182 */       setString(parameterIndex, x);
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 5187 */     if (x == null) {
/* 5188 */       setNull(parameterIndex, 1);
/*      */     } else {
/* 5190 */       int stringLength = x.length();
/*      */ 
/*      */ 
/*      */       
/* 5194 */       StringBuffer buf = new StringBuffer((int)(x.length() * 1.1D + 4.0D));
/* 5195 */       buf.append("_utf8");
/* 5196 */       buf.append('\'');
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 5205 */       for (int i = 0; i < stringLength; i++) {
/* 5206 */         char c = x.charAt(i);
/*      */         
/* 5208 */         switch (c) {
/*      */           case '\000':
/* 5210 */             buf.append('\\');
/* 5211 */             buf.append('0');
/*      */             break;
/*      */ 
/*      */           
/*      */           case '\n':
/* 5216 */             buf.append('\\');
/* 5217 */             buf.append('n');
/*      */             break;
/*      */ 
/*      */           
/*      */           case '\r':
/* 5222 */             buf.append('\\');
/* 5223 */             buf.append('r');
/*      */             break;
/*      */ 
/*      */           
/*      */           case '\\':
/* 5228 */             buf.append('\\');
/* 5229 */             buf.append('\\');
/*      */             break;
/*      */ 
/*      */           
/*      */           case '\'':
/* 5234 */             buf.append('\\');
/* 5235 */             buf.append('\'');
/*      */             break;
/*      */ 
/*      */           
/*      */           case '"':
/* 5240 */             if (this.usingAnsiMode) {
/* 5241 */               buf.append('\\');
/*      */             }
/*      */             
/* 5244 */             buf.append('"');
/*      */             break;
/*      */ 
/*      */           
/*      */           case '\032':
/* 5249 */             buf.append('\\');
/* 5250 */             buf.append('Z');
/*      */             break;
/*      */ 
/*      */           
/*      */           default:
/* 5255 */             buf.append(c);
/*      */             break;
/*      */         } 
/*      */       } 
/* 5259 */       buf.append('\'');
/*      */       
/* 5261 */       String parameterAsString = buf.toString();
/*      */       
/* 5263 */       byte[] parameterAsBytes = null;
/*      */       
/* 5265 */       if (!this.isLoadDataQuery) {
/* 5266 */         parameterAsBytes = StringUtils.getBytes(parameterAsString, this.connection.getCharsetConverter("UTF-8"), "UTF-8", this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/* 5272 */         parameterAsBytes = parameterAsString.getBytes();
/*      */       } 
/*      */       
/* 5275 */       setInternal(parameterIndex, parameterAsBytes);
/*      */       
/* 5277 */       this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = -9;
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
/*      */   public void setNCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
/*      */     try {
/* 5306 */       if (reader == null) {
/* 5307 */         setNull(parameterIndex, -1);
/*      */       } else {
/*      */         
/* 5310 */         char[] c = null;
/* 5311 */         int len = 0;
/*      */         
/* 5313 */         boolean useLength = this.connection.getUseStreamLengthsInPrepStmts();
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 5318 */         if (useLength && length != -1L) {
/* 5319 */           c = new char[(int)length];
/*      */           
/* 5321 */           int numCharsRead = readFully(reader, c, (int)length);
/*      */ 
/*      */ 
/*      */           
/* 5325 */           setNString(parameterIndex, new String(c, 0, numCharsRead));
/*      */         } else {
/*      */           
/* 5328 */           c = new char[4096];
/*      */           
/* 5330 */           StringBuffer buf = new StringBuffer();
/*      */           
/* 5332 */           while ((len = reader.read(c)) != -1) {
/* 5333 */             buf.append(c, 0, len);
/*      */           }
/*      */           
/* 5336 */           setNString(parameterIndex, buf.toString());
/*      */         } 
/*      */         
/* 5339 */         this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 2011;
/*      */       } 
/* 5341 */     } catch (IOException ioEx) {
/* 5342 */       throw SQLError.createSQLException(ioEx.toString(), "S1000", getExceptionInterceptor());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNClob(int parameterIndex, Reader reader) throws SQLException {
/* 5348 */     setNCharacterStream(parameterIndex, reader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
/* 5366 */     if (reader == null) {
/* 5367 */       setNull(parameterIndex, -1);
/*      */     } else {
/* 5369 */       setNCharacterStream(parameterIndex, reader, length);
/*      */     } 
/*      */   }
/*      */   
/*      */   public ParameterBindings getParameterBindings() throws SQLException {
/* 5374 */     return new EmulatedPreparedStatementBindings(this);
/*      */   }
/*      */   
/*      */   class EmulatedPreparedStatementBindings implements ParameterBindings { private ResultSetImpl bindingsAsRs;
/*      */     private boolean[] parameterIsNull;
/*      */     private final PreparedStatement this$0;
/*      */     
/*      */     public EmulatedPreparedStatementBindings(PreparedStatement this$0) throws SQLException {
/* 5382 */       this.this$0 = this$0;
/* 5383 */       List rows = new ArrayList();
/* 5384 */       this.parameterIsNull = new boolean[this$0.parameterCount];
/* 5385 */       System.arraycopy(this$0.isNull, 0, this.parameterIsNull, 0, this$0.parameterCount);
/*      */ 
/*      */       
/* 5388 */       byte[][] rowData = new byte[this$0.parameterCount][];
/* 5389 */       Field[] typeMetadata = new Field[this$0.parameterCount];
/*      */       
/* 5391 */       for (int i = 0; i < this$0.parameterCount; i++) {
/* 5392 */         if (this$0.batchCommandIndex == -1) {
/* 5393 */           rowData[i] = this$0.getBytesRepresentation(i);
/*      */         } else {
/* 5395 */           rowData[i] = this$0.getBytesRepresentationForBatch(i, this$0.batchCommandIndex);
/*      */         } 
/* 5397 */         int charsetIndex = 0;
/*      */         
/* 5399 */         if (this$0.parameterTypes[i] == -2 || this$0.parameterTypes[i] == 2004) {
/*      */           
/* 5401 */           charsetIndex = 63;
/*      */         } else {
/* 5403 */           String mysqlEncodingName = CharsetMapping.getMysqlEncodingForJavaEncoding(this$0.connection.getEncoding(), this$0.connection);
/*      */ 
/*      */           
/* 5406 */           charsetIndex = CharsetMapping.getCharsetIndexForMysqlEncodingName(mysqlEncodingName);
/*      */         } 
/*      */ 
/*      */         
/* 5410 */         Field parameterMetadata = new Field(null, "parameter_" + (i + 1), charsetIndex, this$0.parameterTypes[i], (rowData[i]).length);
/*      */ 
/*      */         
/* 5413 */         parameterMetadata.setConnection(this$0.connection);
/* 5414 */         typeMetadata[i] = parameterMetadata;
/*      */       } 
/*      */       
/* 5417 */       rows.add(new ByteArrayRow(rowData, this$0.getExceptionInterceptor()));
/*      */       
/* 5419 */       this.bindingsAsRs = new ResultSetImpl(this$0.connection.getCatalog(), typeMetadata, new RowDataStatic(rows), this$0.connection, null);
/*      */       
/* 5421 */       this.bindingsAsRs.next();
/*      */     }
/*      */     
/*      */     public Array getArray(int parameterIndex) throws SQLException {
/* 5425 */       return this.bindingsAsRs.getArray(parameterIndex);
/*      */     }
/*      */ 
/*      */     
/*      */     public InputStream getAsciiStream(int parameterIndex) throws SQLException {
/* 5430 */       return this.bindingsAsRs.getAsciiStream(parameterIndex);
/*      */     }
/*      */     
/*      */     public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
/* 5434 */       return this.bindingsAsRs.getBigDecimal(parameterIndex);
/*      */     }
/*      */ 
/*      */     
/*      */     public InputStream getBinaryStream(int parameterIndex) throws SQLException {
/* 5439 */       return this.bindingsAsRs.getBinaryStream(parameterIndex);
/*      */     }
/*      */     
/*      */     public Blob getBlob(int parameterIndex) throws SQLException {
/* 5443 */       return this.bindingsAsRs.getBlob(parameterIndex);
/*      */     }
/*      */     
/*      */     public boolean getBoolean(int parameterIndex) throws SQLException {
/* 5447 */       return this.bindingsAsRs.getBoolean(parameterIndex);
/*      */     }
/*      */     
/*      */     public byte getByte(int parameterIndex) throws SQLException {
/* 5451 */       return this.bindingsAsRs.getByte(parameterIndex);
/*      */     }
/*      */     
/*      */     public byte[] getBytes(int parameterIndex) throws SQLException {
/* 5455 */       return this.bindingsAsRs.getBytes(parameterIndex);
/*      */     }
/*      */ 
/*      */     
/*      */     public Reader getCharacterStream(int parameterIndex) throws SQLException {
/* 5460 */       return this.bindingsAsRs.getCharacterStream(parameterIndex);
/*      */     }
/*      */     
/*      */     public Clob getClob(int parameterIndex) throws SQLException {
/* 5464 */       return this.bindingsAsRs.getClob(parameterIndex);
/*      */     }
/*      */     
/*      */     public Date getDate(int parameterIndex) throws SQLException {
/* 5468 */       return this.bindingsAsRs.getDate(parameterIndex);
/*      */     }
/*      */     
/*      */     public double getDouble(int parameterIndex) throws SQLException {
/* 5472 */       return this.bindingsAsRs.getDouble(parameterIndex);
/*      */     }
/*      */     
/*      */     public float getFloat(int parameterIndex) throws SQLException {
/* 5476 */       return this.bindingsAsRs.getFloat(parameterIndex);
/*      */     }
/*      */     
/*      */     public int getInt(int parameterIndex) throws SQLException {
/* 5480 */       return this.bindingsAsRs.getInt(parameterIndex);
/*      */     }
/*      */     
/*      */     public long getLong(int parameterIndex) throws SQLException {
/* 5484 */       return this.bindingsAsRs.getLong(parameterIndex);
/*      */     }
/*      */ 
/*      */     
/*      */     public Reader getNCharacterStream(int parameterIndex) throws SQLException {
/* 5489 */       return this.bindingsAsRs.getCharacterStream(parameterIndex);
/*      */     }
/*      */     
/*      */     public Reader getNClob(int parameterIndex) throws SQLException {
/* 5493 */       return this.bindingsAsRs.getCharacterStream(parameterIndex);
/*      */     }
/*      */     
/*      */     public Object getObject(int parameterIndex) throws SQLException {
/* 5497 */       this.this$0.checkBounds(parameterIndex, 0);
/*      */       
/* 5499 */       if (this.parameterIsNull[parameterIndex - 1]) {
/* 5500 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 5507 */       switch (this.this$0.parameterTypes[parameterIndex - 1]) {
/*      */         case -6:
/* 5509 */           return new Byte(getByte(parameterIndex));
/*      */         case 5:
/* 5511 */           return new Short(getShort(parameterIndex));
/*      */         case 4:
/* 5513 */           return new Integer(getInt(parameterIndex));
/*      */         case -5:
/* 5515 */           return new Long(getLong(parameterIndex));
/*      */         case 6:
/* 5517 */           return new Float(getFloat(parameterIndex));
/*      */         case 8:
/* 5519 */           return new Double(getDouble(parameterIndex));
/*      */       } 
/* 5521 */       return this.bindingsAsRs.getObject(parameterIndex);
/*      */     }
/*      */ 
/*      */     
/*      */     public Ref getRef(int parameterIndex) throws SQLException {
/* 5526 */       return this.bindingsAsRs.getRef(parameterIndex);
/*      */     }
/*      */     
/*      */     public short getShort(int parameterIndex) throws SQLException {
/* 5530 */       return this.bindingsAsRs.getShort(parameterIndex);
/*      */     }
/*      */     
/*      */     public String getString(int parameterIndex) throws SQLException {
/* 5534 */       return this.bindingsAsRs.getString(parameterIndex);
/*      */     }
/*      */     
/*      */     public Time getTime(int parameterIndex) throws SQLException {
/* 5538 */       return this.bindingsAsRs.getTime(parameterIndex);
/*      */     }
/*      */     
/*      */     public Timestamp getTimestamp(int parameterIndex) throws SQLException {
/* 5542 */       return this.bindingsAsRs.getTimestamp(parameterIndex);
/*      */     }
/*      */     
/*      */     public URL getURL(int parameterIndex) throws SQLException {
/* 5546 */       return this.bindingsAsRs.getURL(parameterIndex);
/*      */     }
/*      */     
/*      */     public boolean isNull(int parameterIndex) throws SQLException {
/* 5550 */       this.this$0.checkBounds(parameterIndex, 0);
/*      */       
/* 5552 */       return this.parameterIsNull[parameterIndex - 1];
/*      */     } }
/*      */ 
/*      */   
/*      */   public String getPreparedSql() {
/* 5557 */     return this.originalSql;
/*      */   }
/*      */   
/*      */   public int getUpdateCount() throws SQLException {
/* 5561 */     int count = super.getUpdateCount();
/*      */     
/* 5563 */     if (containsOnDuplicateKeyUpdateInSQL() && this.compensateForOnDuplicateKeyUpdate)
/*      */     {
/* 5565 */       if (count == 2 || count == 0) {
/* 5566 */         count = 1;
/*      */       }
/*      */     }
/*      */     
/* 5570 */     return count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static boolean canRewrite(String sql, boolean isOnDuplicateKeyUpdate, int locationOfOnDuplicateKeyUpdate, int statementStartPos) {
/* 5577 */     boolean rewritableOdku = true;
/*      */     
/* 5579 */     if (isOnDuplicateKeyUpdate) {
/* 5580 */       int updateClausePos = StringUtils.indexOfIgnoreCase(locationOfOnDuplicateKeyUpdate, sql, " UPDATE ");
/*      */ 
/*      */       
/* 5583 */       if (updateClausePos != -1) {
/* 5584 */         rewritableOdku = (StringUtils.indexOfIgnoreCaseRespectMarker(updateClausePos, sql, "LAST_INSERT_ID", "\"'`", "\"'`", false) == -1);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5591 */     return (StringUtils.startsWithIgnoreCaseAndWs(sql, "INSERT", statementStartPos) && StringUtils.indexOfIgnoreCaseRespectMarker(statementStartPos, sql, "SELECT", "\"'`", "\"'`", false) == -1 && rewritableOdku);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\PreparedStatement.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */