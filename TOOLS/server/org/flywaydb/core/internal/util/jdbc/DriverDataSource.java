/*     */ package org.flywaydb.core.internal.util.jdbc;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.sql.Connection;
/*     */ import java.sql.Driver;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Logger;
/*     */ import javax.sql.DataSource;
/*     */ import org.flywaydb.core.api.FlywayException;
/*     */ import org.flywaydb.core.internal.util.ClassUtils;
/*     */ import org.flywaydb.core.internal.util.FeatureDetector;
/*     */ import org.flywaydb.core.internal.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DriverDataSource
/*     */   implements DataSource
/*     */ {
/*     */   private static final String MARIADB_JDBC_DRIVER = "org.mariadb.jdbc.Driver";
/*     */   private static final String MYSQL_JDBC_URL_PREFIX = "jdbc:mysql:";
/*     */   private static final String ORACLE_JDBC_URL_PREFIX = "jdbc:oracle:";
/*     */   private static final String MYSQL_5_JDBC_DRIVER = "com.mysql.jdbc.Driver";
/*     */   private Driver driver;
/*     */   private final String url;
/*     */   private final String user;
/*     */   private final String password;
/*     */   private final String[] initSqls;
/*     */   private final Properties defaultProps;
/*     */   private final ClassLoader classLoader;
/*     */   private boolean singleConnectionMode;
/*     */   private Connection originalSingleConnection;
/*     */   private Connection uncloseableSingleConnection;
/*     */   
/*     */   public DriverDataSource(ClassLoader classLoader, String driverClass, String url, String user, String password, String... initSqls) throws FlywayException {
/* 106 */     this.classLoader = classLoader;
/* 107 */     this.url = detectFallbackUrl(url);
/*     */     
/* 109 */     if (!StringUtils.hasLength(driverClass)) {
/* 110 */       driverClass = detectDriverForUrl(url);
/* 111 */       if (!StringUtils.hasLength(driverClass)) {
/* 112 */         throw new FlywayException("Unable to autodetect JDBC driver for url: " + url);
/*     */       }
/*     */     } 
/*     */     
/* 116 */     this.defaultProps = detectPropsForUrl(url);
/*     */     
/*     */     try {
/* 119 */       this.driver = (Driver)ClassUtils.instantiate(driverClass, classLoader);
/* 120 */     } catch (Exception e) {
/* 121 */       String backupDriverClass = detectBackupDriverForUrl(url);
/* 122 */       if (backupDriverClass == null) {
/* 123 */         throw new FlywayException("Unable to instantiate JDBC driver: " + driverClass, e);
/*     */       }
/*     */       try {
/* 126 */         this.driver = (Driver)ClassUtils.instantiate(backupDriverClass, classLoader);
/* 127 */       } catch (Exception e1) {
/*     */         
/* 129 */         throw new FlywayException("Unable to instantiate JDBC driver: " + driverClass, e);
/*     */       } 
/*     */     } 
/*     */     
/* 133 */     this.user = detectFallbackUser(user);
/* 134 */     this.password = detectFallbackPassword(password);
/*     */     
/* 136 */     if (initSqls == null) {
/* 137 */       initSqls = new String[0];
/*     */     }
/* 139 */     this.initSqls = initSqls;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String detectFallbackUrl(String url) {
/* 149 */     if (!StringUtils.hasText(url)) {
/*     */       
/* 151 */       String boxfuseDatabaseUrl = System.getenv("BOXFUSE_DATABASE_URL");
/* 152 */       if (StringUtils.hasText(boxfuseDatabaseUrl)) {
/* 153 */         return boxfuseDatabaseUrl;
/*     */       }
/*     */       
/* 156 */       throw new FlywayException("Missing required JDBC URL. Unable to create DataSource!");
/*     */     } 
/*     */     
/* 159 */     if (!url.toLowerCase().startsWith("jdbc:")) {
/* 160 */       throw new FlywayException("Invalid JDBC URL (should start with jdbc:) : " + url);
/*     */     }
/* 162 */     return url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String detectFallbackUser(String user) {
/* 172 */     if (!StringUtils.hasText(user)) {
/*     */       
/* 174 */       String boxfuseDatabaseUser = System.getenv("BOXFUSE_DATABASE_USER");
/* 175 */       if (StringUtils.hasText(boxfuseDatabaseUser)) {
/* 176 */         return boxfuseDatabaseUser;
/*     */       }
/*     */     } 
/* 179 */     return user;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String detectFallbackPassword(String password) {
/* 189 */     if (!StringUtils.hasText(password)) {
/*     */       
/* 191 */       String boxfuseDatabasePassword = System.getenv("BOXFUSE_DATABASE_PASSWORD");
/* 192 */       if (StringUtils.hasText(boxfuseDatabasePassword)) {
/* 193 */         return boxfuseDatabasePassword;
/*     */       }
/*     */     } 
/* 196 */     return password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Properties detectPropsForUrl(String url) {
/* 206 */     Properties result = new Properties();
/*     */     
/* 208 */     if (url.startsWith("jdbc:oracle:")) {
/* 209 */       String osUser = System.getProperty("user.name");
/* 210 */       result.put("v$session.osuser", osUser.substring(0, Math.min(osUser.length(), 30)));
/* 211 */       result.put("v$session.program", "Flyway by Boxfuse");
/*     */     } 
/*     */     
/* 214 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String detectBackupDriverForUrl(String url) {
/* 224 */     if (url.startsWith("jdbc:mysql:")) {
/* 225 */       if (ClassUtils.isPresent("com.mysql.jdbc.Driver", this.classLoader)) {
/* 226 */         return "com.mysql.jdbc.Driver";
/*     */       }
/*     */       
/* 229 */       return "org.mariadb.jdbc.Driver";
/*     */     } 
/*     */     
/* 232 */     if (url.startsWith("jdbc:redshift:")) {
/* 233 */       return "com.amazon.redshift.jdbc4.Driver";
/*     */     }
/*     */     
/* 236 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String detectDriverForUrl(String url) {
/* 246 */     if (url.startsWith("jdbc:db2:")) {
/* 247 */       return "com.ibm.db2.jcc.DB2Driver";
/*     */     }
/*     */     
/* 250 */     if (url.startsWith("jdbc:derby://")) {
/* 251 */       return "org.apache.derby.jdbc.ClientDriver";
/*     */     }
/*     */     
/* 254 */     if (url.startsWith("jdbc:derby:")) {
/* 255 */       return "org.apache.derby.jdbc.EmbeddedDriver";
/*     */     }
/*     */     
/* 258 */     if (url.startsWith("jdbc:h2:")) {
/* 259 */       return "org.h2.Driver";
/*     */     }
/*     */     
/* 262 */     if (url.startsWith("jdbc:hsqldb:")) {
/* 263 */       return "org.hsqldb.jdbcDriver";
/*     */     }
/*     */     
/* 266 */     if (url.startsWith("jdbc:sqlite:")) {
/* 267 */       this.singleConnectionMode = true;
/* 268 */       if ((new FeatureDetector(this.classLoader)).isAndroidAvailable()) {
/* 269 */         return "org.sqldroid.SQLDroidDriver";
/*     */       }
/* 271 */       return "org.sqlite.JDBC";
/*     */     } 
/*     */     
/* 274 */     if (url.startsWith("jdbc:sqldroid:")) {
/* 275 */       return "org.sqldroid.SQLDroidDriver";
/*     */     }
/*     */     
/* 278 */     if (url.startsWith("jdbc:mysql:")) {
/* 279 */       return "com.mysql.cj.jdbc.Driver";
/*     */     }
/*     */     
/* 282 */     if (url.startsWith("jdbc:mariadb:")) {
/* 283 */       return "org.mariadb.jdbc.Driver";
/*     */     }
/*     */     
/* 286 */     if (url.startsWith("jdbc:google:")) {
/* 287 */       return "com.mysql.jdbc.GoogleDriver";
/*     */     }
/*     */     
/* 290 */     if (url.startsWith("jdbc:oracle:")) {
/* 291 */       return "oracle.jdbc.OracleDriver";
/*     */     }
/*     */     
/* 294 */     if (url.startsWith("jdbc:phoenix")) {
/* 295 */       return "org.apache.phoenix.jdbc.PhoenixDriver";
/*     */     }
/*     */     
/* 298 */     if (url.startsWith("jdbc:postgresql:"))
/*     */     {
/* 300 */       return "org.postgresql.Driver";
/*     */     }
/*     */     
/* 303 */     if (url.startsWith("jdbc:redshift:"))
/*     */     {
/* 305 */       return "com.amazon.redshift.jdbc41.Driver";
/*     */     }
/*     */     
/* 308 */     if (url.startsWith("jdbc:jtds:")) {
/* 309 */       return "net.sourceforge.jtds.jdbc.Driver";
/*     */     }
/*     */     
/* 312 */     if (url.startsWith("jdbc:sqlserver:")) {
/* 313 */       return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
/*     */     }
/*     */     
/* 316 */     if (url.startsWith("jdbc:vertica:")) {
/* 317 */       return "com.vertica.jdbc.Driver";
/*     */     }
/*     */     
/* 320 */     if (url.startsWith("jdbc:sap:")) {
/* 321 */       return "com.sap.db.jdbc.Driver";
/*     */     }
/*     */     
/* 324 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Driver getDriver() {
/* 331 */     return this.driver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUrl() {
/* 338 */     return this.url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUser() {
/* 345 */     return this.user;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPassword() {
/* 352 */     return this.password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getInitSqls() {
/* 359 */     return this.initSqls;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection() throws SQLException {
/* 370 */     return getConnectionFromDriver(getUser(), getPassword());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection(String username, String password) throws SQLException {
/* 381 */     return getConnectionFromDriver(username, password);
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
/*     */   protected Connection getConnectionFromDriver(String username, String password) throws SQLException {
/*     */     Connection connection;
/* 396 */     if (this.singleConnectionMode && this.uncloseableSingleConnection != null) {
/* 397 */       return this.uncloseableSingleConnection;
/*     */     }
/*     */     
/* 400 */     Properties props = new Properties(this.defaultProps);
/* 401 */     if (username != null) {
/* 402 */       props.setProperty("user", username);
/*     */     }
/* 404 */     if (password != null) {
/* 405 */       props.setProperty("password", password);
/*     */     }
/*     */     
/*     */     try {
/* 409 */       connection = this.driver.connect(this.url, props);
/* 410 */     } catch (SQLException e) {
/* 411 */       throw new FlywayException("Unable to obtain Jdbc connection from DataSource (" + this.url + ") for user '" + this.user + "': " + e
/* 412 */           .getMessage(), e);
/*     */     } 
/*     */     
/* 415 */     for (String initSql : this.initSqls) {
/* 416 */       Statement statement = null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 425 */     if (this.singleConnectionMode) {
/* 426 */       this.originalSingleConnection = connection;
/* 427 */       InvocationHandler suppressCloseHandler = new SuppressCloseHandler(this.originalSingleConnection);
/* 428 */       this
/* 429 */         .uncloseableSingleConnection = (Connection)Proxy.newProxyInstance(this.classLoader, new Class[] { Connection.class }, suppressCloseHandler);
/* 430 */       return this.uncloseableSingleConnection;
/*     */     } 
/*     */     
/* 433 */     return connection;
/*     */   }
/*     */   
/*     */   public int getLoginTimeout() throws SQLException {
/* 437 */     return 0;
/*     */   }
/*     */   
/*     */   public void setLoginTimeout(int timeout) throws SQLException {
/* 441 */     throw new UnsupportedOperationException("setLoginTimeout");
/*     */   }
/*     */   
/*     */   public PrintWriter getLogWriter() {
/* 445 */     throw new UnsupportedOperationException("getLogWriter");
/*     */   }
/*     */   
/*     */   public void setLogWriter(PrintWriter pw) throws SQLException {
/* 449 */     throw new UnsupportedOperationException("setLogWriter");
/*     */   }
/*     */   
/*     */   public <T> T unwrap(Class<T> iface) throws SQLException {
/* 453 */     throw new UnsupportedOperationException("unwrap");
/*     */   }
/*     */   
/*     */   public boolean isWrapperFor(Class<?> iface) throws SQLException {
/* 457 */     return DataSource.class.equals(iface);
/*     */   }
/*     */   
/*     */   public Logger getParentLogger() {
/* 461 */     throw new UnsupportedOperationException("getParentLogger");
/*     */   }
/*     */   
/*     */   private static class SuppressCloseHandler implements InvocationHandler {
/*     */     private final Connection connection;
/*     */     
/*     */     public SuppressCloseHandler(Connection connection) {
/* 468 */       this.connection = connection;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 473 */       if (!"close".equals(method.getName())) {
/* 474 */         return method.invoke(this.connection, args);
/*     */       }
/*     */       
/* 477 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 485 */     this.uncloseableSingleConnection = null;
/* 486 */     JdbcUtils.closeConnection(this.originalSingleConnection);
/* 487 */     this.originalSingleConnection = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\jdbc\DriverDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */