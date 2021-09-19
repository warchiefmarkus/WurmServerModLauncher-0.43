/*     */ package com.wurmonline.server;
/*     */ 
/*     */ import com.wurmonline.server.creatures.CreaturePos;
/*     */ import com.wurmonline.server.database.ConnectionFactory;
/*     */ import com.wurmonline.server.database.MysqlConnectionFactory;
/*     */ import com.wurmonline.server.database.SqliteConnectionFactory;
/*     */ import com.wurmonline.server.database.WurmDatabaseSchema;
/*     */ import com.wurmonline.server.database.migrations.MigrationResult;
/*     */ import com.wurmonline.server.database.migrations.MigrationStrategy;
/*     */ import com.wurmonline.server.database.migrations.MysqlMigrationStrategy;
/*     */ import com.wurmonline.server.database.migrations.SqliteMigrationStrategy;
/*     */ import com.wurmonline.server.gui.folders.DistEntity;
/*     */ import com.wurmonline.server.gui.folders.Folders;
/*     */ import com.wurmonline.server.items.DbItem;
/*     */ import java.nio.file.Path;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.EnumMap;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import org.sqlite.SQLiteConfig;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DbConnector
/*     */   implements TimeConstants
/*     */ {
/*  78 */   private static final Logger logger = Logger.getLogger(DbConnector.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean sqlite = true;
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isInitialized = false;
/*     */ 
/*     */   
/*     */   private static final String SQLITE_JDBC_DRIVER = "org.sqlite.JDBC";
/*     */ 
/*     */   
/*  92 */   private static EnumMap<WurmDatabaseSchema, DbConnector> CONNECTORS = new EnumMap<>(WurmDatabaseSchema.class);
/*     */   
/*     */   private static boolean isTrackingOpenDatabaseResources = false;
/*     */   
/*  96 */   private static final Path MIGRATIONS_DIR = Folders.getDist().getPathFor(DistEntity.Migrations);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Connection connection;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   private long lastUsed = System.currentTimeMillis();
/*     */ 
/*     */   
/*     */   private final String loggingName;
/*     */ 
/*     */   
/*     */   private final ConnectionFactory connectionFactory;
/*     */   
/* 115 */   private static final Pattern portPattern = Pattern.compile(":?([0-9]+)");
/* 116 */   private static final SQLiteConfig config = new SQLiteConfig();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static MigrationStrategy MIGRATION_STRATEGY;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class ConfigHelper<B extends ConnectionFactory>
/*     */   {
/*     */     private ConfigHelper() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean loadIsTrackingOpenDatabaseResources() {
/* 140 */       if (Constants.trackOpenDatabaseResources) {
/* 141 */         DbConnector.logger.warning("Cannot set tracking of open database resources as this is not supported for this driver");
/*     */       }
/* 143 */       return false;
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
/*     */     EnumMap<WurmDatabaseSchema, DbConnector> buildConnectors() {
/* 155 */       DbConnector.isTrackingOpenDatabaseResources = loadIsTrackingOpenDatabaseResources();
/* 156 */       if (Constants.usePooledDb)
/*     */       {
/* 158 */         DbConnector.logger.warning("Database connection pooling is set to true, but is not currently supported");
/*     */       }
/*     */       
/* 161 */       EnumMap<WurmDatabaseSchema, B> factories = new EnumMap<>(WurmDatabaseSchema.class);
/* 162 */       for (WurmDatabaseSchema schema : WurmDatabaseSchema.values()) {
/* 163 */         factories.put(schema, factoryForSchema(schema));
/*     */       }
/*     */       
/* 166 */       EnumMap<WurmDatabaseSchema, DbConnector> newConnectors = new EnumMap<>(WurmDatabaseSchema.class);
/* 167 */       newConnectors.put(WurmDatabaseSchema.PLAYERS, new DbConnector((ConnectionFactory)factories
/* 168 */             .get(WurmDatabaseSchema.PLAYERS), "playerDbcon")
/*     */           {
/*     */             
/*     */             protected void beforeStaleClose() throws SQLException
/*     */             {
/* 173 */               CreaturePos.clearBatches();
/*     */             }
/*     */           });
/*     */       
/* 177 */       newConnectors.put(WurmDatabaseSchema.CREATURES, new DbConnector((ConnectionFactory)factories
/* 178 */             .get(WurmDatabaseSchema.CREATURES), "creatureDbcon")
/*     */           {
/*     */             
/*     */             protected void beforeStaleClose() throws SQLException
/*     */             {
/* 183 */               CreaturePos.clearBatches();
/*     */             }
/*     */           });
/*     */       
/* 187 */       newConnectors.put(WurmDatabaseSchema.ITEMS, new DbConnector((ConnectionFactory)factories
/* 188 */             .get(WurmDatabaseSchema.ITEMS), "itemdbcon")
/*     */           {
/*     */             protected void beforeStaleClose() throws SQLException
/*     */             {
/* 192 */               DbItem.clearBatches();
/*     */             }
/*     */           });
/*     */       
/* 196 */       newConnectors.put(WurmDatabaseSchema.TEMPLATES, new DbConnector((ConnectionFactory)factories
/* 197 */             .get(WurmDatabaseSchema.TEMPLATES), "templateDbcon"));
/*     */       
/* 199 */       newConnectors.put(WurmDatabaseSchema.ZONES, new DbConnector((ConnectionFactory)factories
/* 200 */             .get(WurmDatabaseSchema.ZONES), "zonesDbcon"));
/*     */       
/* 202 */       newConnectors.put(WurmDatabaseSchema.ECONOMY, new DbConnector((ConnectionFactory)factories
/* 203 */             .get(WurmDatabaseSchema.ECONOMY), "economyDbcon"));
/*     */       
/* 205 */       newConnectors.put(WurmDatabaseSchema.DEITIES, new DbConnector((ConnectionFactory)factories
/* 206 */             .get(WurmDatabaseSchema.DEITIES), "deityDbcon"));
/*     */       
/* 208 */       newConnectors.put(WurmDatabaseSchema.LOGIN, new DbConnector((ConnectionFactory)factories
/* 209 */             .get(WurmDatabaseSchema.LOGIN), "loginDbcon"));
/*     */       
/* 211 */       newConnectors.put(WurmDatabaseSchema.LOGS, new DbConnector((ConnectionFactory)factories
/* 212 */             .get(WurmDatabaseSchema.LOGS), "logsDbcon"));
/* 213 */       return newConnectors;
/*     */     }
/*     */ 
/*     */     
/*     */     public abstract B factoryForSchema(WurmDatabaseSchema param1WurmDatabaseSchema);
/*     */     
/*     */     abstract MigrationStrategy newMigrationStrategy();
/*     */   }
/*     */   
/*     */   private static class SqliteConfigHelper
/*     */     extends ConfigHelper<SqliteConnectionFactory>
/*     */   {
/*     */     SqliteConfigHelper() {
/* 226 */       DbConnector.config.setJournalMode(SQLiteConfig.JournalMode.WAL);
/* 227 */       DbConnector.config.setSynchronous(SQLiteConfig.SynchronousMode.NORMAL);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public SqliteConnectionFactory factoryForSchema(WurmDatabaseSchema schema) {
/* 233 */       return new SqliteConnectionFactory(Constants.dbHost, schema, DbConnector.config);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     MigrationStrategy newMigrationStrategy() {
/* 239 */       List<SqliteConnectionFactory> sqliteConnectionFactories = new ArrayList<>();
/* 240 */       for (DbConnector connector : DbConnector.CONNECTORS.values()) {
/*     */ 
/*     */         
/* 243 */         SqliteConnectionFactory factory = (SqliteConnectionFactory)connector.connectionFactory;
/* 244 */         sqliteConnectionFactories.add(factory);
/*     */       } 
/* 246 */       return (MigrationStrategy)new SqliteMigrationStrategy(sqliteConnectionFactories, DbConnector.MIGRATIONS_DIR);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class MysqlConfigHelper
/*     */     extends ConfigHelper<MysqlConnectionFactory>
/*     */   {
/*     */     private MysqlConfigHelper() {}
/*     */     
/*     */     protected boolean loadIsTrackingOpenDatabaseResources() {
/* 257 */       return Constants.trackOpenDatabaseResources;
/*     */     }
/*     */ 
/*     */     
/*     */     public MysqlConnectionFactory factoryForSchema(WurmDatabaseSchema schema) {
/* 262 */       return new MysqlConnectionFactory(Constants.dbHost, DbConnector.asPort(Constants.dbPort).intValue(), Constants.dbUser, Constants.dbPass, schema);
/*     */     }
/*     */ 
/*     */     
/*     */     public MigrationStrategy newMigrationStrategy() {
/* 267 */       return (MigrationStrategy)new MysqlMigrationStrategy(
/* 268 */           (MysqlConnectionFactory)((DbConnector)DbConnector.CONNECTORS.get(MysqlMigrationStrategy.MIGRATION_SCHEMA)).connectionFactory);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void initialize() {
/* 277 */     initialize(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void initialize(boolean reinitialize) {
/*     */     ConfigHelper<? extends ConnectionFactory> configHelper;
/*     */     String driver;
/* 287 */     if (isInitialized && !reinitialize) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 293 */     if (isUseSqlite()) {
/*     */       
/* 295 */       configHelper = new SqliteConfigHelper();
/* 296 */       driver = "org.sqlite.JDBC";
/*     */     }
/*     */     else {
/*     */       
/* 300 */       configHelper = new MysqlConfigHelper();
/* 301 */       driver = Constants.dbDriver;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 307 */       Class.forName(driver);
/* 308 */     } catch (ClassNotFoundException e) {
/*     */       
/* 310 */       logger.warning("No class found for database driver: " + Constants.dbDriver);
/* 311 */       e.printStackTrace();
/*     */     } 
/*     */     
/* 314 */     CONNECTORS = configHelper.buildConnectors();
/* 315 */     MIGRATION_STRATEGY = configHelper.newMigrationStrategy();
/* 316 */     setInitialized(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DbConnector(String driver, String host, String port, WurmDatabaseSchema schema, String user, String password, String loggingName) {
/* 325 */     if (isUseSqlite()) {
/* 326 */       config.setJournalMode(SQLiteConfig.JournalMode.WAL);
/* 327 */       config.setSynchronous(SQLiteConfig.SynchronousMode.NORMAL);
/* 328 */       this.connectionFactory = (ConnectionFactory)new SqliteConnectionFactory(host, schema, config);
/*     */     } else {
/* 330 */       this
/* 331 */         .connectionFactory = (ConnectionFactory)new MysqlConnectionFactory(host, asPort(port).intValue(), user, password, schema);
/*     */     } 
/* 333 */     this.loggingName = loggingName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DbConnector(ConnectionFactory connectionFactory, String loggingName) {
/* 343 */     this.connectionFactory = connectionFactory;
/* 344 */     this.loggingName = loggingName;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isUseSqlite() {
/* 349 */     return sqlite;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void beforeStaleClose() throws SQLException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void refreshDbConnection() throws SQLException {
/* 365 */     if (this.connectionFactory.isStale(this.lastUsed, this.connection)) {
/*     */       
/* 367 */       logger.log(Level.INFO, "Recreating " + this.loggingName);
/* 368 */       if (this.connection != null) {
/*     */         
/*     */         try {
/*     */           
/* 372 */           beforeStaleClose();
/* 373 */           attemptClose();
/*     */         }
/* 375 */         catch (SQLException e) {
/*     */ 
/*     */           
/* 378 */           e.printStackTrace();
/* 379 */           logger.log(Level.WARNING, "Unable to perform pre-close on stale " + this.loggingName, e);
/*     */         } 
/*     */       }
/*     */     } 
/* 383 */     if (!this.connectionFactory.isValid(this.connection)) {
/*     */       
/*     */       try {
/* 386 */         this.connection = this.connectionFactory.createConnection();
/*     */       }
/* 388 */       catch (Exception e) {
/*     */         
/* 390 */         e.printStackTrace();
/* 391 */         logger.log(Level.WARNING, "Problem opening the " + this.loggingName, e);
/*     */       } 
/*     */     }
/* 394 */     this.lastUsed = System.currentTimeMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean hasPendingMigrations() {
/* 403 */     return MIGRATION_STRATEGY.hasPendingMigrations();
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
/*     */   public static MigrationResult performMigrations() {
/* 416 */     return MIGRATION_STRATEGY.migrate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Connection getLoginDbCon() throws SQLException {
/* 426 */     return refreshConnectionForSchema(WurmDatabaseSchema.LOGIN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Connection getCreatureDbCon() throws SQLException {
/* 437 */     return refreshConnectionForSchema(WurmDatabaseSchema.CREATURES);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Connection getDeityDbCon() throws SQLException {
/* 447 */     return refreshConnectionForSchema(WurmDatabaseSchema.DEITIES);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Connection getEconomyDbCon() throws SQLException {
/* 457 */     return refreshConnectionForSchema(WurmDatabaseSchema.ECONOMY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Connection getPlayerDbCon() throws SQLException {
/* 467 */     return refreshConnectionForSchema(WurmDatabaseSchema.PLAYERS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Connection getItemDbCon() throws SQLException {
/* 477 */     return refreshConnectionForSchema(WurmDatabaseSchema.ITEMS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Connection getTemplateDbCon() throws SQLException {
/* 487 */     return refreshConnectionForSchema(WurmDatabaseSchema.TEMPLATES);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Connection getZonesDbCon() throws SQLException {
/* 497 */     return refreshConnectionForSchema(WurmDatabaseSchema.ZONES);
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
/*     */   public static Connection getLogsDbCon() throws SQLException {
/* 509 */     return refreshConnectionForSchema(WurmDatabaseSchema.LOGS);
/*     */   }
/*     */   
/*     */   private void attemptClose() {
/* 513 */     if (this.connection != null) {
/*     */ 
/*     */       
/*     */       try {
/* 517 */         this.connection.close();
/*     */       }
/* 519 */       catch (SQLException ex) {
/*     */         
/* 521 */         ex.printStackTrace();
/* 522 */         logger.log(Level.WARNING, "Problem closing the " + this.loggingName, ex);
/*     */       } 
/* 524 */       this.connection = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void closeAll() {
/* 530 */     logger.info("Starting to close all Database Connections.");
/* 531 */     CONNECTORS.values().forEach(DbConnector::attemptClose);
/* 532 */     logger.info("Finished closing all Database Connections.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void returnConnection(@Nullable Connection aConnection) {
/* 542 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/* 544 */       logger.finest("Returning Connection: " + aConnection);
/*     */     }
/* 546 */     if (!isTrackingOpenDatabaseResources && aConnection != null)
/*     */     {
/* 548 */       MysqlConnectionFactory.logActiveStatementCount(aConnection);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static Connection refreshConnectionForSchema(WurmDatabaseSchema schema) throws SQLException {
/* 554 */     if (!isInitialized())
/*     */     {
/* 556 */       initialize();
/*     */     }
/* 558 */     DbConnector connector = CONNECTORS.get(schema);
/* 559 */     connector.refreshDbConnection();
/* 560 */     return connector.connection;
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
/*     */   public static Connection getConnectionForSchema(@Nonnull WurmDatabaseSchema aSchema) throws SQLException {
/* 576 */     if (logger.isLoggable(Level.FINER))
/*     */     {
/* 578 */       logger.finer("Getting database connection for schema: " + aSchema);
/*     */     }
/* 580 */     if (!isInitialized())
/* 581 */       initialize(); 
/* 582 */     DbConnector connector = CONNECTORS.get(aSchema);
/* 583 */     if (connector == null) {
/*     */ 
/*     */ 
/*     */       
/* 587 */       assert false : aSchema;
/* 588 */       logger.warning("Returning null for an unexpected WurmDatabaseSchema: " + aSchema);
/* 589 */       return null;
/*     */     } 
/* 591 */     if (connector.connection == null) {
/*     */       
/* 593 */       logger.warning("Null connection found for connector " + connector.loggingName);
/* 594 */       connector.refreshDbConnection();
/*     */     } 
/* 596 */     return connector.connection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setUseSqlite(boolean sqlite) {
/* 607 */     DbConnector.sqlite = sqlite;
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
/*     */   private static Integer asPort(@Nullable String portProperty) {
/* 620 */     if (portProperty != null && !portProperty.isEmpty()) {
/*     */       
/* 622 */       Matcher m = portPattern.matcher(portProperty);
/* 623 */       if (m.matches()) {
/*     */ 
/*     */         
/*     */         try {
/* 627 */           return Integer.valueOf(Integer.parseInt(m.group(1)));
/* 628 */         } catch (NumberFormatException e) {
/*     */ 
/*     */           
/* 631 */           logger.warning("Unexpected error, could not converted matched port number into integer: " + portProperty);
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 637 */         logger.warning("Database port property does not match expected pattern: " + portProperty);
/*     */       } 
/*     */     } 
/* 640 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isInitialized() {
/* 650 */     return isInitialized;
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
/*     */   private static void setInitialized(boolean isInitialized) {
/* 663 */     DbConnector.isInitialized = isInitialized;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\DbConnector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */