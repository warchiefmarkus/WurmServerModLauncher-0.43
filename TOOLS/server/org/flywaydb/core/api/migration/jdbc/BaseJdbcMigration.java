/*    */ package org.flywaydb.core.api.migration.jdbc;
/*    */ 
/*    */ import org.flywaydb.core.api.configuration.ConfigurationAware;
/*    */ import org.flywaydb.core.api.configuration.FlywayConfiguration;
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
/*    */ public abstract class BaseJdbcMigration
/*    */   implements JdbcMigration, ConfigurationAware
/*    */ {
/*    */   protected FlywayConfiguration flywayConfiguration;
/*    */   
/*    */   public void setFlywayConfiguration(FlywayConfiguration flywayConfiguration) {
/* 32 */     this.flywayConfiguration = flywayConfiguration;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\api\migration\jdbc\BaseJdbcMigration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */