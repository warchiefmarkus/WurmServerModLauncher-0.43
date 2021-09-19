/*    */ package com.wurmonline.server.database.migrations;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.sql.Connection;
/*    */ import java.sql.SQLException;
/*    */ import java.sql.SQLFeatureNotSupportedException;
/*    */ import java.util.logging.Logger;
/*    */ import javax.sql.DataSource;
/*    */ import org.sqlite.SQLiteDataSource;
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
/*    */ public class SqliteFlywayIssue1499Workaround
/*    */   implements DataSource
/*    */ {
/*    */   private final SQLiteDataSource dataSource;
/*    */   private Connection connection;
/*    */   
/*    */   public SqliteFlywayIssue1499Workaround(SQLiteDataSource dataSource) {
/* 29 */     this.dataSource = dataSource;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Connection getConnection() throws SQLException {
/* 35 */     if (this.connection == null || this.connection.isClosed())
/*    */     {
/* 37 */       this.connection = this.dataSource.getConnection();
/*    */     }
/* 39 */     return this.connection;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Connection getConnection(String username, String password) throws SQLException {
/* 45 */     return getConnection();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> T unwrap(Class<T> iface) throws SQLException {
/* 51 */     return (T)this.dataSource.unwrap(iface);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isWrapperFor(Class<?> iface) throws SQLException {
/* 57 */     return SQLiteDataSource.class.equals(iface);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public PrintWriter getLogWriter() throws SQLException {
/* 63 */     return this.dataSource.getLogWriter();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLogWriter(PrintWriter out) throws SQLException {
/* 69 */     this.dataSource.setLogWriter(out);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLoginTimeout(int seconds) throws SQLException {
/* 75 */     this.dataSource.setLoginTimeout(seconds);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getLoginTimeout() throws SQLException {
/* 81 */     return this.dataSource.getLoginTimeout();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Logger getParentLogger() throws SQLFeatureNotSupportedException {
/* 87 */     return this.dataSource.getParentLogger();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\database\migrations\SqliteFlywayIssue1499Workaround.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */