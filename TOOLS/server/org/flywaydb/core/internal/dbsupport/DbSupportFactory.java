/*     */ package org.flywaydb.core.internal.dbsupport;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.SQLException;
/*     */ import org.flywaydb.core.api.FlywayException;
/*     */ import org.flywaydb.core.internal.dbsupport.db2.DB2DbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.db2zos.DB2zosDbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.derby.DerbyDbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.h2.H2DbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.hsql.HsqlDbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.mysql.MySQLDbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.oracle.OracleDbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.phoenix.PhoenixDbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.postgresql.PostgreSQLDbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.redshift.RedshfitDbSupportViaPostgreSQLDriver;
/*     */ import org.flywaydb.core.internal.dbsupport.redshift.RedshfitDbSupportViaRedshiftDriver;
/*     */ import org.flywaydb.core.internal.dbsupport.saphana.SapHanaDbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.solid.SolidDbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.sqlite.SQLiteDbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.sqlserver.SQLServerDbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.sybase.ase.SybaseASEDbSupport;
/*     */ import org.flywaydb.core.internal.dbsupport.vertica.VerticaDbSupport;
/*     */ import org.flywaydb.core.internal.util.logging.Log;
/*     */ import org.flywaydb.core.internal.util.logging.LogFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DbSupportFactory
/*     */ {
/*  48 */   private static final Log LOG = LogFactory.getLog(DbSupportFactory.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DbSupport createDbSupport(Connection connection, boolean printInfo) {
/*  65 */     String databaseProductName = getDatabaseProductName(connection);
/*     */     
/*  67 */     if (printInfo) {
/*  68 */       LOG.info("Database: " + getJdbcUrl(connection) + " (" + databaseProductName + ")");
/*     */     }
/*     */     
/*  71 */     if (databaseProductName.startsWith("Apache Derby")) {
/*  72 */       return (DbSupport)new DerbyDbSupport(connection);
/*     */     }
/*  74 */     if (databaseProductName.startsWith("SQLite")) {
/*  75 */       return (DbSupport)new SQLiteDbSupport(connection);
/*     */     }
/*  77 */     if (databaseProductName.startsWith("H2")) {
/*  78 */       return (DbSupport)new H2DbSupport(connection);
/*     */     }
/*  80 */     if (databaseProductName.contains("HSQL Database Engine"))
/*     */     {
/*  82 */       return (DbSupport)new HsqlDbSupport(connection);
/*     */     }
/*  84 */     if (databaseProductName.startsWith("Microsoft SQL Server")) {
/*  85 */       return (DbSupport)new SQLServerDbSupport(connection);
/*     */     }
/*  87 */     if (databaseProductName.contains("MySQL"))
/*     */     {
/*     */ 
/*     */       
/*  91 */       return (DbSupport)new MySQLDbSupport(connection);
/*     */     }
/*  93 */     if (databaseProductName.startsWith("Oracle")) {
/*  94 */       return (DbSupport)new OracleDbSupport(connection);
/*     */     }
/*  96 */     if (databaseProductName.startsWith("PostgreSQL 8")) {
/*     */       RedshfitDbSupportViaPostgreSQLDriver redshfitDbSupportViaPostgreSQLDriver;
/*     */ 
/*     */ 
/*     */       
/* 101 */       if ("RedshiftJDBC".equals(getDriverName(connection))) {
/* 102 */         RedshfitDbSupportViaRedshiftDriver redshfitDbSupportViaRedshiftDriver = new RedshfitDbSupportViaRedshiftDriver(connection);
/*     */       } else {
/* 104 */         redshfitDbSupportViaPostgreSQLDriver = new RedshfitDbSupportViaPostgreSQLDriver(connection);
/*     */       } 
/* 106 */       if (redshfitDbSupportViaPostgreSQLDriver.detect()) {
/* 107 */         return (DbSupport)redshfitDbSupportViaPostgreSQLDriver;
/*     */       }
/*     */     } 
/* 110 */     if (databaseProductName.startsWith("PostgreSQL")) {
/* 111 */       return (DbSupport)new PostgreSQLDbSupport(connection);
/*     */     }
/* 113 */     if (databaseProductName.startsWith("DB2")) {
/* 114 */       if (getDatabaseProductVersion(connection).startsWith("DSN")) {
/* 115 */         return (DbSupport)new DB2zosDbSupport(connection);
/*     */       }
/* 117 */       return (DbSupport)new DB2DbSupport(connection);
/*     */     } 
/*     */     
/* 120 */     if (databaseProductName.startsWith("Vertica")) {
/* 121 */       return (DbSupport)new VerticaDbSupport(connection);
/*     */     }
/* 123 */     if (databaseProductName.contains("solidDB"))
/*     */     {
/*     */ 
/*     */       
/* 127 */       return (DbSupport)new SolidDbSupport(connection);
/*     */     }
/* 129 */     if (databaseProductName.startsWith("Phoenix")) {
/* 130 */       return (DbSupport)new PhoenixDbSupport(connection);
/*     */     }
/*     */ 
/*     */     
/* 134 */     if (databaseProductName.startsWith("ASE") || databaseProductName.startsWith("Adaptive")) {
/* 135 */       return (DbSupport)new SybaseASEDbSupport(connection);
/*     */     }
/* 137 */     if (databaseProductName.startsWith("HDB")) {
/* 138 */       return (DbSupport)new SapHanaDbSupport(connection);
/*     */     }
/*     */     
/* 141 */     throw new FlywayException("Unsupported Database: " + databaseProductName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getJdbcUrl(Connection connection) {
/*     */     try {
/* 153 */       return connection.getMetaData().getURL();
/* 154 */     } catch (SQLException e) {
/* 155 */       throw new FlywayException("Unable to retrieve the Jdbc connection Url!", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getDatabaseProductName(Connection connection) {
/*     */     try {
/* 167 */       DatabaseMetaData databaseMetaData = connection.getMetaData();
/* 168 */       if (databaseMetaData == null) {
/* 169 */         throw new FlywayException("Unable to read database metadata while it is null!");
/*     */       }
/*     */       
/* 172 */       String databaseProductName = databaseMetaData.getDatabaseProductName();
/* 173 */       if (databaseProductName == null) {
/* 174 */         throw new FlywayException("Unable to determine database. Product name is null.");
/*     */       }
/*     */       
/* 177 */       int databaseMajorVersion = databaseMetaData.getDatabaseMajorVersion();
/* 178 */       int databaseMinorVersion = databaseMetaData.getDatabaseMinorVersion();
/*     */       
/* 180 */       return databaseProductName + " " + databaseMajorVersion + "." + databaseMinorVersion;
/* 181 */     } catch (SQLException e) {
/* 182 */       throw new FlywayException("Error while determining database product name", e);
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
/*     */   private static String getDatabaseProductVersion(Connection connection) {
/*     */     try {
/* 196 */       DatabaseMetaData databaseMetaData = connection.getMetaData();
/* 197 */       if (databaseMetaData == null) {
/* 198 */         throw new FlywayException("Unable to read database metadata while it is null!");
/*     */       }
/*     */       
/* 201 */       String databaseProductVersion = databaseMetaData.getDatabaseProductVersion();
/* 202 */       if (databaseProductVersion == null) {
/* 203 */         throw new FlywayException("Unable to determine database. Product version is null.");
/*     */       }
/*     */ 
/*     */       
/* 207 */       return databaseProductVersion;
/* 208 */     } catch (SQLException e) {
/* 209 */       throw new FlywayException("Error while determining database product version", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getDriverName(Connection connection) {
/*     */     try {
/* 221 */       DatabaseMetaData databaseMetaData = connection.getMetaData();
/* 222 */       if (databaseMetaData == null) {
/* 223 */         throw new FlywayException("Unable to read database metadata while it is null!");
/*     */       }
/*     */       
/* 226 */       String driverName = databaseMetaData.getDriverName();
/* 227 */       if (driverName == null) {
/* 228 */         throw new FlywayException("Unable to determine JDBC  driver name. JDBC driver name is null.");
/*     */       }
/*     */       
/* 231 */       return driverName;
/* 232 */     } catch (SQLException e) {
/* 233 */       throw new FlywayException("Error while determining JDBC driver name", e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\dbsupport\DbSupportFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */