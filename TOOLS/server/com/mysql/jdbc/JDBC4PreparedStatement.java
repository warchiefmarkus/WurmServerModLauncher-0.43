/*    */ package com.mysql.jdbc;
/*    */ 
/*    */ import java.sql.NClob;
/*    */ import java.sql.RowId;
/*    */ import java.sql.SQLException;
/*    */ import java.sql.SQLXML;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JDBC4PreparedStatement
/*    */   extends PreparedStatement
/*    */ {
/*    */   public JDBC4PreparedStatement(ConnectionImpl conn, String catalog) throws SQLException {
/* 42 */     super(conn, catalog);
/*    */   }
/*    */ 
/*    */   
/*    */   public JDBC4PreparedStatement(ConnectionImpl conn, String sql, String catalog) throws SQLException {
/* 47 */     super(conn, sql, catalog);
/*    */   }
/*    */ 
/*    */   
/*    */   public JDBC4PreparedStatement(ConnectionImpl conn, String sql, String catalog, PreparedStatement.ParseInfo cachedParseInfo) throws SQLException {
/* 52 */     super(conn, sql, catalog, cachedParseInfo);
/*    */   }
/*    */   
/*    */   public void setRowId(int parameterIndex, RowId x) throws SQLException {
/* 56 */     JDBC4PreparedStatementHelper.setRowId(this, parameterIndex, x);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setNClob(int parameterIndex, NClob value) throws SQLException {
/* 71 */     JDBC4PreparedStatementHelper.setNClob(this, parameterIndex, value);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
/* 76 */     JDBC4PreparedStatementHelper.setSQLXML(this, parameterIndex, xmlObject);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\JDBC4PreparedStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */