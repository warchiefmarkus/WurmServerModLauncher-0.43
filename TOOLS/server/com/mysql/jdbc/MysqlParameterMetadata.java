/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.sql.ParameterMetaData;
/*     */ import java.sql.SQLException;
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
/*     */ public class MysqlParameterMetadata
/*     */   implements ParameterMetaData
/*     */ {
/*     */   boolean returnSimpleMetadata = false;
/*  33 */   ResultSetMetaData metadata = null;
/*     */   
/*  35 */   int parameterCount = 0;
/*     */   
/*     */   private ExceptionInterceptor exceptionInterceptor;
/*     */   
/*     */   MysqlParameterMetadata(Field[] fieldInfo, int parameterCount, ExceptionInterceptor exceptionInterceptor) {
/*  40 */     this.metadata = new ResultSetMetaData(fieldInfo, false, exceptionInterceptor);
/*     */     
/*  42 */     this.parameterCount = parameterCount;
/*  43 */     this.exceptionInterceptor = exceptionInterceptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   MysqlParameterMetadata(int count) {
/*  53 */     this.parameterCount = count;
/*  54 */     this.returnSimpleMetadata = true;
/*     */   }
/*     */   
/*     */   public int getParameterCount() throws SQLException {
/*  58 */     return this.parameterCount;
/*     */   }
/*     */   
/*     */   public int isNullable(int arg0) throws SQLException {
/*  62 */     checkAvailable();
/*     */     
/*  64 */     return this.metadata.isNullable(arg0);
/*     */   }
/*     */   
/*     */   private void checkAvailable() throws SQLException {
/*  68 */     if (this.metadata == null || this.metadata.fields == null) {
/*  69 */       throw SQLError.createSQLException("Parameter metadata not available for the given statement", "S1C00", this.exceptionInterceptor);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSigned(int arg0) throws SQLException {
/*  76 */     if (this.returnSimpleMetadata) {
/*  77 */       checkBounds(arg0);
/*     */       
/*  79 */       return false;
/*     */     } 
/*     */     
/*  82 */     checkAvailable();
/*     */     
/*  84 */     return this.metadata.isSigned(arg0);
/*     */   }
/*     */   
/*     */   public int getPrecision(int arg0) throws SQLException {
/*  88 */     if (this.returnSimpleMetadata) {
/*  89 */       checkBounds(arg0);
/*     */       
/*  91 */       return 0;
/*     */     } 
/*     */     
/*  94 */     checkAvailable();
/*     */     
/*  96 */     return this.metadata.getPrecision(arg0);
/*     */   }
/*     */   
/*     */   public int getScale(int arg0) throws SQLException {
/* 100 */     if (this.returnSimpleMetadata) {
/* 101 */       checkBounds(arg0);
/*     */       
/* 103 */       return 0;
/*     */     } 
/*     */     
/* 106 */     checkAvailable();
/*     */     
/* 108 */     return this.metadata.getScale(arg0);
/*     */   }
/*     */   
/*     */   public int getParameterType(int arg0) throws SQLException {
/* 112 */     if (this.returnSimpleMetadata) {
/* 113 */       checkBounds(arg0);
/*     */       
/* 115 */       return 12;
/*     */     } 
/*     */     
/* 118 */     checkAvailable();
/*     */     
/* 120 */     return this.metadata.getColumnType(arg0);
/*     */   }
/*     */   
/*     */   public String getParameterTypeName(int arg0) throws SQLException {
/* 124 */     if (this.returnSimpleMetadata) {
/* 125 */       checkBounds(arg0);
/*     */       
/* 127 */       return "VARCHAR";
/*     */     } 
/*     */     
/* 130 */     checkAvailable();
/*     */     
/* 132 */     return this.metadata.getColumnTypeName(arg0);
/*     */   }
/*     */   
/*     */   public String getParameterClassName(int arg0) throws SQLException {
/* 136 */     if (this.returnSimpleMetadata) {
/* 137 */       checkBounds(arg0);
/*     */       
/* 139 */       return "java.lang.String";
/*     */     } 
/*     */     
/* 142 */     checkAvailable();
/*     */     
/* 144 */     return this.metadata.getColumnClassName(arg0);
/*     */   }
/*     */   
/*     */   public int getParameterMode(int arg0) throws SQLException {
/* 148 */     return 1;
/*     */   }
/*     */   
/*     */   private void checkBounds(int paramNumber) throws SQLException {
/* 152 */     if (paramNumber < 1) {
/* 153 */       throw SQLError.createSQLException("Parameter index of '" + paramNumber + "' is invalid.", "S1009", this.exceptionInterceptor);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 158 */     if (paramNumber > this.parameterCount) {
/* 159 */       throw SQLError.createSQLException("Parameter index of '" + paramNumber + "' is greater than number of parameters, which is '" + this.parameterCount + "'.", "S1009", this.exceptionInterceptor);
/*     */     }
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
/*     */   public boolean isWrapperFor(Class iface) throws SQLException {
/* 187 */     return iface.isInstance(this);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object unwrap(Class iface) throws SQLException {
/*     */     try {
/* 208 */       return Util.cast(iface, this);
/* 209 */     } catch (ClassCastException cce) {
/* 210 */       throw SQLError.createSQLException("Unable to unwrap to " + iface.toString(), "S1009", this.exceptionInterceptor);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\MysqlParameterMetadata.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */