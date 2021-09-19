/*     */ package com.wurmonline.server.database;
/*     */ 
/*     */ import com.mysql.jdbc.ConnectionImpl;
/*     */ import com.wurmonline.server.Constants;
/*     */ import com.wurmonline.shared.exceptions.WurmException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.Driver;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MysqlConnectionFactory
/*     */   extends ConnectionFactory
/*     */ {
/*  23 */   private static final Logger logger = Logger.getLogger(MysqlConnectionFactory.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String DB_CACHE_PREP_STMTS = "&cachePrepStmts=true&useServerPrepStmts=true&prepStmtCacheSqlLimit=512&prepStmtCacheSize=100&useCompression=false&allowMultiQueries=true&elideSetAutoCommits=true&maintainTimeStats=false";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String DB_GATHER_STATS = "&gatherPerfMetrics=true&reportMetricsIntervalMillis=600000&profileSQL=false&useUsageAdvisor=false&useNanosForElapsedTime=false";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int DATABASE_IS_VALID_TIMEOUT = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String user;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String password;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MysqlConnectionFactory(String host, int port, String user, String password, WurmDatabaseSchema schema) {
/*  76 */     super(createConnectionUrl(host, port, schema), schema);
/*  77 */     this.user = user;
/*  78 */     this.password = password;
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
/*     */   private static StringBuilder addOptionalJdbcConnectionProperties(StringBuilder urlBuilder) {
/*  94 */     if (Constants.trackOpenDatabaseResources) {
/*  95 */       urlBuilder.append("dontTrackOpenResources=false");
/*     */     } else {
/*  97 */       urlBuilder.append("dontTrackOpenResources=true");
/*  98 */     }  if (Constants.usePrepStmts)
/*  99 */       urlBuilder.append("&cachePrepStmts=true&useServerPrepStmts=true&prepStmtCacheSqlLimit=512&prepStmtCacheSize=100&useCompression=false&allowMultiQueries=true&elideSetAutoCommits=true&maintainTimeStats=false"); 
/* 100 */     if (Constants.gatherDbStats)
/* 101 */       urlBuilder.append("&gatherPerfMetrics=true&reportMetricsIntervalMillis=600000&profileSQL=false&useUsageAdvisor=false&useNanosForElapsedTime=false"); 
/* 102 */     return urlBuilder;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String createConnectionUrl(String host, int port, WurmDatabaseSchema schema) {
/* 108 */     StringBuilder urlBuilder = new StringBuilder();
/* 109 */     urlBuilder.append("jdbc:mysql://");
/* 110 */     urlBuilder.append(host);
/* 111 */     urlBuilder.append(":");
/* 112 */     urlBuilder.append(port);
/* 113 */     urlBuilder.append("/");
/* 114 */     urlBuilder.append(schema.getDatabase());
/* 115 */     urlBuilder.append("?");
/* 116 */     return addOptionalJdbcConnectionProperties(urlBuilder).toString();
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
/*     */   public Connection createConnection() throws SQLException {
/* 129 */     Driver driver = DriverManager.getDriver(getUrl());
/* 130 */     Properties connectionInfo = new Properties();
/* 131 */     connectionInfo.put("user", this.user);
/* 132 */     connectionInfo.put("password", this.password);
/* 133 */     Connection con = driver.connect(getUrl(), connectionInfo);
/* 134 */     if (logger.isLoggable(Level.FINE))
/*     */     {
/* 136 */       logger.fine("JDBC Driver Class: " + driver.getClass() + ", version: " + driver.getMajorVersion() + '.' + driver
/* 137 */           .getMinorVersion());
/*     */     }
/* 139 */     return con;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValid(@Nullable Connection con) throws SQLException {
/* 145 */     return (con != null && con.isValid(0));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStale(long lastUsed, @Nullable Connection connection) throws SQLException {
/* 151 */     return (System.currentTimeMillis() - lastUsed > 3600000L || (connection != null && 
/* 152 */       !connection.isValid(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUser() {
/* 157 */     return this.user;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPassword() {
/* 162 */     return this.password;
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
/*     */   public static void logActiveStatementCount(@Nonnull Connection aConnection) {
/* 174 */     if (aConnection instanceof ConnectionImpl) {
/*     */       
/* 176 */       int lActiveStatementCount = ((ConnectionImpl)aConnection).getActiveStatementCount();
/* 177 */       if (lActiveStatementCount > 0)
/*     */       {
/* 179 */         logger.log(Level.WARNING, "Returned connection: " + aConnection.getClass() + ", active statement count: " + lActiveStatementCount, (Throwable)new WurmException("SQL Statements still open when returning connection"));
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\database\MysqlConnectionFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */