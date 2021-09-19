/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.rmi.server.UID;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Savepoint;
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
/*     */ public class MysqlSavepoint
/*     */   implements Savepoint
/*     */ {
/*     */   private String savepointName;
/*     */   private ExceptionInterceptor exceptionInterceptor;
/*     */   
/*     */   private static String getUniqueId() {
/*  41 */     String uidStr = (new UID()).toString();
/*     */     
/*  43 */     int uidLength = uidStr.length();
/*     */     
/*  45 */     StringBuffer safeString = new StringBuffer(uidLength);
/*     */     
/*  47 */     for (int i = 0; i < uidLength; i++) {
/*  48 */       char c = uidStr.charAt(i);
/*     */       
/*  50 */       if (Character.isLetter(c) || Character.isDigit(c)) {
/*  51 */         safeString.append(c);
/*     */       } else {
/*  53 */         safeString.append('_');
/*     */       } 
/*     */     } 
/*     */     
/*  57 */     return safeString.toString();
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
/*     */   MysqlSavepoint(ExceptionInterceptor exceptionInterceptor) throws SQLException {
/*  73 */     this(getUniqueId(), exceptionInterceptor);
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
/*     */   MysqlSavepoint(String name, ExceptionInterceptor exceptionInterceptor) throws SQLException {
/*  86 */     if (name == null || name.length() == 0) {
/*  87 */       throw SQLError.createSQLException("Savepoint name can not be NULL or empty", "S1009", exceptionInterceptor);
/*     */     }
/*     */ 
/*     */     
/*  91 */     this.savepointName = name;
/*     */     
/*  93 */     this.exceptionInterceptor = exceptionInterceptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSavepointId() throws SQLException {
/* 100 */     throw SQLError.createSQLException("Only named savepoints are supported.", "S1C00", this.exceptionInterceptor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSavepointName() throws SQLException {
/* 108 */     return this.savepointName;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\MysqlSavepoint.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */