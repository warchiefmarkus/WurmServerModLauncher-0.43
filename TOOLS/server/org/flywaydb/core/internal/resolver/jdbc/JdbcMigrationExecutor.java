/*    */ package org.flywaydb.core.internal.resolver.jdbc;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import org.flywaydb.core.api.FlywayException;
/*    */ import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
/*    */ import org.flywaydb.core.api.resolver.MigrationExecutor;
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
/*    */ public class JdbcMigrationExecutor
/*    */   implements MigrationExecutor
/*    */ {
/*    */   private final JdbcMigration jdbcMigration;
/*    */   
/*    */   public JdbcMigrationExecutor(JdbcMigration jdbcMigration) {
/* 39 */     this.jdbcMigration = jdbcMigration;
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(Connection connection) {
/*    */     try {
/* 45 */       this.jdbcMigration.migrate(connection);
/* 46 */     } catch (Exception e) {
/* 47 */       throw new FlywayException("Migration failed !", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean executeInTransaction() {
/* 53 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\resolver\jdbc\JdbcMigrationExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */