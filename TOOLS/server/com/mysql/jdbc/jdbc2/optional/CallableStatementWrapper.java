/*      */ package com.mysql.jdbc.jdbc2.optional;
/*      */ 
/*      */ import com.mysql.jdbc.SQLError;
/*      */ import com.mysql.jdbc.Util;
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.math.BigDecimal;
/*      */ import java.net.URL;
/*      */ import java.sql.Array;
/*      */ import java.sql.Blob;
/*      */ import java.sql.CallableStatement;
/*      */ import java.sql.Clob;
/*      */ import java.sql.Date;
/*      */ import java.sql.Ref;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.Calendar;
/*      */ import java.util.Map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CallableStatementWrapper
/*      */   extends PreparedStatementWrapper
/*      */   implements CallableStatement
/*      */ {
/*      */   private static final Constructor JDBC_4_CALLABLE_STATEMENT_WRAPPER_CTOR;
/*      */   
/*      */   static {
/*   59 */     if (Util.isJdbc4()) {
/*      */       try {
/*   61 */         JDBC_4_CALLABLE_STATEMENT_WRAPPER_CTOR = Class.forName("com.mysql.jdbc.jdbc2.optional.JDBC4CallableStatementWrapper").getConstructor(new Class[] { ConnectionWrapper.class, MysqlPooledConnection.class, CallableStatement.class });
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*   66 */       catch (SecurityException e) {
/*   67 */         throw new RuntimeException(e);
/*   68 */       } catch (NoSuchMethodException e) {
/*   69 */         throw new RuntimeException(e);
/*   70 */       } catch (ClassNotFoundException e) {
/*   71 */         throw new RuntimeException(e);
/*      */       } 
/*      */     } else {
/*   74 */       JDBC_4_CALLABLE_STATEMENT_WRAPPER_CTOR = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected static CallableStatementWrapper getInstance(ConnectionWrapper c, MysqlPooledConnection conn, CallableStatement toWrap) throws SQLException {
/*   81 */     if (!Util.isJdbc4()) {
/*   82 */       return new CallableStatementWrapper(c, conn, toWrap);
/*      */     }
/*      */ 
/*      */     
/*   86 */     return (CallableStatementWrapper)Util.handleNewInstance(JDBC_4_CALLABLE_STATEMENT_WRAPPER_CTOR, new Object[] { c, conn, toWrap }, conn.getExceptionInterceptor());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CallableStatementWrapper(ConnectionWrapper c, MysqlPooledConnection conn, CallableStatement toWrap) {
/*  101 */     super(c, conn, toWrap);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
/*      */     try {
/*  112 */       if (this.wrappedStmt != null) {
/*  113 */         ((CallableStatement)this.wrappedStmt).registerOutParameter(parameterIndex, sqlType);
/*      */       } else {
/*      */         
/*  116 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/*  120 */     catch (SQLException sqlEx) {
/*  121 */       checkAndFireConnectionError(sqlEx);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
/*      */     try {
/*  133 */       if (this.wrappedStmt != null) {
/*  134 */         ((CallableStatement)this.wrappedStmt).registerOutParameter(parameterIndex, sqlType, scale);
/*      */       } else {
/*      */         
/*  137 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/*  141 */     catch (SQLException sqlEx) {
/*  142 */       checkAndFireConnectionError(sqlEx);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean wasNull() throws SQLException {
/*      */     try {
/*  153 */       if (this.wrappedStmt != null) {
/*  154 */         return ((CallableStatement)this.wrappedStmt).wasNull();
/*      */       }
/*  156 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/*  160 */     catch (SQLException sqlEx) {
/*  161 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/*  164 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getString(int parameterIndex) throws SQLException {
/*      */     try {
/*  174 */       if (this.wrappedStmt != null) {
/*  175 */         return ((CallableStatement)this.wrappedStmt).getString(parameterIndex);
/*      */       }
/*      */       
/*  178 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/*  182 */     catch (SQLException sqlEx) {
/*  183 */       checkAndFireConnectionError(sqlEx);
/*      */       
/*  185 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getBoolean(int parameterIndex) throws SQLException {
/*      */     try {
/*  195 */       if (this.wrappedStmt != null) {
/*  196 */         return ((CallableStatement)this.wrappedStmt).getBoolean(parameterIndex);
/*      */       }
/*      */       
/*  199 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/*  203 */     catch (SQLException sqlEx) {
/*  204 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/*  207 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getByte(int parameterIndex) throws SQLException {
/*      */     try {
/*  217 */       if (this.wrappedStmt != null) {
/*  218 */         return ((CallableStatement)this.wrappedStmt).getByte(parameterIndex);
/*      */       }
/*      */       
/*  221 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/*  225 */     catch (SQLException sqlEx) {
/*  226 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/*  229 */       return 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short getShort(int parameterIndex) throws SQLException {
/*      */     try {
/*  239 */       if (this.wrappedStmt != null) {
/*  240 */         return ((CallableStatement)this.wrappedStmt).getShort(parameterIndex);
/*      */       }
/*      */       
/*  243 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/*  247 */     catch (SQLException sqlEx) {
/*  248 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/*  251 */       return 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getInt(int parameterIndex) throws SQLException {
/*      */     try {
/*  261 */       if (this.wrappedStmt != null) {
/*  262 */         return ((CallableStatement)this.wrappedStmt).getInt(parameterIndex);
/*      */       }
/*      */       
/*  265 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/*  269 */     catch (SQLException sqlEx) {
/*  270 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/*  273 */       return 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLong(int parameterIndex) throws SQLException {
/*      */     try {
/*  283 */       if (this.wrappedStmt != null) {
/*  284 */         return ((CallableStatement)this.wrappedStmt).getLong(parameterIndex);
/*      */       }
/*      */       
/*  287 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/*  291 */     catch (SQLException sqlEx) {
/*  292 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/*  295 */       return 0L;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFloat(int parameterIndex) throws SQLException {
/*      */     try {
/*  305 */       if (this.wrappedStmt != null) {
/*  306 */         return ((CallableStatement)this.wrappedStmt).getFloat(parameterIndex);
/*      */       }
/*      */       
/*  309 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/*  313 */     catch (SQLException sqlEx) {
/*  314 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/*  317 */       return 0.0F;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getDouble(int parameterIndex) throws SQLException {
/*      */     try {
/*  327 */       if (this.wrappedStmt != null) {
/*  328 */         return ((CallableStatement)this.wrappedStmt).getDouble(parameterIndex);
/*      */       }
/*      */       
/*  331 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/*  335 */     catch (SQLException sqlEx) {
/*  336 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/*  339 */       return 0.0D;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
/*      */     try {
/*  350 */       if (this.wrappedStmt != null) {
/*  351 */         return ((CallableStatement)this.wrappedStmt).getBigDecimal(parameterIndex, scale);
/*      */       }
/*      */       
/*  354 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/*  358 */     catch (SQLException sqlEx) {
/*  359 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/*  362 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getBytes(int parameterIndex) throws SQLException {
/*      */     try {
/*  372 */       if (this.wrappedStmt != null) {
/*  373 */         return ((CallableStatement)this.wrappedStmt).getBytes(parameterIndex);
/*      */       }
/*      */       
/*  376 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/*  380 */     catch (SQLException sqlEx) {
/*  381 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/*  384 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Date getDate(int parameterIndex) throws SQLException {
/*      */     try {
/*  394 */       if (this.wrappedStmt != null) {
/*  395 */         return ((CallableStatement)this.wrappedStmt).getDate(parameterIndex);
/*      */       }
/*      */       
/*  398 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/*  402 */     catch (SQLException sqlEx) {
/*  403 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/*  406 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Time getTime(int parameterIndex) throws SQLException {
/*      */     try {
/*  416 */       if (this.wrappedStmt != null) {
/*  417 */         return ((CallableStatement)this.wrappedStmt).getTime(parameterIndex);
/*      */       }
/*      */       
/*  420 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/*  424 */     catch (SQLException sqlEx) {
/*  425 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/*  428 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Timestamp getTimestamp(int parameterIndex) throws SQLException {
/*      */     try {
/*  438 */       if (this.wrappedStmt != null) {
/*  439 */         return ((CallableStatement)this.wrappedStmt).getTimestamp(parameterIndex);
/*      */       }
/*      */       
/*  442 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/*  446 */     catch (SQLException sqlEx) {
/*  447 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/*  450 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getObject(int parameterIndex) throws SQLException {
/*      */     try {
/*  460 */       if (this.wrappedStmt != null) {
/*  461 */         return ((CallableStatement)this.wrappedStmt).getObject(parameterIndex);
/*      */       }
/*      */       
/*  464 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/*  468 */     catch (SQLException sqlEx) {
/*  469 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/*  472 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
/*      */     try {
/*  482 */       if (this.wrappedStmt != null) {
/*  483 */         return ((CallableStatement)this.wrappedStmt).getBigDecimal(parameterIndex);
/*      */       }
/*      */       
/*  486 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/*  490 */     catch (SQLException sqlEx) {
/*  491 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/*  494 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getObject(int parameterIndex, Map typeMap) throws SQLException {
/*      */     try {
/*  505 */       if (this.wrappedStmt != null) {
/*  506 */         return ((CallableStatement)this.wrappedStmt).getObject(parameterIndex, typeMap);
/*      */       }
/*      */       
/*  509 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/*  513 */     catch (SQLException sqlEx) {
/*  514 */       checkAndFireConnectionError(sqlEx);
/*      */       
/*  516 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Ref getRef(int parameterIndex) throws SQLException {
/*      */     try {
/*  526 */       if (this.wrappedStmt != null) {
/*  527 */         return ((CallableStatement)this.wrappedStmt).getRef(parameterIndex);
/*      */       }
/*      */       
/*  530 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/*  534 */     catch (SQLException sqlEx) {
/*  535 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/*  538 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Blob getBlob(int parameterIndex) throws SQLException {
/*      */     try {
/*  548 */       if (this.wrappedStmt != null) {
/*  549 */         return ((CallableStatement)this.wrappedStmt).getBlob(parameterIndex);
/*      */       }
/*      */       
/*  552 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/*  556 */     catch (SQLException sqlEx) {
/*  557 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/*  560 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Clob getClob(int parameterIndex) throws SQLException {
/*      */     try {
/*  570 */       if (this.wrappedStmt != null) {
/*  571 */         return ((CallableStatement)this.wrappedStmt).getClob(parameterIndex);
/*      */       }
/*      */       
/*  574 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/*  578 */     catch (SQLException sqlEx) {
/*  579 */       checkAndFireConnectionError(sqlEx);
/*      */       
/*  581 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Array getArray(int parameterIndex) throws SQLException {
/*      */     try {
/*  591 */       if (this.wrappedStmt != null) {
/*  592 */         return ((CallableStatement)this.wrappedStmt).getArray(parameterIndex);
/*      */       }
/*      */       
/*  595 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/*  599 */     catch (SQLException sqlEx) {
/*  600 */       checkAndFireConnectionError(sqlEx);
/*      */       
/*  602 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
/*      */     try {
/*  612 */       if (this.wrappedStmt != null) {
/*  613 */         return ((CallableStatement)this.wrappedStmt).getDate(parameterIndex, cal);
/*      */       }
/*      */       
/*  616 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/*  620 */     catch (SQLException sqlEx) {
/*  621 */       checkAndFireConnectionError(sqlEx);
/*      */       
/*  623 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
/*      */     try {
/*  633 */       if (this.wrappedStmt != null) {
/*  634 */         return ((CallableStatement)this.wrappedStmt).getTime(parameterIndex, cal);
/*      */       }
/*      */       
/*  637 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/*  641 */     catch (SQLException sqlEx) {
/*  642 */       checkAndFireConnectionError(sqlEx);
/*      */       
/*  644 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
/*      */     try {
/*  655 */       if (this.wrappedStmt != null) {
/*  656 */         return ((CallableStatement)this.wrappedStmt).getTimestamp(parameterIndex, cal);
/*      */       }
/*      */       
/*  659 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/*  663 */     catch (SQLException sqlEx) {
/*  664 */       checkAndFireConnectionError(sqlEx);
/*      */       
/*  666 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void registerOutParameter(int paramIndex, int sqlType, String typeName) throws SQLException {
/*      */     try {
/*  678 */       if (this.wrappedStmt != null) {
/*  679 */         ((CallableStatement)this.wrappedStmt).registerOutParameter(paramIndex, sqlType, typeName);
/*      */       } else {
/*      */         
/*  682 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/*  686 */     catch (SQLException sqlEx) {
/*  687 */       checkAndFireConnectionError(sqlEx);
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
/*      */   public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
/*      */     try {
/*  700 */       if (this.wrappedStmt != null) {
/*  701 */         ((CallableStatement)this.wrappedStmt).registerOutParameter(parameterName, sqlType);
/*      */       } else {
/*      */         
/*  704 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/*  708 */     catch (SQLException sqlEx) {
/*  709 */       checkAndFireConnectionError(sqlEx);
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
/*      */   public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
/*      */     try {
/*  722 */       if (this.wrappedStmt != null) {
/*  723 */         ((CallableStatement)this.wrappedStmt).registerOutParameter(parameterName, sqlType, scale);
/*      */       } else {
/*      */         
/*  726 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/*  730 */     catch (SQLException sqlEx) {
/*  731 */       checkAndFireConnectionError(sqlEx);
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
/*      */   public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
/*      */     try {
/*  744 */       if (this.wrappedStmt != null) {
/*  745 */         ((CallableStatement)this.wrappedStmt).registerOutParameter(parameterName, sqlType, typeName);
/*      */       } else {
/*      */         
/*  748 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/*  752 */     catch (SQLException sqlEx) {
/*  753 */       checkAndFireConnectionError(sqlEx);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URL getURL(int parameterIndex) throws SQLException {
/*      */     try {
/*  764 */       if (this.wrappedStmt != null) {
/*  765 */         return ((CallableStatement)this.wrappedStmt).getURL(parameterIndex);
/*      */       }
/*      */       
/*  768 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/*  772 */     catch (SQLException sqlEx) {
/*  773 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/*  776 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setURL(String parameterName, URL val) throws SQLException {
/*      */     try {
/*  786 */       if (this.wrappedStmt != null) {
/*  787 */         ((CallableStatement)this.wrappedStmt).setURL(parameterName, val);
/*      */       } else {
/*      */         
/*  790 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/*  794 */     catch (SQLException sqlEx) {
/*  795 */       checkAndFireConnectionError(sqlEx);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNull(String parameterName, int sqlType) throws SQLException {
/*      */     try {
/*  806 */       if (this.wrappedStmt != null) {
/*  807 */         ((CallableStatement)this.wrappedStmt).setNull(parameterName, sqlType);
/*      */       } else {
/*      */         
/*  810 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/*  814 */     catch (SQLException sqlEx) {
/*  815 */       checkAndFireConnectionError(sqlEx);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBoolean(String parameterName, boolean x) throws SQLException {
/*      */     try {
/*  826 */       if (this.wrappedStmt != null) {
/*  827 */         ((CallableStatement)this.wrappedStmt).setBoolean(parameterName, x);
/*      */       } else {
/*      */         
/*  830 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/*  834 */     catch (SQLException sqlEx) {
/*  835 */       checkAndFireConnectionError(sqlEx);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setByte(String parameterName, byte x) throws SQLException {
/*      */     try {
/*  846 */       if (this.wrappedStmt != null) {
/*  847 */         ((CallableStatement)this.wrappedStmt).setByte(parameterName, x);
/*      */       } else {
/*      */         
/*  850 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/*  854 */     catch (SQLException sqlEx) {
/*  855 */       checkAndFireConnectionError(sqlEx);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setShort(String parameterName, short x) throws SQLException {
/*      */     try {
/*  866 */       if (this.wrappedStmt != null) {
/*  867 */         ((CallableStatement)this.wrappedStmt).setShort(parameterName, x);
/*      */       } else {
/*      */         
/*  870 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/*  874 */     catch (SQLException sqlEx) {
/*  875 */       checkAndFireConnectionError(sqlEx);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInt(String parameterName, int x) throws SQLException {
/*      */     try {
/*  886 */       if (this.wrappedStmt != null) {
/*  887 */         ((CallableStatement)this.wrappedStmt).setInt(parameterName, x);
/*      */       } else {
/*  889 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/*  893 */     catch (SQLException sqlEx) {
/*  894 */       checkAndFireConnectionError(sqlEx);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLong(String parameterName, long x) throws SQLException {
/*      */     try {
/*  905 */       if (this.wrappedStmt != null) {
/*  906 */         ((CallableStatement)this.wrappedStmt).setLong(parameterName, x);
/*      */       } else {
/*      */         
/*  909 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/*  913 */     catch (SQLException sqlEx) {
/*  914 */       checkAndFireConnectionError(sqlEx);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFloat(String parameterName, float x) throws SQLException {
/*      */     try {
/*  925 */       if (this.wrappedStmt != null) {
/*  926 */         ((CallableStatement)this.wrappedStmt).setFloat(parameterName, x);
/*      */       } else {
/*      */         
/*  929 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/*  933 */     catch (SQLException sqlEx) {
/*  934 */       checkAndFireConnectionError(sqlEx);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDouble(String parameterName, double x) throws SQLException {
/*      */     try {
/*  945 */       if (this.wrappedStmt != null) {
/*  946 */         ((CallableStatement)this.wrappedStmt).setDouble(parameterName, x);
/*      */       } else {
/*      */         
/*  949 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/*  953 */     catch (SQLException sqlEx) {
/*  954 */       checkAndFireConnectionError(sqlEx);
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
/*      */   public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
/*      */     try {
/*  967 */       if (this.wrappedStmt != null) {
/*  968 */         ((CallableStatement)this.wrappedStmt).setBigDecimal(parameterName, x);
/*      */       } else {
/*      */         
/*  971 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/*  975 */     catch (SQLException sqlEx) {
/*  976 */       checkAndFireConnectionError(sqlEx);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setString(String parameterName, String x) throws SQLException {
/*      */     try {
/*  988 */       if (this.wrappedStmt != null) {
/*  989 */         ((CallableStatement)this.wrappedStmt).setString(parameterName, x);
/*      */       } else {
/*      */         
/*  992 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/*  996 */     catch (SQLException sqlEx) {
/*  997 */       checkAndFireConnectionError(sqlEx);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBytes(String parameterName, byte[] x) throws SQLException {
/*      */     try {
/* 1008 */       if (this.wrappedStmt != null) {
/* 1009 */         ((CallableStatement)this.wrappedStmt).setBytes(parameterName, x);
/*      */       } else {
/*      */         
/* 1012 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/* 1016 */     catch (SQLException sqlEx) {
/* 1017 */       checkAndFireConnectionError(sqlEx);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDate(String parameterName, Date x) throws SQLException {
/*      */     try {
/* 1028 */       if (this.wrappedStmt != null) {
/* 1029 */         ((CallableStatement)this.wrappedStmt).setDate(parameterName, x);
/*      */       } else {
/*      */         
/* 1032 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/* 1036 */     catch (SQLException sqlEx) {
/* 1037 */       checkAndFireConnectionError(sqlEx);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTime(String parameterName, Time x) throws SQLException {
/*      */     try {
/* 1048 */       if (this.wrappedStmt != null) {
/* 1049 */         ((CallableStatement)this.wrappedStmt).setTime(parameterName, x);
/*      */       } else {
/*      */         
/* 1052 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/* 1056 */     catch (SQLException sqlEx) {
/* 1057 */       checkAndFireConnectionError(sqlEx);
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
/*      */   public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
/*      */     try {
/* 1070 */       if (this.wrappedStmt != null) {
/* 1071 */         ((CallableStatement)this.wrappedStmt).setTimestamp(parameterName, x);
/*      */       } else {
/*      */         
/* 1074 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/* 1078 */     catch (SQLException sqlEx) {
/* 1079 */       checkAndFireConnectionError(sqlEx);
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
/*      */   public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
/*      */     try {
/* 1092 */       if (this.wrappedStmt != null) {
/* 1093 */         ((CallableStatement)this.wrappedStmt).setAsciiStream(parameterName, x, length);
/*      */       } else {
/*      */         
/* 1096 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/* 1100 */     catch (SQLException sqlEx) {
/* 1101 */       checkAndFireConnectionError(sqlEx);
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
/*      */   public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
/*      */     try {
/* 1115 */       if (this.wrappedStmt != null) {
/* 1116 */         ((CallableStatement)this.wrappedStmt).setBinaryStream(parameterName, x, length);
/*      */       } else {
/*      */         
/* 1119 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/* 1123 */     catch (SQLException sqlEx) {
/* 1124 */       checkAndFireConnectionError(sqlEx);
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
/*      */   public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
/*      */     try {
/* 1137 */       if (this.wrappedStmt != null) {
/* 1138 */         ((CallableStatement)this.wrappedStmt).setObject(parameterName, x, targetSqlType, scale);
/*      */       } else {
/*      */         
/* 1141 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/* 1145 */     catch (SQLException sqlEx) {
/* 1146 */       checkAndFireConnectionError(sqlEx);
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
/*      */   public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
/*      */     try {
/* 1159 */       if (this.wrappedStmt != null) {
/* 1160 */         ((CallableStatement)this.wrappedStmt).setObject(parameterName, x, targetSqlType);
/*      */       } else {
/*      */         
/* 1163 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/* 1167 */     catch (SQLException sqlEx) {
/* 1168 */       checkAndFireConnectionError(sqlEx);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setObject(String parameterName, Object x) throws SQLException {
/*      */     try {
/* 1180 */       if (this.wrappedStmt != null) {
/* 1181 */         ((CallableStatement)this.wrappedStmt).setObject(parameterName, x);
/*      */       } else {
/*      */         
/* 1184 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/* 1188 */     catch (SQLException sqlEx) {
/* 1189 */       checkAndFireConnectionError(sqlEx);
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
/*      */   public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
/*      */     try {
/* 1202 */       if (this.wrappedStmt != null) {
/* 1203 */         ((CallableStatement)this.wrappedStmt).setCharacterStream(parameterName, reader, length);
/*      */       } else {
/*      */         
/* 1206 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/* 1210 */     catch (SQLException sqlEx) {
/* 1211 */       checkAndFireConnectionError(sqlEx);
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
/*      */   public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
/*      */     try {
/* 1224 */       if (this.wrappedStmt != null) {
/* 1225 */         ((CallableStatement)this.wrappedStmt).setDate(parameterName, x, cal);
/*      */       } else {
/*      */         
/* 1228 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/* 1232 */     catch (SQLException sqlEx) {
/* 1233 */       checkAndFireConnectionError(sqlEx);
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
/*      */   public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
/*      */     try {
/* 1246 */       if (this.wrappedStmt != null) {
/* 1247 */         ((CallableStatement)this.wrappedStmt).setTime(parameterName, x, cal);
/*      */       } else {
/*      */         
/* 1250 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/* 1254 */     catch (SQLException sqlEx) {
/* 1255 */       checkAndFireConnectionError(sqlEx);
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
/*      */   public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
/*      */     try {
/* 1268 */       if (this.wrappedStmt != null) {
/* 1269 */         ((CallableStatement)this.wrappedStmt).setTimestamp(parameterName, x, cal);
/*      */       } else {
/*      */         
/* 1272 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/* 1276 */     catch (SQLException sqlEx) {
/* 1277 */       checkAndFireConnectionError(sqlEx);
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
/*      */   public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
/*      */     try {
/* 1290 */       if (this.wrappedStmt != null) {
/* 1291 */         ((CallableStatement)this.wrappedStmt).setNull(parameterName, sqlType, typeName);
/*      */       } else {
/*      */         
/* 1294 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */       }
/*      */     
/*      */     }
/* 1298 */     catch (SQLException sqlEx) {
/* 1299 */       checkAndFireConnectionError(sqlEx);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getString(String parameterName) throws SQLException {
/*      */     try {
/* 1310 */       if (this.wrappedStmt != null) {
/* 1311 */         return ((CallableStatement)this.wrappedStmt).getString(parameterName);
/*      */       }
/*      */       
/* 1314 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/* 1318 */     catch (SQLException sqlEx) {
/* 1319 */       checkAndFireConnectionError(sqlEx);
/*      */       
/* 1321 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getBoolean(String parameterName) throws SQLException {
/*      */     try {
/* 1331 */       if (this.wrappedStmt != null) {
/* 1332 */         return ((CallableStatement)this.wrappedStmt).getBoolean(parameterName);
/*      */       }
/*      */       
/* 1335 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/* 1339 */     catch (SQLException sqlEx) {
/* 1340 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/* 1343 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getByte(String parameterName) throws SQLException {
/*      */     try {
/* 1353 */       if (this.wrappedStmt != null) {
/* 1354 */         return ((CallableStatement)this.wrappedStmt).getByte(parameterName);
/*      */       }
/*      */       
/* 1357 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/* 1361 */     catch (SQLException sqlEx) {
/* 1362 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/* 1365 */       return 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short getShort(String parameterName) throws SQLException {
/*      */     try {
/* 1375 */       if (this.wrappedStmt != null) {
/* 1376 */         return ((CallableStatement)this.wrappedStmt).getShort(parameterName);
/*      */       }
/*      */       
/* 1379 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/* 1383 */     catch (SQLException sqlEx) {
/* 1384 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/* 1387 */       return 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getInt(String parameterName) throws SQLException {
/*      */     try {
/* 1397 */       if (this.wrappedStmt != null) {
/* 1398 */         return ((CallableStatement)this.wrappedStmt).getInt(parameterName);
/*      */       }
/*      */       
/* 1401 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/* 1405 */     catch (SQLException sqlEx) {
/* 1406 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/* 1409 */       return 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLong(String parameterName) throws SQLException {
/*      */     try {
/* 1419 */       if (this.wrappedStmt != null) {
/* 1420 */         return ((CallableStatement)this.wrappedStmt).getLong(parameterName);
/*      */       }
/*      */       
/* 1423 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/* 1427 */     catch (SQLException sqlEx) {
/* 1428 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/* 1431 */       return 0L;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFloat(String parameterName) throws SQLException {
/*      */     try {
/* 1441 */       if (this.wrappedStmt != null) {
/* 1442 */         return ((CallableStatement)this.wrappedStmt).getFloat(parameterName);
/*      */       }
/*      */       
/* 1445 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/* 1449 */     catch (SQLException sqlEx) {
/* 1450 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/* 1453 */       return 0.0F;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getDouble(String parameterName) throws SQLException {
/*      */     try {
/* 1463 */       if (this.wrappedStmt != null) {
/* 1464 */         return ((CallableStatement)this.wrappedStmt).getDouble(parameterName);
/*      */       }
/*      */       
/* 1467 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/* 1471 */     catch (SQLException sqlEx) {
/* 1472 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/* 1475 */       return 0.0D;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getBytes(String parameterName) throws SQLException {
/*      */     try {
/* 1485 */       if (this.wrappedStmt != null) {
/* 1486 */         return ((CallableStatement)this.wrappedStmt).getBytes(parameterName);
/*      */       }
/*      */       
/* 1489 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/* 1493 */     catch (SQLException sqlEx) {
/* 1494 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/* 1497 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Date getDate(String parameterName) throws SQLException {
/*      */     try {
/* 1507 */       if (this.wrappedStmt != null) {
/* 1508 */         return ((CallableStatement)this.wrappedStmt).getDate(parameterName);
/*      */       }
/*      */       
/* 1511 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/* 1515 */     catch (SQLException sqlEx) {
/* 1516 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/* 1519 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Time getTime(String parameterName) throws SQLException {
/*      */     try {
/* 1529 */       if (this.wrappedStmt != null) {
/* 1530 */         return ((CallableStatement)this.wrappedStmt).getTime(parameterName);
/*      */       }
/*      */       
/* 1533 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/* 1537 */     catch (SQLException sqlEx) {
/* 1538 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/* 1541 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Timestamp getTimestamp(String parameterName) throws SQLException {
/*      */     try {
/* 1551 */       if (this.wrappedStmt != null) {
/* 1552 */         return ((CallableStatement)this.wrappedStmt).getTimestamp(parameterName);
/*      */       }
/*      */       
/* 1555 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/* 1559 */     catch (SQLException sqlEx) {
/* 1560 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/* 1563 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getObject(String parameterName) throws SQLException {
/*      */     try {
/* 1573 */       if (this.wrappedStmt != null) {
/* 1574 */         return ((CallableStatement)this.wrappedStmt).getObject(parameterName);
/*      */       }
/*      */       
/* 1577 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/* 1581 */     catch (SQLException sqlEx) {
/* 1582 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/* 1585 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BigDecimal getBigDecimal(String parameterName) throws SQLException {
/*      */     try {
/* 1595 */       if (this.wrappedStmt != null) {
/* 1596 */         return ((CallableStatement)this.wrappedStmt).getBigDecimal(parameterName);
/*      */       }
/*      */       
/* 1599 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/* 1603 */     catch (SQLException sqlEx) {
/* 1604 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/* 1607 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getObject(String parameterName, Map typeMap) throws SQLException {
/*      */     try {
/* 1618 */       if (this.wrappedStmt != null) {
/* 1619 */         return ((CallableStatement)this.wrappedStmt).getObject(parameterName, typeMap);
/*      */       }
/*      */       
/* 1622 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/* 1626 */     catch (SQLException sqlEx) {
/* 1627 */       checkAndFireConnectionError(sqlEx);
/*      */       
/* 1629 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Ref getRef(String parameterName) throws SQLException {
/*      */     try {
/* 1639 */       if (this.wrappedStmt != null) {
/* 1640 */         return ((CallableStatement)this.wrappedStmt).getRef(parameterName);
/*      */       }
/*      */       
/* 1643 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/* 1647 */     catch (SQLException sqlEx) {
/* 1648 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/* 1651 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Blob getBlob(String parameterName) throws SQLException {
/*      */     try {
/* 1661 */       if (this.wrappedStmt != null) {
/* 1662 */         return ((CallableStatement)this.wrappedStmt).getBlob(parameterName);
/*      */       }
/*      */       
/* 1665 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/* 1669 */     catch (SQLException sqlEx) {
/* 1670 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/* 1673 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Clob getClob(String parameterName) throws SQLException {
/*      */     try {
/* 1683 */       if (this.wrappedStmt != null) {
/* 1684 */         return ((CallableStatement)this.wrappedStmt).getClob(parameterName);
/*      */       }
/*      */       
/* 1687 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/* 1691 */     catch (SQLException sqlEx) {
/* 1692 */       checkAndFireConnectionError(sqlEx);
/*      */       
/* 1694 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Array getArray(String parameterName) throws SQLException {
/*      */     try {
/* 1704 */       if (this.wrappedStmt != null) {
/* 1705 */         return ((CallableStatement)this.wrappedStmt).getArray(parameterName);
/*      */       }
/*      */       
/* 1708 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/* 1712 */     catch (SQLException sqlEx) {
/* 1713 */       checkAndFireConnectionError(sqlEx);
/*      */       
/* 1715 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Date getDate(String parameterName, Calendar cal) throws SQLException {
/*      */     try {
/* 1725 */       if (this.wrappedStmt != null) {
/* 1726 */         return ((CallableStatement)this.wrappedStmt).getDate(parameterName, cal);
/*      */       }
/*      */       
/* 1729 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/* 1733 */     catch (SQLException sqlEx) {
/* 1734 */       checkAndFireConnectionError(sqlEx);
/*      */       
/* 1736 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Time getTime(String parameterName, Calendar cal) throws SQLException {
/*      */     try {
/* 1746 */       if (this.wrappedStmt != null) {
/* 1747 */         return ((CallableStatement)this.wrappedStmt).getTime(parameterName, cal);
/*      */       }
/*      */       
/* 1750 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/* 1754 */     catch (SQLException sqlEx) {
/* 1755 */       checkAndFireConnectionError(sqlEx);
/*      */       
/* 1757 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
/*      */     try {
/* 1768 */       if (this.wrappedStmt != null) {
/* 1769 */         return ((CallableStatement)this.wrappedStmt).getTimestamp(parameterName, cal);
/*      */       }
/*      */       
/* 1772 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/* 1776 */     catch (SQLException sqlEx) {
/* 1777 */       checkAndFireConnectionError(sqlEx);
/*      */       
/* 1779 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URL getURL(String parameterName) throws SQLException {
/*      */     try {
/* 1789 */       if (this.wrappedStmt != null) {
/* 1790 */         return ((CallableStatement)this.wrappedStmt).getURL(parameterName);
/*      */       }
/*      */       
/* 1793 */       throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*      */ 
/*      */     
/*      */     }
/* 1797 */     catch (SQLException sqlEx) {
/* 1798 */       checkAndFireConnectionError(sqlEx);
/*      */ 
/*      */       
/* 1801 */       return null;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\jdbc2\optional\CallableStatementWrapper.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */