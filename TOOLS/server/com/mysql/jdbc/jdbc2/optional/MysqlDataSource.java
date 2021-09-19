/*     */ package com.mysql.jdbc.jdbc2.optional;
/*     */ 
/*     */ import com.mysql.jdbc.ConnectionPropertiesImpl;
/*     */ import com.mysql.jdbc.NonRegisteringDriver;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Properties;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.Referenceable;
/*     */ import javax.naming.StringRefAddr;
/*     */ import javax.sql.DataSource;
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
/*     */ public class MysqlDataSource
/*     */   extends ConnectionPropertiesImpl
/*     */   implements DataSource, Referenceable, Serializable
/*     */ {
/*  50 */   protected static NonRegisteringDriver mysqlDriver = null;
/*     */   
/*     */   static {
/*     */     try {
/*  54 */       mysqlDriver = new NonRegisteringDriver();
/*  55 */     } catch (Exception E) {
/*  56 */       throw new RuntimeException("Can not load Driver class com.mysql.jdbc.Driver");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  62 */   protected PrintWriter logWriter = null;
/*     */ 
/*     */   
/*  65 */   protected String databaseName = null;
/*     */ 
/*     */   
/*  68 */   protected String encoding = null;
/*     */ 
/*     */   
/*  71 */   protected String hostName = null;
/*     */ 
/*     */   
/*  74 */   protected String password = null;
/*     */ 
/*     */   
/*  77 */   protected String profileSql = "false";
/*     */ 
/*     */   
/*  80 */   protected String url = null;
/*     */ 
/*     */   
/*  83 */   protected String user = null;
/*     */ 
/*     */   
/*     */   protected boolean explicitUrl = false;
/*     */ 
/*     */   
/*  89 */   protected int port = 3306;
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
/*     */   public Connection getConnection() throws SQLException {
/* 107 */     return getConnection(this.user, this.password);
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
/*     */   public Connection getConnection(String userID, String pass) throws SQLException {
/* 125 */     Properties props = new Properties();
/*     */     
/* 127 */     if (userID != null) {
/* 128 */       props.setProperty("user", userID);
/*     */     }
/*     */     
/* 131 */     if (pass != null) {
/* 132 */       props.setProperty("password", pass);
/*     */     }
/*     */     
/* 135 */     exposeAsProperties(props);
/*     */     
/* 137 */     return getConnection(props);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDatabaseName(String dbName) {
/* 147 */     this.databaseName = dbName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDatabaseName() {
/* 156 */     return (this.databaseName != null) ? this.databaseName : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLogWriter(PrintWriter output) throws SQLException {
/* 165 */     this.logWriter = output;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrintWriter getLogWriter() {
/* 174 */     return this.logWriter;
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
/*     */   public void setLoginTimeout(int seconds) throws SQLException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLoginTimeout() {
/* 195 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPassword(String pass) {
/* 205 */     this.password = pass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPort(int p) {
/* 215 */     this.port = p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPort() {
/* 224 */     return this.port;
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
/*     */   public void setPortNumber(int p) {
/* 236 */     setPort(p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPortNumber() {
/* 245 */     return getPort();
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
/*     */   public void setPropertiesViaRef(Reference ref) throws SQLException {
/* 258 */     initializeFromRef(ref);
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
/*     */   public Reference getReference() throws NamingException {
/* 270 */     String factoryName = "com.mysql.jdbc.jdbc2.optional.MysqlDataSourceFactory";
/* 271 */     Reference ref = new Reference(getClass().getName(), factoryName, null);
/* 272 */     ref.add(new StringRefAddr("user", getUser()));
/*     */     
/* 274 */     ref.add(new StringRefAddr("password", this.password));
/*     */     
/* 276 */     ref.add(new StringRefAddr("serverName", getServerName()));
/* 277 */     ref.add(new StringRefAddr("port", "" + getPort()));
/* 278 */     ref.add(new StringRefAddr("databaseName", getDatabaseName()));
/* 279 */     ref.add(new StringRefAddr("url", getUrl()));
/* 280 */     ref.add(new StringRefAddr("explicitUrl", String.valueOf(this.explicitUrl)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 287 */       storeToRef(ref);
/* 288 */     } catch (SQLException sqlEx) {
/* 289 */       throw new NamingException(sqlEx.getMessage());
/*     */     } 
/*     */     
/* 292 */     return ref;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServerName(String serverName) {
/* 302 */     this.hostName = serverName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getServerName() {
/* 311 */     return (this.hostName != null) ? this.hostName : "";
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
/*     */   public void setURL(String url) {
/* 326 */     setUrl(url);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getURL() {
/* 335 */     return getUrl();
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
/*     */   public void setUrl(String url) {
/* 347 */     this.url = url;
/* 348 */     this.explicitUrl = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUrl() {
/* 357 */     if (!this.explicitUrl) {
/* 358 */       String builtUrl = "jdbc:mysql://";
/* 359 */       builtUrl = builtUrl + getServerName() + ":" + getPort() + "/" + getDatabaseName();
/*     */ 
/*     */       
/* 362 */       return builtUrl;
/*     */     } 
/*     */     
/* 365 */     return this.url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUser(String userID) {
/* 375 */     this.user = userID;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUser() {
/* 384 */     return this.user;
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
/*     */   protected Connection getConnection(Properties props) throws SQLException {
/* 400 */     String jdbcUrlToUse = null;
/*     */     
/* 402 */     if (!this.explicitUrl) {
/* 403 */       StringBuffer jdbcUrl = new StringBuffer("jdbc:mysql://");
/*     */       
/* 405 */       if (this.hostName != null) {
/* 406 */         jdbcUrl.append(this.hostName);
/*     */       }
/*     */       
/* 409 */       jdbcUrl.append(":");
/* 410 */       jdbcUrl.append(this.port);
/* 411 */       jdbcUrl.append("/");
/*     */       
/* 413 */       if (this.databaseName != null) {
/* 414 */         jdbcUrl.append(this.databaseName);
/*     */       }
/*     */       
/* 417 */       jdbcUrlToUse = jdbcUrl.toString();
/*     */     } else {
/* 419 */       jdbcUrlToUse = this.url;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 426 */     Properties urlProps = mysqlDriver.parseURL(jdbcUrlToUse, null);
/* 427 */     urlProps.remove("DBNAME");
/* 428 */     urlProps.remove("HOST");
/* 429 */     urlProps.remove("PORT");
/*     */     
/* 431 */     Iterator keys = urlProps.keySet().iterator();
/*     */     
/* 433 */     while (keys.hasNext()) {
/* 434 */       String key = keys.next();
/*     */       
/* 436 */       props.setProperty(key, urlProps.getProperty(key));
/*     */     } 
/*     */     
/* 439 */     return mysqlDriver.connect(jdbcUrlToUse, props);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\mysql\jdbc\jdbc2\optional\MysqlDataSource.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */