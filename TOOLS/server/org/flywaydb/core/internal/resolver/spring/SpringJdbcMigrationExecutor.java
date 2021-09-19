/*    */ package org.flywaydb.core.internal.resolver.spring;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import javax.sql.DataSource;
/*    */ import org.flywaydb.core.api.FlywayException;
/*    */ import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
/*    */ import org.flywaydb.core.api.resolver.MigrationExecutor;
/*    */ import org.springframework.jdbc.core.JdbcTemplate;
/*    */ import org.springframework.jdbc.datasource.SingleConnectionDataSource;
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
/*    */ public class SpringJdbcMigrationExecutor
/*    */   implements MigrationExecutor
/*    */ {
/*    */   private final SpringJdbcMigration springJdbcMigration;
/*    */   
/*    */   public SpringJdbcMigrationExecutor(SpringJdbcMigration springJdbcMigration) {
/* 40 */     this.springJdbcMigration = springJdbcMigration;
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(Connection connection) {
/*    */     try {
/* 46 */       this.springJdbcMigration.migrate(new JdbcTemplate((DataSource)new SingleConnectionDataSource(connection, true)));
/*    */     }
/* 48 */     catch (Exception e) {
/* 49 */       throw new FlywayException("Migration failed !", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean executeInTransaction() {
/* 55 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\internal\resolver\spring\SpringJdbcMigrationExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */