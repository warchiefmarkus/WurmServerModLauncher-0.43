/*     */ package com.wurmonline.server.database;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.nio.file.Path;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Locale;
/*     */ import javax.annotation.Nullable;
/*     */ import org.sqlite.SQLiteConfig;
/*     */ import org.sqlite.SQLiteDataSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SqliteConnectionFactory
/*     */   extends ConnectionFactory
/*     */ {
/*     */   private final SQLiteConfig config;
/*     */   private final Path fileDirectory;
/*     */   private final Path filePath;
/*     */   private final SQLiteDataSource dataSource;
/*     */   
/*     */   private static String buildFilename(WurmDatabaseSchema schema) {
/*  29 */     return schema.getDatabase().toLowerCase(Locale.ENGLISH) + ".db";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Path sqliteDirectory(String worldDirectory) {
/*  40 */     return (new File(worldDirectory)).toPath().resolve("sqlite");
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
/*     */   private static String buildUrl(String directory, WurmDatabaseSchema schema) {
/*  52 */     return "jdbc:sqlite:" + directory + "/sqlite/" + schema.getDatabase().toLowerCase(Locale.ENGLISH) + ".db";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SqliteConnectionFactory(String worldDirectory, WurmDatabaseSchema schema, SQLiteConfig config) {
/*  58 */     super(buildUrl(worldDirectory, schema), schema);
/*  59 */     this.fileDirectory = sqliteDirectory(worldDirectory);
/*  60 */     this.filePath = this.fileDirectory.resolve(buildFilename(schema));
/*  61 */     this.config = config;
/*  62 */     this.dataSource = new SQLiteDataSource(config);
/*  63 */     this.dataSource.setUrl(getUrl());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection createConnection() throws SQLException {
/*  69 */     return this.dataSource.getConnection();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValid(@Nullable Connection con) throws SQLException {
/*  75 */     return (con != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStale(long lastUsed, @Nullable Connection connection) throws SQLException {
/*  81 */     return (System.currentTimeMillis() - lastUsed > 3600000L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getFilePath() {
/*  89 */     return this.filePath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getFileDirectory() {
/*  97 */     return this.fileDirectory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SQLiteDataSource getDataSource() {
/* 105 */     return this.dataSource;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\database\SqliteConnectionFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */