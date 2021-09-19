/*    */ package org.flywaydb.core.api.migration.spring;
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
/*    */ public abstract class BaseSpringJdbcMigration
/*    */   implements SpringJdbcMigration, ConfigurationAware
/*    */ {
/*    */   protected FlywayConfiguration flywayConfiguration;
/*    */   
/*    */   public void setFlywayConfiguration(FlywayConfiguration flywayConfiguration) {
/* 32 */     this.flywayConfiguration = flywayConfiguration;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\api\migration\spring\BaseSpringJdbcMigration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */