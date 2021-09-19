/*     */ package com.mysql.jdbc.jdbc2.optional;
/*     */ 
/*     */ import com.mysql.jdbc.SQLError;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.sql.NClob;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.RowId;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLXML;
/*     */ import java.util.HashMap;
/*     */ import javax.sql.StatementEvent;
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
/*     */ public class JDBC4PreparedStatementWrapper
/*     */   extends PreparedStatementWrapper
/*     */ {
/*     */   public JDBC4PreparedStatementWrapper(ConnectionWrapper c, MysqlPooledConnection conn, PreparedStatement toWrap) {
/*  62 */     super(c, conn, toWrap);
/*     */   }
/*     */   
/*     */   public synchronized void close() throws SQLException {
/*  66 */     if (this.pooledConnection == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  71 */     MysqlPooledConnection con = this.pooledConnection;
/*     */ 
/*     */     
/*     */     try {
/*  75 */       super.close();
/*     */     } finally {
/*     */       try {
/*  78 */         StatementEvent e = new StatementEvent(con, this);
/*     */         
/*  80 */         if (con instanceof JDBC4MysqlPooledConnection) {
/*  81 */           ((JDBC4MysqlPooledConnection)con).fireStatementEvent(e);
/*  82 */         } else if (con instanceof JDBC4MysqlXAConnection) {
/*  83 */           ((JDBC4MysqlXAConnection)con).fireStatementEvent(e);
/*  84 */         } else if (con instanceof JDBC4SuspendableXAConnection) {
/*  85 */           ((JDBC4SuspendableXAConnection)con).fireStatementEvent(e);
/*     */         } 
/*     */       } finally {
/*  88 */         this.unwrappedInterfaces = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isClosed() throws SQLException {
/*     */     try {
/*  95 */       if (this.wrappedStmt != null) {
/*  96 */         return this.wrappedStmt.isClosed();
/*     */       }
/*  98 */       throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
/*     */     
/*     */     }
/* 101 */     catch (SQLException sqlEx) {
/* 102 */       checkAndFireConnectionError(sqlEx);
/*     */ 
/*     */       
/* 105 */       return false;
/*     */     } 
/*     */   }
/*     */   public void setPoolable(boolean poolable) throws SQLException {
/*     */     try {
/* 110 */       if (this.wrappedStmt != null) {
/* 111 */         this.wrappedStmt.setPoolable(poolable);
/*     */       } else {
/* 113 */         throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
/*     */       }
/*     */     
/* 116 */     } catch (SQLException sqlEx) {
/* 117 */       checkAndFireConnectionError(sqlEx);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isPoolable() throws SQLException {
/*     */     try {
/* 123 */       if (this.wrappedStmt != null) {
/* 124 */         return this.wrappedStmt.isPoolable();
/*     */       }
/* 126 */       throw SQLError.createSQLException("Statement already closed", "S1009", this.exceptionInterceptor);
/*     */     
/*     */     }
/* 129 */     catch (SQLException sqlEx) {
/* 130 */       checkAndFireConnectionError(sqlEx);
/*     */ 
/*     */       
/* 133 */       return false;
/*     */     } 
/*     */   }
/*     */   public void setRowId(int parameterIndex, RowId x) throws SQLException {
/*     */     try {
/* 138 */       if (this.wrappedStmt != null) {
/* 139 */         ((PreparedStatement)this.wrappedStmt).setRowId(parameterIndex, x);
/*     */       } else {
/*     */         
/* 142 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     
/*     */     }
/* 146 */     catch (SQLException sqlEx) {
/* 147 */       checkAndFireConnectionError(sqlEx);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setNClob(int parameterIndex, NClob value) throws SQLException {
/*     */     try {
/* 153 */       if (this.wrappedStmt != null) {
/* 154 */         ((PreparedStatement)this.wrappedStmt).setNClob(parameterIndex, value);
/*     */       } else {
/*     */         
/* 157 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     
/*     */     }
/* 161 */     catch (SQLException sqlEx) {
/* 162 */       checkAndFireConnectionError(sqlEx);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
/*     */     try {
/* 169 */       if (this.wrappedStmt != null) {
/* 170 */         ((PreparedStatement)this.wrappedStmt).setSQLXML(parameterIndex, xmlObject);
/*     */       } else {
/*     */         
/* 173 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     
/*     */     }
/* 177 */     catch (SQLException sqlEx) {
/* 178 */       checkAndFireConnectionError(sqlEx);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNString(int parameterIndex, String value) throws SQLException {
/*     */     try {
/* 187 */       if (this.wrappedStmt != null) {
/* 188 */         ((PreparedStatement)this.wrappedStmt).setNString(parameterIndex, value);
/*     */       } else {
/*     */         
/* 191 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     
/*     */     }
/* 195 */     catch (SQLException sqlEx) {
/* 196 */       checkAndFireConnectionError(sqlEx);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
/*     */     try {
/* 205 */       if (this.wrappedStmt != null) {
/* 206 */         ((PreparedStatement)this.wrappedStmt).setNCharacterStream(parameterIndex, value, length);
/*     */       } else {
/*     */         
/* 209 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     
/*     */     }
/* 213 */     catch (SQLException sqlEx) {
/* 214 */       checkAndFireConnectionError(sqlEx);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
/*     */     try {
/* 223 */       if (this.wrappedStmt != null) {
/* 224 */         ((PreparedStatement)this.wrappedStmt).setClob(parameterIndex, reader, length);
/*     */       } else {
/*     */         
/* 227 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     
/*     */     }
/* 231 */     catch (SQLException sqlEx) {
/* 232 */       checkAndFireConnectionError(sqlEx);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
/*     */     try {
/* 241 */       if (this.wrappedStmt != null) {
/* 242 */         ((PreparedStatement)this.wrappedStmt).setBlob(parameterIndex, inputStream, length);
/*     */       } else {
/*     */         
/* 245 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     
/*     */     }
/* 249 */     catch (SQLException sqlEx) {
/* 250 */       checkAndFireConnectionError(sqlEx);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
/*     */     try {
/* 259 */       if (this.wrappedStmt != null) {
/* 260 */         ((PreparedStatement)this.wrappedStmt).setNClob(parameterIndex, reader, length);
/*     */       } else {
/*     */         
/* 263 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     
/*     */     }
/* 267 */     catch (SQLException sqlEx) {
/* 268 */       checkAndFireConnectionError(sqlEx);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
/*     */     try {
/* 277 */       if (this.wrappedStmt != null) {
/* 278 */         ((PreparedStatement)this.wrappedStmt).setAsciiStream(parameterIndex, x, length);
/*     */       } else {
/*     */         
/* 281 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     
/*     */     }
/* 285 */     catch (SQLException sqlEx) {
/* 286 */       checkAndFireConnectionError(sqlEx);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
/*     */     try {
/* 295 */       if (this.wrappedStmt != null) {
/* 296 */         ((PreparedStatement)this.wrappedStmt).setBinaryStream(parameterIndex, x, length);
/*     */       } else {
/*     */         
/* 299 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     
/*     */     }
/* 303 */     catch (SQLException sqlEx) {
/* 304 */       checkAndFireConnectionError(sqlEx);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
/*     */     try {
/* 313 */       if (this.wrappedStmt != null) {
/* 314 */         ((PreparedStatement)this.wrappedStmt).setCharacterStream(parameterIndex, reader, length);
/*     */       } else {
/*     */         
/* 317 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     
/*     */     }
/* 321 */     catch (SQLException sqlEx) {
/* 322 */       checkAndFireConnectionError(sqlEx);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
/*     */     try {
/* 330 */       if (this.wrappedStmt != null) {
/* 331 */         ((PreparedStatement)this.wrappedStmt).setAsciiStream(parameterIndex, x);
/*     */       } else {
/*     */         
/* 334 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     
/*     */     }
/* 338 */     catch (SQLException sqlEx) {
/* 339 */       checkAndFireConnectionError(sqlEx);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
/*     */     try {
/* 347 */       if (this.wrappedStmt != null) {
/* 348 */         ((PreparedStatement)this.wrappedStmt).setBinaryStream(parameterIndex, x);
/*     */       } else {
/*     */         
/* 351 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     
/*     */     }
/* 355 */     catch (SQLException sqlEx) {
/* 356 */       checkAndFireConnectionError(sqlEx);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
/*     */     try {
/* 364 */       if (this.wrappedStmt != null) {
/* 365 */         ((PreparedStatement)this.wrappedStmt).setCharacterStream(parameterIndex, reader);
/*     */       } else {
/*     */         
/* 368 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     
/*     */     }
/* 372 */     catch (SQLException sqlEx) {
/* 373 */       checkAndFireConnectionError(sqlEx);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
/*     */     try {
/* 382 */       if (this.wrappedStmt != null) {
/* 383 */         ((PreparedStatement)this.wrappedStmt).setNCharacterStream(parameterIndex, value);
/*     */       } else {
/*     */         
/* 386 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     
/*     */     }
/* 390 */     catch (SQLException sqlEx) {
/* 391 */       checkAndFireConnectionError(sqlEx);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClob(int parameterIndex, Reader reader) throws SQLException {
/*     */     try {
/* 400 */       if (this.wrappedStmt != null) {
/* 401 */         ((PreparedStatement)this.wrappedStmt).setClob(parameterIndex, reader);
/*     */       } else {
/*     */         
/* 404 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     
/*     */     }
/* 408 */     catch (SQLException sqlEx) {
/* 409 */       checkAndFireConnectionError(sqlEx);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
/*     */     try {
/* 418 */       if (this.wrappedStmt != null) {
/* 419 */         ((PreparedStatement)this.wrappedStmt).setBlob(parameterIndex, inputStream);
/*     */       } else {
/*     */         
/* 422 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     
/*     */     }
/* 426 */     catch (SQLException sqlEx) {
/* 427 */       checkAndFireConnectionError(sqlEx);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNClob(int parameterIndex, Reader reader) throws SQLException {
/*     */     try {
/* 435 */       if (this.wrappedStmt != null) {
/* 436 */         ((PreparedStatement)this.wrappedStmt).setNClob(parameterIndex, reader);
/*     */       } else {
/*     */         
/* 439 */         throw SQLError.createSQLException("No operations allowed after statement closed", "S1000", this.exceptionInterceptor);
/*     */       }
/*     */     
/*     */     }
/* 443 */     catch (SQLException sqlEx) {
/* 444 */       checkAndFireConnectionError(sqlEx);
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
/*     */   public boolean isWrapperFor(Class<?> iface) throws SQLException {
/* 472 */     boolean isInstance = iface.isInstance(this);
/*     */     
/* 474 */     if (isInstance) {
/* 475 */       return true;
/*     */     }
/*     */     
/* 478 */     String interfaceClassName = iface.getName();
/*     */     
/* 480 */     return (interfaceClassName.equals("com.mysql.jdbc.Statement") || interfaceClassName.equals("java.sql.Statement") || interfaceClassName.equals("java.sql.PreparedStatement") || interfaceClassName.equals("java.sql.Wrapper"));
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
/*     */   public synchronized <T> T unwrap(Class<T> iface) throws SQLException {
/*     */     try {
/* 508 */       if ("java.sql.Statement".equals(iface.getName()) || "java.sql.PreparedStatement".equals(iface.getName()) || "java.sql.Wrapper.class".equals(iface.getName()))
/*     */       {
/*     */         
/* 511 */         return iface.cast(this);
/*     */       }
/*     */       
/* 514 */       if (this.unwrappedInterfaces == null) {
/* 515 */         this.unwrappedInterfaces = new HashMap<Object, Object>();
/*     */       }
/*     */       
/* 518 */       Object cachedUnwrapped = this.unwrappedInterfaces.get(iface);
/*     */       
/* 520 */       if (cachedUnwrapped == null) {
/* 521 */         if (cachedUnwrapped == null) {
/* 522 */           cachedUnwrapped = Proxy.newProxyInstance(this.wrappedStmt.getClass().getClassLoader(), new Class[] { iface }, new WrapperBase.ConnectionErrorFiringInvocationHandler(this, this.wrappedStmt));
/*     */ 
/*     */ 
/*     */           
/* 526 */           this.unwrappedInterfaces.put(iface, cachedUnwrapped);
/*     */         } 
/* 528 */         this.unwrappedInterfaces.put(iface, cachedUnwrapped);
/*     */       } 
/*     */       
/* 531 */       return iface.cast(cachedUnwrapped);
/* 532 */     } catch (ClassCastException cce) {
/* 533 */       throw SQLError.createSQLException("Unable to unwrap to " + iface.toString(), "S1009", this.exceptionInterceptor);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\jdbc2\optional\JDBC4PreparedStatementWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */