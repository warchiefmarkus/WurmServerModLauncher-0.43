/*    */ package org.flywaydb.core.api.resolver;
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
/*    */ public abstract class BaseMigrationResolver
/*    */   implements MigrationResolver, ConfigurationAware
/*    */ {
/*    */   protected FlywayConfiguration flywayConfiguration;
/*    */   
/*    */   public void setFlywayConfiguration(FlywayConfiguration flywayConfiguration) {
/* 31 */     this.flywayConfiguration = flywayConfiguration;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\api\resolver\BaseMigrationResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */