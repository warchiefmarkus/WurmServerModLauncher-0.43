/*     */ package com.mysql.jdbc.log;
/*     */ 
/*     */ import com.mysql.jdbc.ExceptionInterceptor;
/*     */ import com.mysql.jdbc.SQLError;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
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
/*     */ public class LogFactory
/*     */ {
/*     */   public static Log getLogger(String className, String instanceName, ExceptionInterceptor exceptionInterceptor) throws SQLException {
/*  58 */     if (className == null) {
/*  59 */       throw SQLError.createSQLException("Logger class can not be NULL", "S1009", exceptionInterceptor);
/*     */     }
/*     */ 
/*     */     
/*  63 */     if (instanceName == null) {
/*  64 */       throw SQLError.createSQLException("Logger instance name can not be NULL", "S1009", exceptionInterceptor);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  70 */       Class loggerClass = null;
/*     */       
/*     */       try {
/*  73 */         loggerClass = Class.forName(className);
/*  74 */       } catch (ClassNotFoundException nfe) {
/*  75 */         loggerClass = Class.forName(Log.class.getPackage().getName() + "." + className);
/*     */       } 
/*     */ 
/*     */       
/*  79 */       Constructor constructor = loggerClass.getConstructor(new Class[] { String.class });
/*     */ 
/*     */       
/*  82 */       return (Log)constructor.newInstance(new Object[] { instanceName });
/*  83 */     } catch (ClassNotFoundException cnfe) {
/*  84 */       SQLException sqlEx = SQLError.createSQLException("Unable to load class for logger '" + className + "'", "S1009", exceptionInterceptor);
/*     */ 
/*     */       
/*  87 */       sqlEx.initCause(cnfe);
/*     */       
/*  89 */       throw sqlEx;
/*  90 */     } catch (NoSuchMethodException nsme) {
/*  91 */       SQLException sqlEx = SQLError.createSQLException("Logger class does not have a single-arg constructor that takes an instance name", "S1009", exceptionInterceptor);
/*     */ 
/*     */ 
/*     */       
/*  95 */       sqlEx.initCause(nsme);
/*     */       
/*  97 */       throw sqlEx;
/*  98 */     } catch (InstantiationException inse) {
/*  99 */       SQLException sqlEx = SQLError.createSQLException("Unable to instantiate logger class '" + className + "', exception in constructor?", "S1009", exceptionInterceptor);
/*     */ 
/*     */ 
/*     */       
/* 103 */       sqlEx.initCause(inse);
/*     */       
/* 105 */       throw sqlEx;
/* 106 */     } catch (InvocationTargetException ite) {
/* 107 */       SQLException sqlEx = SQLError.createSQLException("Unable to instantiate logger class '" + className + "', exception in constructor?", "S1009", exceptionInterceptor);
/*     */ 
/*     */ 
/*     */       
/* 111 */       sqlEx.initCause(ite);
/*     */       
/* 113 */       throw sqlEx;
/* 114 */     } catch (IllegalAccessException iae) {
/* 115 */       SQLException sqlEx = SQLError.createSQLException("Unable to instantiate logger class '" + className + "', constructor not public", "S1009", exceptionInterceptor);
/*     */ 
/*     */ 
/*     */       
/* 119 */       sqlEx.initCause(iae);
/*     */       
/* 121 */       throw sqlEx;
/* 122 */     } catch (ClassCastException cce) {
/* 123 */       SQLException sqlEx = SQLError.createSQLException("Logger class '" + className + "' does not implement the '" + Log.class.getName() + "' interface", "S1009", exceptionInterceptor);
/*     */ 
/*     */ 
/*     */       
/* 127 */       sqlEx.initCause(cce);
/*     */       
/* 129 */       throw sqlEx;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\log\LogFactory.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */