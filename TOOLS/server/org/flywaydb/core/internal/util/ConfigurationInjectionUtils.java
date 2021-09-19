/*    */ package org.flywaydb.core.internal.util;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConfigurationInjectionUtils
/*    */ {
/*    */   public static void injectFlywayConfiguration(Object target, FlywayConfiguration configuration) {
/* 38 */     if (target instanceof ConfigurationAware)
/* 39 */       ((ConfigurationAware)target).setFlywayConfiguration(configuration); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\ConfigurationInjectionUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */