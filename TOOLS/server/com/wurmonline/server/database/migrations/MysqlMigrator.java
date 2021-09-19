/*    */ package com.wurmonline.server.database.migrations;
/*    */ 
/*    */ import com.wurmonline.server.database.MysqlConnectionFactory;
/*    */ import com.wurmonline.server.database.WurmDatabaseSchema;
/*    */ import java.io.File;
/*    */ import java.nio.file.Path;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import javax.annotation.Nonnull;
/*    */ import org.flywaydb.core.Flyway;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MysqlMigrator
/*    */   extends Migrator<MysqlConnectionFactory>
/*    */ {
/* 17 */   private static final Path DEFAULT_MIGRATIONS_DIR = (new File("migrations")).toPath();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MysqlMigrator(WurmDatabaseSchema migrationSchema, MysqlConnectionFactory connectionFactory) {
/* 27 */     super(connectionFactory, 
/* 28 */         Collections.singletonList(DEFAULT_MIGRATIONS_DIR), flyway -> {
/*    */           flyway.setDataSource(connectionFactory.getUrl(), connectionFactory.getUser(), connectionFactory.getPassword(), new String[0]);
/*    */           WurmDatabaseSchema[] schemas = WurmDatabaseSchema.values();
/*    */           ArrayList<String> migrationSchemas = new ArrayList<>(schemas.length);
/*    */           for (WurmDatabaseSchema schema : schemas) {
/*    */             if (!schema.equals(migrationSchema)) {
/*    */               migrationSchemas.add(schema.getDatabase());
/*    */             }
/*    */           } 
/*    */           migrationSchemas.add(0, migrationSchema.getDatabase());
/*    */           flyway.setSchemas(migrationSchemas.<String>toArray(new String[migrationSchemas.size()]));
/*    */         });
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
/*    */   @Nonnull
/*    */   public MigrationResult migrate() {
/* 52 */     return super.migrate();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\database\migrations\MysqlMigrator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */