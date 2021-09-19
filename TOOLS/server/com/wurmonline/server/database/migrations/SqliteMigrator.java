/*    */ package com.wurmonline.server.database.migrations;
/*    */ 
/*    */ import com.wurmonline.server.database.SqliteConnectionFactory;
/*    */ import com.wurmonline.server.database.WurmDatabaseSchema;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Collections;
/*    */ import org.flywaydb.core.Flyway;
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
/*    */ public class SqliteMigrator
/*    */   extends Migrator<SqliteConnectionFactory>
/*    */ {
/*    */   private final WurmDatabaseSchema schema;
/*    */   
/*    */   public SqliteMigrator(SqliteConnectionFactory connectionFactory, Path migrationsDir) {
/* 28 */     super(connectionFactory, 
/* 29 */         Collections.singletonList(migrationsDir), flyway -> flyway.setDataSource(new SqliteFlywayIssue1499Workaround(connectionFactory.getDataSource())));
/*    */ 
/*    */     
/* 32 */     this.schema = connectionFactory.getSchema();
/*    */   }
/*    */ 
/*    */   
/*    */   public WurmDatabaseSchema getSchema() {
/* 37 */     return this.schema;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\database\migrations\SqliteMigrator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */