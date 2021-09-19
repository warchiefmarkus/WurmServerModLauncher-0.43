/*    */ package com.wurmonline.server.database.migrations;
/*    */ 
/*    */ import com.wurmonline.server.database.MysqlConnectionFactory;
/*    */ import com.wurmonline.server.database.WurmDatabaseSchema;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MysqlMigrationStrategy
/*    */   implements MigrationStrategy
/*    */ {
/* 14 */   public static final WurmDatabaseSchema MIGRATION_SCHEMA = WurmDatabaseSchema.LOGIN;
/*    */   
/*    */   private final MysqlMigrator migrator;
/*    */ 
/*    */   
/*    */   public MysqlMigrationStrategy(MysqlConnectionFactory connectionFactory) {
/* 20 */     this.migrator = new MysqlMigrator(MIGRATION_SCHEMA, connectionFactory);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MigrationResult migrate() {
/* 28 */     return this.migrator.migrate();
/*    */   }
/*    */   
/*    */   public boolean hasPendingMigrations() {
/* 32 */     return this.migrator.hasPendingMigrations();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\database\migrations\MysqlMigrationStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */