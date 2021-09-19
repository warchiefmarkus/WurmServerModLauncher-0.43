/*     */ package com.mysql.jdbc.jdbc2.optional;
/*     */ 
/*     */ import com.mysql.jdbc.Connection;
/*     */ import com.mysql.jdbc.ExceptionInterceptor;
/*     */ import com.mysql.jdbc.SQLError;
/*     */ import com.mysql.jdbc.Util;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.sql.ConnectionEvent;
/*     */ import javax.sql.ConnectionEventListener;
/*     */ import javax.sql.PooledConnection;
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
/*     */ public class MysqlPooledConnection
/*     */   implements PooledConnection
/*     */ {
/*     */   private static final Constructor JDBC_4_POOLED_CONNECTION_WRAPPER_CTOR;
/*     */   public static final int CONNECTION_ERROR_EVENT = 1;
/*     */   public static final int CONNECTION_CLOSED_EVENT = 2;
/*     */   private Map connectionEventListeners;
/*     */   private Connection logicalHandle;
/*     */   private Connection physicalConn;
/*     */   private ExceptionInterceptor exceptionInterceptor;
/*     */   
/*     */   static {
/*  56 */     if (Util.isJdbc4()) {
/*     */       try {
/*  58 */         JDBC_4_POOLED_CONNECTION_WRAPPER_CTOR = Class.forName("com.mysql.jdbc.jdbc2.optional.JDBC4MysqlPooledConnection").getConstructor(new Class[] { Connection.class });
/*     */ 
/*     */       
/*     */       }
/*  62 */       catch (SecurityException e) {
/*  63 */         throw new RuntimeException(e);
/*  64 */       } catch (NoSuchMethodException e) {
/*  65 */         throw new RuntimeException(e);
/*  66 */       } catch (ClassNotFoundException e) {
/*  67 */         throw new RuntimeException(e);
/*     */       } 
/*     */     } else {
/*  70 */       JDBC_4_POOLED_CONNECTION_WRAPPER_CTOR = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static MysqlPooledConnection getInstance(Connection connection) throws SQLException {
/*  75 */     if (!Util.isJdbc4()) {
/*  76 */       return new MysqlPooledConnection(connection);
/*     */     }
/*     */     
/*  79 */     return (MysqlPooledConnection)Util.handleNewInstance(JDBC_4_POOLED_CONNECTION_WRAPPER_CTOR, new Object[] { connection }, connection.getExceptionInterceptor());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MysqlPooledConnection(Connection connection) {
/* 113 */     this.logicalHandle = null;
/* 114 */     this.physicalConn = connection;
/* 115 */     this.connectionEventListeners = new HashMap();
/* 116 */     this.exceptionInterceptor = this.physicalConn.getExceptionInterceptor();
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
/*     */   public synchronized void addConnectionEventListener(ConnectionEventListener connectioneventlistener) {
/* 129 */     if (this.connectionEventListeners != null) {
/* 130 */       this.connectionEventListeners.put(connectioneventlistener, connectioneventlistener);
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
/*     */   public synchronized void removeConnectionEventListener(ConnectionEventListener connectioneventlistener) {
/* 145 */     if (this.connectionEventListeners != null) {
/* 146 */       this.connectionEventListeners.remove(connectioneventlistener);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Connection getConnection() throws SQLException {
/* 157 */     return getConnection(true, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized Connection getConnection(boolean resetServerState, boolean forXa) throws SQLException {
/* 164 */     if (this.physicalConn == null) {
/*     */       
/* 166 */       SQLException sqlException = SQLError.createSQLException("Physical Connection doesn't exist", this.exceptionInterceptor);
/*     */       
/* 168 */       callConnectionEventListeners(1, sqlException);
/*     */       
/* 170 */       throw sqlException;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 175 */       if (this.logicalHandle != null) {
/* 176 */         ((ConnectionWrapper)this.logicalHandle).close(false);
/*     */       }
/*     */       
/* 179 */       if (resetServerState) {
/* 180 */         this.physicalConn.resetServerState();
/*     */       }
/*     */       
/* 183 */       this.logicalHandle = (Connection)ConnectionWrapper.getInstance(this, this.physicalConn, forXa);
/*     */     
/*     */     }
/* 186 */     catch (SQLException sqlException) {
/* 187 */       callConnectionEventListeners(1, sqlException);
/*     */       
/* 189 */       throw sqlException;
/*     */     } 
/*     */     
/* 192 */     return this.logicalHandle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void close() throws SQLException {
/* 203 */     if (this.physicalConn != null) {
/* 204 */       this.physicalConn.close();
/*     */       
/* 206 */       this.physicalConn = null;
/*     */     } 
/*     */     
/* 209 */     if (this.connectionEventListeners != null) {
/* 210 */       this.connectionEventListeners.clear();
/*     */       
/* 212 */       this.connectionEventListeners = null;
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
/*     */   protected synchronized void callConnectionEventListeners(int eventType, SQLException sqlException) {
/* 231 */     if (this.connectionEventListeners == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 236 */     Iterator iterator = this.connectionEventListeners.entrySet().iterator();
/*     */     
/* 238 */     ConnectionEvent connectionevent = new ConnectionEvent(this, sqlException);
/*     */ 
/*     */     
/* 241 */     while (iterator.hasNext()) {
/*     */       
/* 243 */       ConnectionEventListener connectioneventlistener = (ConnectionEventListener)((Map.Entry)iterator.next()).getValue();
/*     */ 
/*     */       
/* 246 */       if (eventType == 2) {
/* 247 */         connectioneventlistener.connectionClosed(connectionevent); continue;
/* 248 */       }  if (eventType == 1) {
/* 249 */         connectioneventlistener.connectionErrorOccurred(connectionevent);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected ExceptionInterceptor getExceptionInterceptor() {
/* 256 */     return this.exceptionInterceptor;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\jdbc2\optional\MysqlPooledConnection.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */