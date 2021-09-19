/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.math.BigDecimal;
/*      */ import java.net.URL;
/*      */ import java.sql.Array;
/*      */ import java.sql.Blob;
/*      */ import java.sql.CallableStatement;
/*      */ import java.sql.Clob;
/*      */ import java.sql.DatabaseMetaData;
/*      */ import java.sql.Date;
/*      */ import java.sql.ParameterMetaData;
/*      */ import java.sql.Ref;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Statement;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
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
/*      */ public class CallableStatement
/*      */   extends PreparedStatement
/*      */   implements CallableStatement
/*      */ {
/*      */   protected static final Constructor JDBC_4_CSTMT_2_ARGS_CTOR;
/*      */   protected static final Constructor JDBC_4_CSTMT_4_ARGS_CTOR;
/*      */   private static final int NOT_OUTPUT_PARAMETER_INDICATOR = -2147483648;
/*      */   private static final String PARAMETER_NAMESPACE_PREFIX = "@com_mysql_jdbc_outparam_";
/*      */   
/*      */   static {
/*   64 */     if (Util.isJdbc4()) {
/*      */       try {
/*   66 */         JDBC_4_CSTMT_2_ARGS_CTOR = Class.forName("com.mysql.jdbc.JDBC4CallableStatement").getConstructor(new Class[] { ConnectionImpl.class, CallableStatementParamInfo.class });
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*   71 */         JDBC_4_CSTMT_4_ARGS_CTOR = Class.forName("com.mysql.jdbc.JDBC4CallableStatement").getConstructor(new Class[] { ConnectionImpl.class, String.class, String.class, boolean.class });
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*   77 */       catch (SecurityException e) {
/*   78 */         throw new RuntimeException(e);
/*   79 */       } catch (NoSuchMethodException e) {
/*   80 */         throw new RuntimeException(e);
/*   81 */       } catch (ClassNotFoundException e) {
/*   82 */         throw new RuntimeException(e);
/*      */       } 
/*      */     } else {
/*   85 */       JDBC_4_CSTMT_4_ARGS_CTOR = null;
/*   86 */       JDBC_4_CSTMT_2_ARGS_CTOR = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected class CallableStatementParam
/*      */   {
/*      */     int desiredJdbcType;
/*      */     
/*      */     int index;
/*      */     
/*      */     int inOutModifier;
/*      */     
/*      */     boolean isIn;
/*      */     
/*      */     boolean isOut;
/*      */     
/*      */     int jdbcType;
/*      */     
/*      */     short nullability;
/*      */     
/*      */     String paramName;
/*      */     
/*      */     int precision;
/*      */     int scale;
/*      */     String typeName;
/*      */     private final CallableStatement this$0;
/*      */     
/*      */     CallableStatementParam(CallableStatement this$0, String name, int idx, boolean in, boolean out, int jdbcType, String typeName, int precision, int scale, short nullability, int inOutModifier) {
/*  115 */       this.this$0 = this$0;
/*  116 */       this.paramName = name;
/*  117 */       this.isIn = in;
/*  118 */       this.isOut = out;
/*  119 */       this.index = idx;
/*      */       
/*  121 */       this.jdbcType = jdbcType;
/*  122 */       this.typeName = typeName;
/*  123 */       this.precision = precision;
/*  124 */       this.scale = scale;
/*  125 */       this.nullability = nullability;
/*  126 */       this.inOutModifier = inOutModifier;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected Object clone() throws CloneNotSupportedException {
/*  135 */       return super.clone();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected class CallableStatementParamInfo
/*      */   {
/*      */     String catalogInUse;
/*      */ 
/*      */     
/*      */     boolean isFunctionCall;
/*      */ 
/*      */     
/*      */     String nativeSql;
/*      */ 
/*      */     
/*      */     int numParameters;
/*      */ 
/*      */     
/*      */     List parameterList;
/*      */ 
/*      */     
/*      */     Map parameterMap;
/*      */ 
/*      */     
/*      */     boolean isReadOnlySafeProcedure;
/*      */ 
/*      */     
/*      */     boolean isReadOnlySafeChecked;
/*      */     
/*      */     private final CallableStatement this$0;
/*      */ 
/*      */     
/*      */     CallableStatementParamInfo(CallableStatement this$0, CallableStatementParamInfo fullParamInfo) {
/*  170 */       this.this$0 = this$0; this.isReadOnlySafeProcedure = false; this.isReadOnlySafeChecked = false;
/*  171 */       this.nativeSql = this$0.originalSql;
/*  172 */       this.catalogInUse = this$0.currentCatalog;
/*  173 */       this.isFunctionCall = fullParamInfo.isFunctionCall;
/*  174 */       int[] localParameterMap = this$0.placeholderToParameterIndexMap;
/*  175 */       int parameterMapLength = localParameterMap.length;
/*      */       
/*  177 */       this.isReadOnlySafeProcedure = fullParamInfo.isReadOnlySafeProcedure;
/*  178 */       this.isReadOnlySafeChecked = fullParamInfo.isReadOnlySafeChecked;
/*  179 */       this.parameterList = new ArrayList(fullParamInfo.numParameters);
/*  180 */       this.parameterMap = new HashMap(fullParamInfo.numParameters);
/*      */       
/*  182 */       if (this.isFunctionCall)
/*      */       {
/*  184 */         this.parameterList.add(fullParamInfo.parameterList.get(0));
/*      */       }
/*      */       
/*  187 */       int offset = this.isFunctionCall ? 1 : 0;
/*      */       
/*  189 */       for (int i = 0; i < parameterMapLength; i++) {
/*  190 */         if (localParameterMap[i] != 0) {
/*  191 */           CallableStatement.CallableStatementParam param = fullParamInfo.parameterList.get(localParameterMap[i] + offset);
/*      */           
/*  193 */           this.parameterList.add(param);
/*  194 */           this.parameterMap.put(param.paramName, param);
/*      */         } 
/*      */       } 
/*      */       
/*  198 */       this.numParameters = this.parameterList.size();
/*      */     }
/*      */     
/*      */     CallableStatementParamInfo(CallableStatement this$0, ResultSet paramTypesRs) throws SQLException {
/*  202 */       this.this$0 = this$0; this.isReadOnlySafeProcedure = false; this.isReadOnlySafeChecked = false;
/*  203 */       boolean hadRows = paramTypesRs.last();
/*      */       
/*  205 */       this.nativeSql = this$0.originalSql;
/*  206 */       this.catalogInUse = this$0.currentCatalog;
/*  207 */       this.isFunctionCall = this$0.callingStoredFunction;
/*      */       
/*  209 */       if (hadRows) {
/*  210 */         this.numParameters = paramTypesRs.getRow();
/*      */         
/*  212 */         this.parameterList = new ArrayList(this.numParameters);
/*  213 */         this.parameterMap = new HashMap(this.numParameters);
/*      */         
/*  215 */         paramTypesRs.beforeFirst();
/*      */         
/*  217 */         addParametersFromDBMD(paramTypesRs);
/*      */       } else {
/*  219 */         this.numParameters = 0;
/*      */       } 
/*      */       
/*  222 */       if (this.isFunctionCall) {
/*  223 */         this.numParameters++;
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     private void addParametersFromDBMD(ResultSet paramTypesRs) throws SQLException {
/*  229 */       int i = 0;
/*      */       
/*  231 */       while (paramTypesRs.next()) {
/*  232 */         String paramName = paramTypesRs.getString(4);
/*  233 */         int inOutModifier = paramTypesRs.getInt(5);
/*      */         
/*  235 */         boolean isOutParameter = false;
/*  236 */         boolean isInParameter = false;
/*      */         
/*  238 */         if (i == 0 && this.isFunctionCall) {
/*  239 */           isOutParameter = true;
/*  240 */           isInParameter = false;
/*  241 */         } else if (inOutModifier == 2) {
/*  242 */           isOutParameter = true;
/*  243 */           isInParameter = true;
/*  244 */         } else if (inOutModifier == 1) {
/*  245 */           isOutParameter = false;
/*  246 */           isInParameter = true;
/*  247 */         } else if (inOutModifier == 4) {
/*  248 */           isOutParameter = true;
/*  249 */           isInParameter = false;
/*      */         } 
/*      */         
/*  252 */         int jdbcType = paramTypesRs.getInt(6);
/*  253 */         String typeName = paramTypesRs.getString(7);
/*  254 */         int precision = paramTypesRs.getInt(8);
/*  255 */         int scale = paramTypesRs.getInt(10);
/*  256 */         short nullability = paramTypesRs.getShort(12);
/*      */         
/*  258 */         CallableStatement.CallableStatementParam paramInfoToAdd = new CallableStatement.CallableStatementParam(this.this$0, paramName, i++, isInParameter, isOutParameter, jdbcType, typeName, precision, scale, nullability, inOutModifier);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  263 */         this.parameterList.add(paramInfoToAdd);
/*  264 */         this.parameterMap.put(paramName, paramInfoToAdd);
/*      */       } 
/*      */     }
/*      */     
/*      */     protected void checkBounds(int paramIndex) throws SQLException {
/*  269 */       int localParamIndex = paramIndex - 1;
/*      */       
/*  271 */       if (paramIndex < 0 || localParamIndex >= this.numParameters) {
/*  272 */         throw SQLError.createSQLException(Messages.getString("CallableStatement.11") + paramIndex + Messages.getString("CallableStatement.12") + this.numParameters + Messages.getString("CallableStatement.13"), "S1009", this.this$0.getExceptionInterceptor());
/*      */       }
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
/*      */     protected Object clone() throws CloneNotSupportedException {
/*  285 */       return super.clone();
/*      */     }
/*      */     
/*      */     CallableStatement.CallableStatementParam getParameter(int index) {
/*  289 */       return this.parameterList.get(index);
/*      */     }
/*      */     
/*      */     CallableStatement.CallableStatementParam getParameter(String name) {
/*  293 */       return (CallableStatement.CallableStatementParam)this.parameterMap.get(name);
/*      */     }
/*      */     
/*      */     public String getParameterClassName(int arg0) throws SQLException {
/*  297 */       String mysqlTypeName = getParameterTypeName(arg0);
/*      */       
/*  299 */       boolean isBinaryOrBlob = (StringUtils.indexOfIgnoreCase(mysqlTypeName, "BLOB") != -1 || StringUtils.indexOfIgnoreCase(mysqlTypeName, "BINARY") != -1);
/*      */ 
/*      */       
/*  302 */       boolean isUnsigned = (StringUtils.indexOfIgnoreCase(mysqlTypeName, "UNSIGNED") != -1);
/*      */       
/*  304 */       int mysqlTypeIfKnown = 0;
/*      */       
/*  306 */       if (StringUtils.startsWithIgnoreCase(mysqlTypeName, "MEDIUMINT")) {
/*  307 */         mysqlTypeIfKnown = 9;
/*      */       }
/*      */       
/*  310 */       return ResultSetMetaData.getClassNameForJavaType(getParameterType(arg0), isUnsigned, mysqlTypeIfKnown, isBinaryOrBlob, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public int getParameterCount() throws SQLException {
/*  315 */       if (this.parameterList == null) {
/*  316 */         return 0;
/*      */       }
/*      */       
/*  319 */       return this.parameterList.size();
/*      */     }
/*      */     
/*      */     public int getParameterMode(int arg0) throws SQLException {
/*  323 */       checkBounds(arg0);
/*      */       
/*  325 */       return (getParameter(arg0 - 1)).inOutModifier;
/*      */     }
/*      */     
/*      */     public int getParameterType(int arg0) throws SQLException {
/*  329 */       checkBounds(arg0);
/*      */       
/*  331 */       return (getParameter(arg0 - 1)).jdbcType;
/*      */     }
/*      */     
/*      */     public String getParameterTypeName(int arg0) throws SQLException {
/*  335 */       checkBounds(arg0);
/*      */       
/*  337 */       return (getParameter(arg0 - 1)).typeName;
/*      */     }
/*      */     
/*      */     public int getPrecision(int arg0) throws SQLException {
/*  341 */       checkBounds(arg0);
/*      */       
/*  343 */       return (getParameter(arg0 - 1)).precision;
/*      */     }
/*      */     
/*      */     public int getScale(int arg0) throws SQLException {
/*  347 */       checkBounds(arg0);
/*      */       
/*  349 */       return (getParameter(arg0 - 1)).scale;
/*      */     }
/*      */     
/*      */     public int isNullable(int arg0) throws SQLException {
/*  353 */       checkBounds(arg0);
/*      */       
/*  355 */       return (getParameter(arg0 - 1)).nullability;
/*      */     }
/*      */     
/*      */     public boolean isSigned(int arg0) throws SQLException {
/*  359 */       checkBounds(arg0);
/*      */       
/*  361 */       return false;
/*      */     }
/*      */     
/*      */     Iterator iterator() {
/*  365 */       return this.parameterList.iterator();
/*      */     }
/*      */     
/*      */     int numberOfParameters() {
/*  369 */       return this.numParameters;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected class CallableStatementParamInfoJDBC3
/*      */     extends CallableStatementParamInfo
/*      */     implements ParameterMetaData
/*      */   {
/*      */     private final CallableStatement this$0;
/*      */ 
/*      */ 
/*      */     
/*      */     CallableStatementParamInfoJDBC3(CallableStatement this$0, ResultSet paramTypesRs) throws SQLException {
/*  384 */       super(this$0, paramTypesRs);
/*      */       this.this$0 = this$0;
/*      */     }
/*      */     public CallableStatementParamInfoJDBC3(CallableStatement this$0, CallableStatement.CallableStatementParamInfo paramInfo) {
/*  388 */       super(this$0, paramInfo);
/*      */       this.this$0 = this$0;
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
/*      */     public boolean isWrapperFor(Class iface) throws SQLException {
/*  407 */       this.this$0.checkClosed();
/*      */ 
/*      */ 
/*      */       
/*  411 */       return iface.isInstance(this);
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
/*      */     public Object unwrap(Class iface) throws SQLException {
/*      */       try {
/*  432 */         return Util.cast(iface, this);
/*  433 */       } catch (ClassCastException cce) {
/*  434 */         throw SQLError.createSQLException("Unable to unwrap to " + iface.toString(), "S1009", this.this$0.getExceptionInterceptor());
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String mangleParameterName(String origParameterName) {
/*  445 */     if (origParameterName == null) {
/*  446 */       return null;
/*      */     }
/*      */     
/*  449 */     int offset = 0;
/*      */     
/*  451 */     if (origParameterName.length() > 0 && origParameterName.charAt(0) == '@')
/*      */     {
/*  453 */       offset = 1;
/*      */     }
/*      */     
/*  456 */     StringBuffer paramNameBuf = new StringBuffer("@com_mysql_jdbc_outparam_".length() + origParameterName.length());
/*      */ 
/*      */     
/*  459 */     paramNameBuf.append("@com_mysql_jdbc_outparam_");
/*  460 */     paramNameBuf.append(origParameterName.substring(offset));
/*      */     
/*  462 */     return paramNameBuf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean callingStoredFunction = false;
/*      */ 
/*      */   
/*      */   private ResultSetInternalMethods functionReturnValueResults;
/*      */ 
/*      */   
/*      */   private boolean hasOutputParams = false;
/*      */ 
/*      */   
/*      */   private ResultSetInternalMethods outputParameterResults;
/*      */ 
/*      */   
/*      */   protected boolean outputParamWasNull = false;
/*      */ 
/*      */   
/*      */   private int[] parameterIndexToRsIndex;
/*      */ 
/*      */   
/*      */   protected CallableStatementParamInfo paramInfo;
/*      */ 
/*      */   
/*      */   private CallableStatementParam returnValueParam;
/*      */ 
/*      */   
/*      */   private int[] placeholderToParameterIndexMap;
/*      */ 
/*      */ 
/*      */   
/*      */   public CallableStatement(ConnectionImpl conn, CallableStatementParamInfo paramInfo) throws SQLException {
/*  496 */     super(conn, paramInfo.nativeSql, paramInfo.catalogInUse);
/*      */     
/*  498 */     this.paramInfo = paramInfo;
/*  499 */     this.callingStoredFunction = this.paramInfo.isFunctionCall;
/*      */     
/*  501 */     if (this.callingStoredFunction) {
/*  502 */       this.parameterCount++;
/*      */     }
/*      */     
/*  505 */     this.retrieveGeneratedKeys = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static CallableStatement getInstance(ConnectionImpl conn, String sql, String catalog, boolean isFunctionCall) throws SQLException {
/*  517 */     if (!Util.isJdbc4()) {
/*  518 */       return new CallableStatement(conn, sql, catalog, isFunctionCall);
/*      */     }
/*      */     
/*  521 */     return (CallableStatement)Util.handleNewInstance(JDBC_4_CSTMT_4_ARGS_CTOR, new Object[] { conn, sql, catalog, Boolean.valueOf(isFunctionCall) }, conn.getExceptionInterceptor());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static CallableStatement getInstance(ConnectionImpl conn, CallableStatementParamInfo paramInfo) throws SQLException {
/*  535 */     if (!Util.isJdbc4()) {
/*  536 */       return new CallableStatement(conn, paramInfo);
/*      */     }
/*      */     
/*  539 */     return (CallableStatement)Util.handleNewInstance(JDBC_4_CSTMT_2_ARGS_CTOR, new Object[] { conn, paramInfo }, conn.getExceptionInterceptor());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateParameterMap() throws SQLException {
/*  547 */     if (this.paramInfo == null) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  555 */     int parameterCountFromMetaData = this.paramInfo.getParameterCount();
/*      */ 
/*      */ 
/*      */     
/*  559 */     if (this.callingStoredFunction) {
/*  560 */       parameterCountFromMetaData--;
/*      */     }
/*      */     
/*  563 */     if (this.paramInfo != null && this.parameterCount != parameterCountFromMetaData) {
/*      */       
/*  565 */       this.placeholderToParameterIndexMap = new int[this.parameterCount];
/*      */       
/*  567 */       int startPos = this.callingStoredFunction ? StringUtils.indexOfIgnoreCase(this.originalSql, "SELECT") : StringUtils.indexOfIgnoreCase(this.originalSql, "CALL");
/*      */ 
/*      */       
/*  570 */       if (startPos != -1) {
/*  571 */         int parenOpenPos = this.originalSql.indexOf('(', startPos + 4);
/*      */         
/*  573 */         if (parenOpenPos != -1) {
/*  574 */           int parenClosePos = StringUtils.indexOfIgnoreCaseRespectQuotes(parenOpenPos, this.originalSql, ")", '\'', true);
/*      */ 
/*      */           
/*  577 */           if (parenClosePos != -1) {
/*  578 */             List parsedParameters = StringUtils.split(this.originalSql.substring(parenOpenPos + 1, parenClosePos), ",", "'\"", "'\"", true);
/*      */             
/*  580 */             int numParsedParameters = parsedParameters.size();
/*      */ 
/*      */ 
/*      */             
/*  584 */             if (numParsedParameters != this.parameterCount);
/*      */ 
/*      */ 
/*      */             
/*  588 */             int placeholderCount = 0;
/*      */             
/*  590 */             for (int i = 0; i < numParsedParameters; i++) {
/*  591 */               if (((String)parsedParameters.get(i)).equals("?")) {
/*  592 */                 this.placeholderToParameterIndexMap[placeholderCount++] = i;
/*      */               }
/*      */             } 
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
/*      */   public CallableStatement(ConnectionImpl conn, String sql, String catalog, boolean isFunctionCall) throws SQLException {
/*  616 */     super(conn, sql, catalog);
/*      */     
/*  618 */     this.callingStoredFunction = isFunctionCall;
/*      */     
/*  620 */     if (!this.callingStoredFunction) {
/*  621 */       if (!StringUtils.startsWithIgnoreCaseAndWs(sql, "CALL")) {
/*      */         
/*  623 */         fakeParameterTypes(false);
/*      */       } else {
/*  625 */         determineParameterTypes();
/*      */       } 
/*      */       
/*  628 */       generateParameterMap();
/*      */     } else {
/*  630 */       determineParameterTypes();
/*  631 */       generateParameterMap();
/*      */       
/*  633 */       this.parameterCount++;
/*      */     } 
/*      */     
/*  636 */     this.retrieveGeneratedKeys = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addBatch() throws SQLException {
/*  645 */     setOutParams();
/*      */     
/*  647 */     super.addBatch();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private CallableStatementParam checkIsOutputParam(int paramIndex) throws SQLException {
/*  653 */     if (this.callingStoredFunction) {
/*  654 */       if (paramIndex == 1) {
/*      */         
/*  656 */         if (this.returnValueParam == null) {
/*  657 */           this.returnValueParam = new CallableStatementParam(this, "", 0, false, true, 12, "VARCHAR", 0, 0, (short)2, 5);
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  663 */         return this.returnValueParam;
/*      */       } 
/*      */ 
/*      */       
/*  667 */       paramIndex--;
/*      */     } 
/*      */     
/*  670 */     checkParameterIndexBounds(paramIndex);
/*      */     
/*  672 */     int localParamIndex = paramIndex - 1;
/*      */     
/*  674 */     if (this.placeholderToParameterIndexMap != null) {
/*  675 */       localParamIndex = this.placeholderToParameterIndexMap[localParamIndex];
/*      */     }
/*      */     
/*  678 */     CallableStatementParam paramDescriptor = this.paramInfo.getParameter(localParamIndex);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  684 */     if (this.connection.getNoAccessToProcedureBodies()) {
/*  685 */       paramDescriptor.isOut = true;
/*  686 */       paramDescriptor.isIn = true;
/*  687 */       paramDescriptor.inOutModifier = 2;
/*  688 */     } else if (!paramDescriptor.isOut) {
/*  689 */       throw SQLError.createSQLException(Messages.getString("CallableStatement.9") + paramIndex + Messages.getString("CallableStatement.10"), "S1009", getExceptionInterceptor());
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  695 */     this.hasOutputParams = true;
/*      */     
/*  697 */     return paramDescriptor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkParameterIndexBounds(int paramIndex) throws SQLException {
/*  708 */     this.paramInfo.checkBounds(paramIndex);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkStreamability() throws SQLException {
/*  720 */     if (this.hasOutputParams && createStreamingResultSet()) {
/*  721 */       throw SQLError.createSQLException(Messages.getString("CallableStatement.14"), "S1C00", getExceptionInterceptor());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void clearParameters() throws SQLException {
/*  727 */     super.clearParameters();
/*      */     
/*      */     try {
/*  730 */       if (this.outputParameterResults != null) {
/*  731 */         this.outputParameterResults.close();
/*      */       }
/*      */     } finally {
/*  734 */       this.outputParameterResults = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fakeParameterTypes(boolean isReallyProcedure) throws SQLException {
/*  745 */     Field[] fields = new Field[13];
/*      */     
/*  747 */     fields[0] = new Field("", "PROCEDURE_CAT", 1, 0);
/*  748 */     fields[1] = new Field("", "PROCEDURE_SCHEM", 1, 0);
/*  749 */     fields[2] = new Field("", "PROCEDURE_NAME", 1, 0);
/*  750 */     fields[3] = new Field("", "COLUMN_NAME", 1, 0);
/*  751 */     fields[4] = new Field("", "COLUMN_TYPE", 1, 0);
/*  752 */     fields[5] = new Field("", "DATA_TYPE", 5, 0);
/*  753 */     fields[6] = new Field("", "TYPE_NAME", 1, 0);
/*  754 */     fields[7] = new Field("", "PRECISION", 4, 0);
/*  755 */     fields[8] = new Field("", "LENGTH", 4, 0);
/*  756 */     fields[9] = new Field("", "SCALE", 5, 0);
/*  757 */     fields[10] = new Field("", "RADIX", 5, 0);
/*  758 */     fields[11] = new Field("", "NULLABLE", 5, 0);
/*  759 */     fields[12] = new Field("", "REMARKS", 1, 0);
/*      */     
/*  761 */     String procName = isReallyProcedure ? extractProcedureName() : null;
/*      */     
/*  763 */     byte[] procNameAsBytes = null;
/*      */     
/*      */     try {
/*  766 */       procNameAsBytes = (procName == null) ? null : procName.getBytes("UTF-8");
/*  767 */     } catch (UnsupportedEncodingException ueEx) {
/*  768 */       procNameAsBytes = StringUtils.s2b(procName, this.connection);
/*      */     } 
/*      */     
/*  771 */     ArrayList resultRows = new ArrayList();
/*      */     
/*  773 */     for (int i = 0; i < this.parameterCount; i++) {
/*  774 */       byte[][] row = new byte[13][];
/*  775 */       row[0] = null;
/*  776 */       row[1] = null;
/*  777 */       row[2] = procNameAsBytes;
/*  778 */       row[3] = StringUtils.s2b(String.valueOf(i), this.connection);
/*      */       
/*  780 */       row[4] = StringUtils.s2b(String.valueOf(1), this.connection);
/*      */ 
/*      */ 
/*      */       
/*  784 */       row[5] = StringUtils.s2b(String.valueOf(12), this.connection);
/*      */       
/*  786 */       row[6] = StringUtils.s2b("VARCHAR", this.connection);
/*  787 */       row[7] = StringUtils.s2b(Integer.toString(65535), this.connection);
/*  788 */       row[8] = StringUtils.s2b(Integer.toString(65535), this.connection);
/*  789 */       row[9] = StringUtils.s2b(Integer.toString(0), this.connection);
/*  790 */       row[10] = StringUtils.s2b(Integer.toString(10), this.connection);
/*      */       
/*  792 */       row[11] = StringUtils.s2b(Integer.toString(2), this.connection);
/*      */ 
/*      */ 
/*      */       
/*  796 */       row[12] = null;
/*      */       
/*  798 */       resultRows.add(new ByteArrayRow(row, getExceptionInterceptor()));
/*      */     } 
/*      */     
/*  801 */     ResultSet paramTypesRs = DatabaseMetaData.buildResultSet(fields, resultRows, this.connection);
/*      */ 
/*      */     
/*  804 */     convertGetProcedureColumnsToInternalDescriptors(paramTypesRs);
/*      */   }
/*      */   
/*      */   private void determineParameterTypes() throws SQLException {
/*  808 */     if (this.connection.getNoAccessToProcedureBodies()) {
/*  809 */       fakeParameterTypes(true);
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  814 */     ResultSet paramTypesRs = null;
/*      */     
/*      */     try {
/*  817 */       String procName = extractProcedureName();
/*      */       
/*  819 */       DatabaseMetaData dbmd = this.connection.getMetaData();
/*      */       
/*  821 */       boolean useCatalog = false;
/*      */       
/*  823 */       if (procName.indexOf(".") == -1) {
/*  824 */         useCatalog = true;
/*      */       }
/*      */       
/*  827 */       paramTypesRs = dbmd.getProcedureColumns((this.connection.versionMeetsMinimum(5, 0, 2) && useCatalog) ? this.currentCatalog : null, null, procName, "%");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  832 */       convertGetProcedureColumnsToInternalDescriptors(paramTypesRs);
/*      */     } finally {
/*  834 */       SQLException sqlExRethrow = null;
/*      */       
/*  836 */       if (paramTypesRs != null) {
/*      */         try {
/*  838 */           paramTypesRs.close();
/*  839 */         } catch (SQLException sqlEx) {
/*  840 */           sqlExRethrow = sqlEx;
/*      */         } 
/*      */         
/*  843 */         paramTypesRs = null;
/*      */       } 
/*      */       
/*  846 */       if (sqlExRethrow != null) {
/*  847 */         throw sqlExRethrow;
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void convertGetProcedureColumnsToInternalDescriptors(ResultSet paramTypesRs) throws SQLException {
/*  853 */     if (!this.connection.isRunningOnJDK13()) {
/*  854 */       this.paramInfo = new CallableStatementParamInfoJDBC3(this, paramTypesRs);
/*      */     } else {
/*      */       
/*  857 */       this.paramInfo = new CallableStatementParamInfo(this, paramTypesRs);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean execute() throws SQLException {
/*  867 */     boolean returnVal = false;
/*      */     
/*  869 */     checkClosed();
/*      */     
/*  871 */     checkStreamability();
/*      */     
/*  873 */     synchronized (this.connection.getMutex()) {
/*  874 */       setInOutParamsOnServer();
/*  875 */       setOutParams();
/*      */       
/*  877 */       returnVal = super.execute();
/*      */       
/*  879 */       if (this.callingStoredFunction) {
/*  880 */         this.functionReturnValueResults = this.results;
/*  881 */         this.functionReturnValueResults.next();
/*  882 */         this.results = null;
/*      */       } 
/*      */       
/*  885 */       retrieveOutParams();
/*      */     } 
/*      */     
/*  888 */     if (!this.callingStoredFunction) {
/*  889 */       return returnVal;
/*      */     }
/*      */ 
/*      */     
/*  893 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet executeQuery() throws SQLException {
/*  902 */     checkClosed();
/*      */     
/*  904 */     checkStreamability();
/*      */     
/*  906 */     ResultSet execResults = null;
/*      */     
/*  908 */     synchronized (this.connection.getMutex()) {
/*  909 */       setInOutParamsOnServer();
/*  910 */       setOutParams();
/*      */       
/*  912 */       execResults = super.executeQuery();
/*      */       
/*  914 */       retrieveOutParams();
/*      */     } 
/*      */     
/*  917 */     return execResults;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int executeUpdate() throws SQLException {
/*  926 */     int returnVal = -1;
/*      */     
/*  928 */     checkClosed();
/*      */     
/*  930 */     checkStreamability();
/*      */     
/*  932 */     if (this.callingStoredFunction) {
/*  933 */       execute();
/*      */       
/*  935 */       return -1;
/*      */     } 
/*      */     
/*  938 */     synchronized (this.connection.getMutex()) {
/*  939 */       setInOutParamsOnServer();
/*  940 */       setOutParams();
/*      */       
/*  942 */       returnVal = super.executeUpdate();
/*      */       
/*  944 */       retrieveOutParams();
/*      */     } 
/*      */     
/*  947 */     return returnVal;
/*      */   }
/*      */   
/*      */   private String extractProcedureName() throws SQLException {
/*  951 */     String sanitizedSql = StringUtils.stripComments(this.originalSql, "`\"'", "`\"'", true, false, true, true);
/*      */ 
/*      */ 
/*      */     
/*  955 */     int endCallIndex = StringUtils.indexOfIgnoreCase(sanitizedSql, "CALL ");
/*      */     
/*  957 */     int offset = 5;
/*      */     
/*  959 */     if (endCallIndex == -1) {
/*  960 */       endCallIndex = StringUtils.indexOfIgnoreCase(sanitizedSql, "SELECT ");
/*      */       
/*  962 */       offset = 7;
/*      */     } 
/*      */     
/*  965 */     if (endCallIndex != -1) {
/*  966 */       StringBuffer nameBuf = new StringBuffer();
/*      */       
/*  968 */       String trimmedStatement = sanitizedSql.substring(endCallIndex + offset).trim();
/*      */ 
/*      */       
/*  971 */       int statementLength = trimmedStatement.length();
/*      */       
/*  973 */       for (int i = 0; i < statementLength; i++) {
/*  974 */         char c = trimmedStatement.charAt(i);
/*      */         
/*  976 */         if (Character.isWhitespace(c) || c == '(' || c == '?') {
/*      */           break;
/*      */         }
/*  979 */         nameBuf.append(c);
/*      */       } 
/*      */ 
/*      */       
/*  983 */       return nameBuf.toString();
/*      */     } 
/*      */     
/*  986 */     throw SQLError.createSQLException(Messages.getString("CallableStatement.1"), "S1000", getExceptionInterceptor());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String fixParameterName(String paramNameIn) throws SQLException {
/* 1002 */     if (paramNameIn == null || paramNameIn.length() == 0) {
/* 1003 */       throw SQLError.createSQLException((Messages.getString("CallableStatement.0") + paramNameIn == null) ? Messages.getString("CallableStatement.15") : Messages.getString("CallableStatement.16"), "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1009 */     if (this.connection.getNoAccessToProcedureBodies()) {
/* 1010 */       throw SQLError.createSQLException("No access to parameters by name when connection has been configured not to access procedure bodies", "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */     
/* 1014 */     return mangleParameterName(paramNameIn);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Array getArray(int i) throws SQLException {
/* 1029 */     ResultSetInternalMethods rs = getOutputParameters(i);
/*      */     
/* 1031 */     Array retValue = rs.getArray(mapOutputParameterIndexToRsIndex(i));
/*      */     
/* 1033 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1035 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Array getArray(String parameterName) throws SQLException {
/* 1043 */     ResultSetInternalMethods rs = getOutputParameters(0);
/*      */ 
/*      */     
/* 1046 */     Array retValue = rs.getArray(fixParameterName(parameterName));
/*      */     
/* 1048 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1050 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
/* 1058 */     ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
/*      */     
/* 1060 */     BigDecimal retValue = rs.getBigDecimal(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */ 
/*      */     
/* 1063 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1065 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
/* 1086 */     ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
/*      */     
/* 1088 */     BigDecimal retValue = rs.getBigDecimal(mapOutputParameterIndexToRsIndex(parameterIndex), scale);
/*      */ 
/*      */     
/* 1091 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1093 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized BigDecimal getBigDecimal(String parameterName) throws SQLException {
/* 1101 */     ResultSetInternalMethods rs = getOutputParameters(0);
/*      */ 
/*      */     
/* 1104 */     BigDecimal retValue = rs.getBigDecimal(fixParameterName(parameterName));
/*      */     
/* 1106 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1108 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Blob getBlob(int parameterIndex) throws SQLException {
/* 1115 */     ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
/*      */     
/* 1117 */     Blob retValue = rs.getBlob(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */ 
/*      */     
/* 1120 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1122 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Blob getBlob(String parameterName) throws SQLException {
/* 1129 */     ResultSetInternalMethods rs = getOutputParameters(0);
/*      */ 
/*      */     
/* 1132 */     Blob retValue = rs.getBlob(fixParameterName(parameterName));
/*      */     
/* 1134 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1136 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized boolean getBoolean(int parameterIndex) throws SQLException {
/* 1144 */     ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
/*      */     
/* 1146 */     boolean retValue = rs.getBoolean(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */ 
/*      */     
/* 1149 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1151 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized boolean getBoolean(String parameterName) throws SQLException {
/* 1159 */     ResultSetInternalMethods rs = getOutputParameters(0);
/*      */ 
/*      */     
/* 1162 */     boolean retValue = rs.getBoolean(fixParameterName(parameterName));
/*      */     
/* 1164 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1166 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized byte getByte(int parameterIndex) throws SQLException {
/* 1173 */     ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
/*      */     
/* 1175 */     byte retValue = rs.getByte(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */ 
/*      */     
/* 1178 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1180 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized byte getByte(String parameterName) throws SQLException {
/* 1187 */     ResultSetInternalMethods rs = getOutputParameters(0);
/*      */ 
/*      */     
/* 1190 */     byte retValue = rs.getByte(fixParameterName(parameterName));
/*      */     
/* 1192 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1194 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized byte[] getBytes(int parameterIndex) throws SQLException {
/* 1201 */     ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
/*      */     
/* 1203 */     byte[] retValue = rs.getBytes(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */ 
/*      */     
/* 1206 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1208 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized byte[] getBytes(String parameterName) throws SQLException {
/* 1216 */     ResultSetInternalMethods rs = getOutputParameters(0);
/*      */ 
/*      */     
/* 1219 */     byte[] retValue = rs.getBytes(fixParameterName(parameterName));
/*      */     
/* 1221 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1223 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Clob getClob(int parameterIndex) throws SQLException {
/* 1230 */     ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
/*      */     
/* 1232 */     Clob retValue = rs.getClob(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */ 
/*      */     
/* 1235 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1237 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Clob getClob(String parameterName) throws SQLException {
/* 1244 */     ResultSetInternalMethods rs = getOutputParameters(0);
/*      */ 
/*      */     
/* 1247 */     Clob retValue = rs.getClob(fixParameterName(parameterName));
/*      */     
/* 1249 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1251 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Date getDate(int parameterIndex) throws SQLException {
/* 1258 */     ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
/*      */     
/* 1260 */     Date retValue = rs.getDate(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */ 
/*      */     
/* 1263 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1265 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Date getDate(int parameterIndex, Calendar cal) throws SQLException {
/* 1273 */     ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
/*      */     
/* 1275 */     Date retValue = rs.getDate(mapOutputParameterIndexToRsIndex(parameterIndex), cal);
/*      */ 
/*      */     
/* 1278 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1280 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Date getDate(String parameterName) throws SQLException {
/* 1287 */     ResultSetInternalMethods rs = getOutputParameters(0);
/*      */ 
/*      */     
/* 1290 */     Date retValue = rs.getDate(fixParameterName(parameterName));
/*      */     
/* 1292 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1294 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Date getDate(String parameterName, Calendar cal) throws SQLException {
/* 1303 */     ResultSetInternalMethods rs = getOutputParameters(0);
/*      */ 
/*      */     
/* 1306 */     Date retValue = rs.getDate(fixParameterName(parameterName), cal);
/*      */     
/* 1308 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1310 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized double getDouble(int parameterIndex) throws SQLException {
/* 1318 */     ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
/*      */     
/* 1320 */     double retValue = rs.getDouble(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */ 
/*      */     
/* 1323 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1325 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized double getDouble(String parameterName) throws SQLException {
/* 1333 */     ResultSetInternalMethods rs = getOutputParameters(0);
/*      */ 
/*      */     
/* 1336 */     double retValue = rs.getDouble(fixParameterName(parameterName));
/*      */     
/* 1338 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1340 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized float getFloat(int parameterIndex) throws SQLException {
/* 1347 */     ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
/*      */     
/* 1349 */     float retValue = rs.getFloat(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */ 
/*      */     
/* 1352 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1354 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized float getFloat(String parameterName) throws SQLException {
/* 1362 */     ResultSetInternalMethods rs = getOutputParameters(0);
/*      */ 
/*      */     
/* 1365 */     float retValue = rs.getFloat(fixParameterName(parameterName));
/*      */     
/* 1367 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1369 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized int getInt(int parameterIndex) throws SQLException {
/* 1376 */     ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
/*      */     
/* 1378 */     int retValue = rs.getInt(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */ 
/*      */     
/* 1381 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1383 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized int getInt(String parameterName) throws SQLException {
/* 1390 */     ResultSetInternalMethods rs = getOutputParameters(0);
/*      */ 
/*      */     
/* 1393 */     int retValue = rs.getInt(fixParameterName(parameterName));
/*      */     
/* 1395 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1397 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized long getLong(int parameterIndex) throws SQLException {
/* 1404 */     ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
/*      */     
/* 1406 */     long retValue = rs.getLong(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */ 
/*      */     
/* 1409 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1411 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized long getLong(String parameterName) throws SQLException {
/* 1418 */     ResultSetInternalMethods rs = getOutputParameters(0);
/*      */ 
/*      */     
/* 1421 */     long retValue = rs.getLong(fixParameterName(parameterName));
/*      */     
/* 1423 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1425 */     return retValue;
/*      */   }
/*      */ 
/*      */   
/*      */   protected int getNamedParamIndex(String paramName, boolean forOut) throws SQLException {
/* 1430 */     if (this.connection.getNoAccessToProcedureBodies()) {
/* 1431 */       throw SQLError.createSQLException("No access to parameters by name when connection has been configured not to access procedure bodies", "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */     
/* 1435 */     if (paramName == null || paramName.length() == 0) {
/* 1436 */       throw SQLError.createSQLException(Messages.getString("CallableStatement.2"), "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */     
/* 1440 */     if (this.paramInfo == null) {
/* 1441 */       throw SQLError.createSQLException(Messages.getString("CallableStatement.3") + paramName + Messages.getString("CallableStatement.4"), "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1446 */     CallableStatementParam namedParamInfo = this.paramInfo.getParameter(paramName);
/*      */ 
/*      */     
/* 1449 */     if (forOut && !namedParamInfo.isOut) {
/* 1450 */       throw SQLError.createSQLException(Messages.getString("CallableStatement.5") + paramName + Messages.getString("CallableStatement.6"), "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1457 */     if (this.placeholderToParameterIndexMap == null) {
/* 1458 */       return namedParamInfo.index + 1;
/*      */     }
/*      */     
/* 1461 */     for (int i = 0; i < this.placeholderToParameterIndexMap.length; i++) {
/* 1462 */       if (this.placeholderToParameterIndexMap[i] == namedParamInfo.index) {
/* 1463 */         return i + 1;
/*      */       }
/*      */     } 
/*      */     
/* 1467 */     throw SQLError.createSQLException("Can't find local placeholder mapping for parameter named \"" + paramName + "\".", "S1009", getExceptionInterceptor());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Object getObject(int parameterIndex) throws SQLException {
/* 1476 */     CallableStatementParam paramDescriptor = checkIsOutputParam(parameterIndex);
/*      */     
/* 1478 */     ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
/*      */     
/* 1480 */     Object retVal = rs.getObjectStoredProc(mapOutputParameterIndexToRsIndex(parameterIndex), paramDescriptor.desiredJdbcType);
/*      */ 
/*      */ 
/*      */     
/* 1484 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1486 */     return retVal;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Object getObject(int parameterIndex, Map map) throws SQLException {
/* 1494 */     ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
/*      */     
/* 1496 */     Object retVal = rs.getObject(mapOutputParameterIndexToRsIndex(parameterIndex), map);
/*      */ 
/*      */     
/* 1499 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1501 */     return retVal;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Object getObject(String parameterName) throws SQLException {
/* 1509 */     ResultSetInternalMethods rs = getOutputParameters(0);
/*      */ 
/*      */     
/* 1512 */     Object retValue = rs.getObject(fixParameterName(parameterName));
/*      */     
/* 1514 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1516 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Object getObject(String parameterName, Map map) throws SQLException {
/* 1525 */     ResultSetInternalMethods rs = getOutputParameters(0);
/*      */ 
/*      */     
/* 1528 */     Object retValue = rs.getObject(fixParameterName(parameterName), map);
/*      */     
/* 1530 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1532 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ResultSetInternalMethods getOutputParameters(int paramIndex) throws SQLException {
/* 1546 */     this.outputParamWasNull = false;
/*      */     
/* 1548 */     if (paramIndex == 1 && this.callingStoredFunction && this.returnValueParam != null)
/*      */     {
/* 1550 */       return this.functionReturnValueResults;
/*      */     }
/*      */     
/* 1553 */     if (this.outputParameterResults == null) {
/* 1554 */       if (this.paramInfo.numberOfParameters() == 0) {
/* 1555 */         throw SQLError.createSQLException(Messages.getString("CallableStatement.7"), "S1009", getExceptionInterceptor());
/*      */       }
/*      */ 
/*      */       
/* 1559 */       throw SQLError.createSQLException(Messages.getString("CallableStatement.8"), "S1000", getExceptionInterceptor());
/*      */     } 
/*      */ 
/*      */     
/* 1563 */     return this.outputParameterResults;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized ParameterMetaData getParameterMetaData() throws SQLException {
/* 1569 */     if (this.placeholderToParameterIndexMap == null) {
/* 1570 */       return (CallableStatementParamInfoJDBC3)this.paramInfo;
/*      */     }
/* 1572 */     return new CallableStatementParamInfoJDBC3(this, this.paramInfo);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Ref getRef(int parameterIndex) throws SQLException {
/* 1580 */     ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
/*      */     
/* 1582 */     Ref retValue = rs.getRef(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */ 
/*      */     
/* 1585 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1587 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Ref getRef(String parameterName) throws SQLException {
/* 1594 */     ResultSetInternalMethods rs = getOutputParameters(0);
/*      */ 
/*      */     
/* 1597 */     Ref retValue = rs.getRef(fixParameterName(parameterName));
/*      */     
/* 1599 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1601 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized short getShort(int parameterIndex) throws SQLException {
/* 1608 */     ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
/*      */     
/* 1610 */     short retValue = rs.getShort(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */ 
/*      */     
/* 1613 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1615 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized short getShort(String parameterName) throws SQLException {
/* 1623 */     ResultSetInternalMethods rs = getOutputParameters(0);
/*      */ 
/*      */     
/* 1626 */     short retValue = rs.getShort(fixParameterName(parameterName));
/*      */     
/* 1628 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1630 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized String getString(int parameterIndex) throws SQLException {
/* 1638 */     ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
/*      */     
/* 1640 */     String retValue = rs.getString(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */ 
/*      */     
/* 1643 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1645 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized String getString(String parameterName) throws SQLException {
/* 1653 */     ResultSetInternalMethods rs = getOutputParameters(0);
/*      */ 
/*      */     
/* 1656 */     String retValue = rs.getString(fixParameterName(parameterName));
/*      */     
/* 1658 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1660 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Time getTime(int parameterIndex) throws SQLException {
/* 1667 */     ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
/*      */     
/* 1669 */     Time retValue = rs.getTime(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */ 
/*      */     
/* 1672 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1674 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Time getTime(int parameterIndex, Calendar cal) throws SQLException {
/* 1682 */     ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
/*      */     
/* 1684 */     Time retValue = rs.getTime(mapOutputParameterIndexToRsIndex(parameterIndex), cal);
/*      */ 
/*      */     
/* 1687 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1689 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Time getTime(String parameterName) throws SQLException {
/* 1696 */     ResultSetInternalMethods rs = getOutputParameters(0);
/*      */ 
/*      */     
/* 1699 */     Time retValue = rs.getTime(fixParameterName(parameterName));
/*      */     
/* 1701 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1703 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Time getTime(String parameterName, Calendar cal) throws SQLException {
/* 1712 */     ResultSetInternalMethods rs = getOutputParameters(0);
/*      */ 
/*      */     
/* 1715 */     Time retValue = rs.getTime(fixParameterName(parameterName), cal);
/*      */     
/* 1717 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1719 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Timestamp getTimestamp(int parameterIndex) throws SQLException {
/* 1727 */     ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
/*      */     
/* 1729 */     Timestamp retValue = rs.getTimestamp(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */ 
/*      */     
/* 1732 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1734 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
/* 1742 */     ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
/*      */     
/* 1744 */     Timestamp retValue = rs.getTimestamp(mapOutputParameterIndexToRsIndex(parameterIndex), cal);
/*      */ 
/*      */     
/* 1747 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1749 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Timestamp getTimestamp(String parameterName) throws SQLException {
/* 1757 */     ResultSetInternalMethods rs = getOutputParameters(0);
/*      */ 
/*      */     
/* 1760 */     Timestamp retValue = rs.getTimestamp(fixParameterName(parameterName));
/*      */     
/* 1762 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1764 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
/* 1773 */     ResultSetInternalMethods rs = getOutputParameters(0);
/*      */ 
/*      */     
/* 1776 */     Timestamp retValue = rs.getTimestamp(fixParameterName(parameterName), cal);
/*      */ 
/*      */     
/* 1779 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1781 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized URL getURL(int parameterIndex) throws SQLException {
/* 1788 */     ResultSetInternalMethods rs = getOutputParameters(parameterIndex);
/*      */     
/* 1790 */     URL retValue = rs.getURL(mapOutputParameterIndexToRsIndex(parameterIndex));
/*      */ 
/*      */     
/* 1793 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1795 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized URL getURL(String parameterName) throws SQLException {
/* 1802 */     ResultSetInternalMethods rs = getOutputParameters(0);
/*      */ 
/*      */     
/* 1805 */     URL retValue = rs.getURL(fixParameterName(parameterName));
/*      */     
/* 1807 */     this.outputParamWasNull = rs.wasNull();
/*      */     
/* 1809 */     return retValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected int mapOutputParameterIndexToRsIndex(int paramIndex) throws SQLException {
/* 1815 */     if (this.returnValueParam != null && paramIndex == 1) {
/* 1816 */       return 1;
/*      */     }
/*      */     
/* 1819 */     checkParameterIndexBounds(paramIndex);
/*      */     
/* 1821 */     int localParamIndex = paramIndex - 1;
/*      */     
/* 1823 */     if (this.placeholderToParameterIndexMap != null) {
/* 1824 */       localParamIndex = this.placeholderToParameterIndexMap[localParamIndex];
/*      */     }
/*      */     
/* 1827 */     int rsIndex = this.parameterIndexToRsIndex[localParamIndex];
/*      */     
/* 1829 */     if (rsIndex == Integer.MIN_VALUE) {
/* 1830 */       throw SQLError.createSQLException(Messages.getString("CallableStatement.21") + paramIndex + Messages.getString("CallableStatement.22"), "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1836 */     return rsIndex + 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
/* 1844 */     CallableStatementParam paramDescriptor = checkIsOutputParam(parameterIndex);
/* 1845 */     paramDescriptor.desiredJdbcType = sqlType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
/* 1853 */     registerOutParameter(parameterIndex, sqlType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
/* 1862 */     checkIsOutputParam(parameterIndex);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void registerOutParameter(String parameterName, int sqlType) throws SQLException {
/* 1871 */     registerOutParameter(getNamedParamIndex(parameterName, true), sqlType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
/* 1880 */     registerOutParameter(getNamedParamIndex(parameterName, true), sqlType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
/* 1889 */     registerOutParameter(getNamedParamIndex(parameterName, true), sqlType, typeName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void retrieveOutParams() throws SQLException {
/* 1900 */     int numParameters = this.paramInfo.numberOfParameters();
/*      */     
/* 1902 */     this.parameterIndexToRsIndex = new int[numParameters];
/*      */     
/* 1904 */     for (int i = 0; i < numParameters; i++) {
/* 1905 */       this.parameterIndexToRsIndex[i] = Integer.MIN_VALUE;
/*      */     }
/*      */     
/* 1908 */     int localParamIndex = 0;
/*      */     
/* 1910 */     if (numParameters > 0) {
/* 1911 */       StringBuffer outParameterQuery = new StringBuffer("SELECT ");
/*      */       
/* 1913 */       boolean firstParam = true;
/* 1914 */       boolean hadOutputParams = false;
/*      */       
/* 1916 */       Iterator paramIter = this.paramInfo.iterator();
/* 1917 */       while (paramIter.hasNext()) {
/* 1918 */         CallableStatementParam retrParamInfo = paramIter.next();
/*      */ 
/*      */         
/* 1921 */         if (retrParamInfo.isOut) {
/* 1922 */           hadOutputParams = true;
/*      */           
/* 1924 */           this.parameterIndexToRsIndex[retrParamInfo.index] = localParamIndex++;
/*      */           
/* 1926 */           String outParameterName = mangleParameterName(retrParamInfo.paramName);
/*      */           
/* 1928 */           if (!firstParam) {
/* 1929 */             outParameterQuery.append(",");
/*      */           } else {
/* 1931 */             firstParam = false;
/*      */           } 
/*      */           
/* 1934 */           if (!outParameterName.startsWith("@")) {
/* 1935 */             outParameterQuery.append('@');
/*      */           }
/*      */           
/* 1938 */           outParameterQuery.append(outParameterName);
/*      */         } 
/*      */       } 
/*      */       
/* 1942 */       if (hadOutputParams) {
/*      */ 
/*      */         
/* 1945 */         Statement outParameterStmt = null;
/* 1946 */         ResultSet outParamRs = null;
/*      */         
/*      */         try {
/* 1949 */           outParameterStmt = this.connection.createStatement();
/* 1950 */           outParamRs = outParameterStmt.executeQuery(outParameterQuery.toString());
/*      */           
/* 1952 */           this.outputParameterResults = ((ResultSetInternalMethods)outParamRs).copy();
/*      */ 
/*      */           
/* 1955 */           if (!this.outputParameterResults.next()) {
/* 1956 */             this.outputParameterResults.close();
/* 1957 */             this.outputParameterResults = null;
/*      */           } 
/*      */         } finally {
/* 1960 */           if (outParameterStmt != null) {
/* 1961 */             outParameterStmt.close();
/*      */           }
/*      */         } 
/*      */       } else {
/* 1965 */         this.outputParameterResults = null;
/*      */       } 
/*      */     } else {
/* 1968 */       this.outputParameterResults = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
/* 1978 */     setAsciiStream(getNamedParamIndex(parameterName, false), x, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
/* 1987 */     setBigDecimal(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
/* 1996 */     setBinaryStream(getNamedParamIndex(parameterName, false), x, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBoolean(String parameterName, boolean x) throws SQLException {
/* 2003 */     setBoolean(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setByte(String parameterName, byte x) throws SQLException {
/* 2010 */     setByte(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBytes(String parameterName, byte[] x) throws SQLException {
/* 2017 */     setBytes(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
/* 2026 */     setCharacterStream(getNamedParamIndex(parameterName, false), reader, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDate(String parameterName, Date x) throws SQLException {
/* 2034 */     setDate(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
/* 2043 */     setDate(getNamedParamIndex(parameterName, false), x, cal);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDouble(String parameterName, double x) throws SQLException {
/* 2050 */     setDouble(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFloat(String parameterName, float x) throws SQLException {
/* 2057 */     setFloat(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setInOutParamsOnServer() throws SQLException {
/* 2064 */     if (this.paramInfo.numParameters > 0) {
/* 2065 */       int parameterIndex = 0;
/*      */       
/* 2067 */       Iterator paramIter = this.paramInfo.iterator();
/* 2068 */       while (paramIter.hasNext()) {
/*      */         
/* 2070 */         CallableStatementParam inParamInfo = paramIter.next();
/*      */ 
/*      */         
/* 2073 */         if (inParamInfo.isOut && inParamInfo.isIn) {
/* 2074 */           String inOutParameterName = mangleParameterName(inParamInfo.paramName);
/* 2075 */           StringBuffer queryBuf = new StringBuffer(4 + inOutParameterName.length() + 1 + 1);
/*      */           
/* 2077 */           queryBuf.append("SET ");
/* 2078 */           queryBuf.append(inOutParameterName);
/* 2079 */           queryBuf.append("=?");
/*      */           
/* 2081 */           PreparedStatement setPstmt = null;
/*      */           
/*      */           try {
/* 2084 */             setPstmt = (PreparedStatement)this.connection.clientPrepareStatement(queryBuf.toString());
/*      */ 
/*      */             
/* 2087 */             byte[] parameterAsBytes = getBytesRepresentation(inParamInfo.index);
/*      */ 
/*      */             
/* 2090 */             if (parameterAsBytes != null) {
/* 2091 */               if (parameterAsBytes.length > 8 && parameterAsBytes[0] == 95 && parameterAsBytes[1] == 98 && parameterAsBytes[2] == 105 && parameterAsBytes[3] == 110 && parameterAsBytes[4] == 97 && parameterAsBytes[5] == 114 && parameterAsBytes[6] == 121 && parameterAsBytes[7] == 39) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 2100 */                 setPstmt.setBytesNoEscapeNoQuotes(1, parameterAsBytes);
/*      */               } else {
/*      */                 
/* 2103 */                 int sqlType = inParamInfo.desiredJdbcType;
/*      */                 
/* 2105 */                 switch (sqlType) {
/*      */                   case -7:
/*      */                   case -4:
/*      */                   case -3:
/*      */                   case -2:
/*      */                   case 2000:
/*      */                   case 2004:
/* 2112 */                     setPstmt.setBytes(1, parameterAsBytes);
/*      */                     break;
/*      */ 
/*      */                   
/*      */                   default:
/* 2117 */                     setPstmt.setBytesNoEscape(1, parameterAsBytes); break;
/*      */                 } 
/*      */               } 
/*      */             } else {
/* 2121 */               setPstmt.setNull(1, 0);
/*      */             } 
/*      */             
/* 2124 */             setPstmt.executeUpdate();
/*      */           } finally {
/* 2126 */             if (setPstmt != null) {
/* 2127 */               setPstmt.close();
/*      */             }
/*      */           } 
/*      */         } 
/*      */         
/* 2132 */         parameterIndex++;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInt(String parameterName, int x) throws SQLException {
/* 2141 */     setInt(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLong(String parameterName, long x) throws SQLException {
/* 2148 */     setLong(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNull(String parameterName, int sqlType) throws SQLException {
/* 2155 */     setNull(getNamedParamIndex(parameterName, false), sqlType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
/* 2164 */     setNull(getNamedParamIndex(parameterName, false), sqlType, typeName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setObject(String parameterName, Object x) throws SQLException {
/* 2172 */     setObject(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
/* 2181 */     setObject(getNamedParamIndex(parameterName, false), x, targetSqlType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setOutParams() throws SQLException {
/* 2193 */     if (this.paramInfo.numParameters > 0) {
/* 2194 */       Iterator paramIter = this.paramInfo.iterator();
/* 2195 */       while (paramIter.hasNext()) {
/* 2196 */         CallableStatementParam outParamInfo = paramIter.next();
/*      */ 
/*      */         
/* 2199 */         if (!this.callingStoredFunction && outParamInfo.isOut) {
/* 2200 */           int outParamIndex; String outParameterName = mangleParameterName(outParamInfo.paramName);
/*      */ 
/*      */ 
/*      */           
/* 2204 */           if (this.placeholderToParameterIndexMap == null) {
/* 2205 */             outParamIndex = outParamInfo.index + 1;
/*      */           } else {
/* 2207 */             outParamIndex = this.placeholderToParameterIndexMap[outParamInfo.index - 1];
/*      */           } 
/*      */           
/* 2210 */           setBytesNoEscapeNoQuotes(outParamIndex, StringUtils.getBytes(outParameterName, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor()));
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
/*      */   public void setShort(String parameterName, short x) throws SQLException {
/* 2225 */     setShort(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setString(String parameterName, String x) throws SQLException {
/* 2233 */     setString(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTime(String parameterName, Time x) throws SQLException {
/* 2240 */     setTime(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
/* 2249 */     setTime(getNamedParamIndex(parameterName, false), x, cal);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
/* 2258 */     setTimestamp(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
/* 2267 */     setTimestamp(getNamedParamIndex(parameterName, false), x, cal);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setURL(String parameterName, URL val) throws SQLException {
/* 2274 */     setURL(getNamedParamIndex(parameterName, false), val);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized boolean wasNull() throws SQLException {
/* 2281 */     return this.outputParamWasNull;
/*      */   }
/*      */   
/*      */   public int[] executeBatch() throws SQLException {
/* 2285 */     if (this.hasOutputParams) {
/* 2286 */       throw SQLError.createSQLException("Can't call executeBatch() on CallableStatement with OUTPUT parameters", "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */     
/* 2290 */     return super.executeBatch();
/*      */   }
/*      */   
/*      */   protected int getParameterIndexOffset() {
/* 2294 */     if (this.callingStoredFunction) {
/* 2295 */       return -1;
/*      */     }
/*      */     
/* 2298 */     return super.getParameterIndexOffset();
/*      */   }
/*      */   
/*      */   public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
/* 2302 */     setAsciiStream(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
/* 2307 */     setAsciiStream(getNamedParamIndex(parameterName, false), x, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
/* 2312 */     setBinaryStream(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
/* 2317 */     setBinaryStream(getNamedParamIndex(parameterName, false), x, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBlob(String parameterName, Blob x) throws SQLException {
/* 2322 */     setBlob(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
/* 2327 */     setBlob(getNamedParamIndex(parameterName, false), inputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
/* 2332 */     setBlob(getNamedParamIndex(parameterName, false), inputStream, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
/* 2337 */     setCharacterStream(getNamedParamIndex(parameterName, false), reader);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
/* 2342 */     setCharacterStream(getNamedParamIndex(parameterName, false), reader, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setClob(String parameterName, Clob x) throws SQLException {
/* 2347 */     setClob(getNamedParamIndex(parameterName, false), x);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setClob(String parameterName, Reader reader) throws SQLException {
/* 2352 */     setClob(getNamedParamIndex(parameterName, false), reader);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setClob(String parameterName, Reader reader, long length) throws SQLException {
/* 2357 */     setClob(getNamedParamIndex(parameterName, false), reader, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
/* 2362 */     setNCharacterStream(getNamedParamIndex(parameterName, false), value);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
/* 2367 */     setNCharacterStream(getNamedParamIndex(parameterName, false), value, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkReadOnlyProcedure() throws SQLException {
/* 2378 */     if (this.connection.getNoAccessToProcedureBodies()) {
/* 2379 */       return false;
/*      */     }
/*      */     
/* 2382 */     synchronized (this.paramInfo) {
/* 2383 */       if (this.paramInfo.isReadOnlySafeChecked) {
/* 2384 */         return this.paramInfo.isReadOnlySafeProcedure;
/*      */       }
/*      */       
/* 2387 */       ResultSet rs = null;
/* 2388 */       PreparedStatement ps = null;
/*      */       
/*      */       try {
/* 2391 */         String procName = extractProcedureName();
/*      */         
/* 2393 */         String catalog = this.currentCatalog;
/*      */         
/* 2395 */         if (procName.indexOf(".") != -1) {
/* 2396 */           catalog = procName.substring(0, procName.indexOf("."));
/* 2397 */           procName = procName.substring(procName.indexOf(".") + 1);
/* 2398 */           procName = new String(StringUtils.stripEnclosure(procName.getBytes(), "`", "`"));
/*      */         } 
/*      */         
/* 2401 */         ps = ((DatabaseMetaData)this.connection.getMetaData()).prepareMetaDataSafeStatement("SELECT SQL_DATA_ACCESS FROM  information_schema.routines  WHERE routine_schema = ?  AND routine_name = ?");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2408 */         ps.setString(1, catalog);
/* 2409 */         ps.setString(2, procName);
/* 2410 */         rs = ps.executeQuery();
/* 2411 */         if (rs.next()) {
/* 2412 */           String sqlDataAccess = rs.getString(1);
/* 2413 */           if ("READS SQL DATA".equalsIgnoreCase(sqlDataAccess) || "NO SQL".equalsIgnoreCase(sqlDataAccess)) {
/*      */             
/* 2415 */             synchronized (this.paramInfo) {
/* 2416 */               this.paramInfo.isReadOnlySafeChecked = true;
/* 2417 */               this.paramInfo.isReadOnlySafeProcedure = true;
/*      */             } 
/* 2419 */             return true;
/*      */           } 
/*      */         } 
/* 2422 */       } catch (SQLException e) {
/*      */       
/*      */       } finally {
/* 2425 */         if (rs != null) {
/* 2426 */           rs.close();
/*      */         }
/* 2428 */         if (ps != null) {
/* 2429 */           ps.close();
/*      */         }
/*      */       } 
/*      */       
/* 2433 */       this.paramInfo.isReadOnlySafeChecked = false;
/* 2434 */       this.paramInfo.isReadOnlySafeProcedure = false;
/*      */     } 
/* 2436 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean checkReadOnlySafeStatement() throws SQLException {
/* 2441 */     return (super.checkReadOnlySafeStatement() || checkReadOnlyProcedure());
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\CallableStatement.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */