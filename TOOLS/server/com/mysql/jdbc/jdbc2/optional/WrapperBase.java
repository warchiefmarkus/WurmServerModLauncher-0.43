/*     */ package com.mysql.jdbc.jdbc2.optional;
/*     */ 
/*     */ import com.mysql.jdbc.ExceptionInterceptor;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Map;
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
/*     */ abstract class WrapperBase
/*     */ {
/*     */   protected MysqlPooledConnection pooledConnection;
/*     */   
/*     */   protected void checkAndFireConnectionError(SQLException sqlEx) throws SQLException {
/*  57 */     if (this.pooledConnection != null && 
/*  58 */       "08S01".equals(sqlEx.getSQLState()))
/*     */     {
/*  60 */       this.pooledConnection.callConnectionEventListeners(1, sqlEx);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  65 */     throw sqlEx;
/*     */   }
/*     */   
/*  68 */   protected Map unwrappedInterfaces = null;
/*     */   protected ExceptionInterceptor exceptionInterceptor;
/*     */   
/*     */   protected WrapperBase(MysqlPooledConnection pooledConnection) {
/*  72 */     this.pooledConnection = pooledConnection;
/*  73 */     this.exceptionInterceptor = this.pooledConnection.getExceptionInterceptor();
/*     */   }
/*     */   
/*     */   protected class ConnectionErrorFiringInvocationHandler implements InvocationHandler { Object invokeOn;
/*     */     
/*     */     public ConnectionErrorFiringInvocationHandler(WrapperBase this$0, Object toInvokeOn) {
/*  79 */       this.this$0 = this$0; this.invokeOn = null;
/*  80 */       this.invokeOn = toInvokeOn;
/*     */     }
/*     */     private final WrapperBase this$0;
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/*  85 */       Object result = null;
/*     */       
/*     */       try {
/*  88 */         result = method.invoke(this.invokeOn, args);
/*     */         
/*  90 */         if (result != null) {
/*  91 */           result = proxyIfInterfaceIsJdbc(result, result.getClass());
/*     */         }
/*     */       }
/*  94 */       catch (InvocationTargetException e) {
/*  95 */         if (e.getTargetException() instanceof SQLException) {
/*  96 */           this.this$0.checkAndFireConnectionError((SQLException)e.getTargetException());
/*     */         } else {
/*     */           
/*  99 */           throw e;
/*     */         } 
/*     */       } 
/*     */       
/* 103 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Object proxyIfInterfaceIsJdbc(Object toProxy, Class clazz) {
/* 115 */       Class[] interfaces = clazz.getInterfaces();
/*     */       
/* 117 */       int i = 0; if (i < interfaces.length) {
/* 118 */         String packageName = interfaces[i].getPackage().getName();
/*     */         
/* 120 */         if ("java.sql".equals(packageName) || "javax.sql".equals(packageName))
/*     */         {
/* 122 */           return Proxy.newProxyInstance(toProxy.getClass().getClassLoader(), interfaces, new ConnectionErrorFiringInvocationHandler(this.this$0, toProxy));
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 127 */         return proxyIfInterfaceIsJdbc(toProxy, interfaces[i]);
/*     */       } 
/*     */       
/* 130 */       return toProxy;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\jdbc2\optional\WrapperBase.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */