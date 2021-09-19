/*    */ package org.flywaydb.core.internal.util.logging.apachecommons;
/*    */ 
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.flywaydb.core.internal.util.logging.Log;
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
/*    */ public class ApacheCommonsLog
/*    */   implements Log
/*    */ {
/*    */   private final Log logger;
/*    */   
/*    */   public ApacheCommonsLog(Log logger) {
/* 35 */     this.logger = logger;
/*    */   }
/*    */   
/*    */   public void debug(String message) {
/* 39 */     this.logger.debug(message);
/*    */   }
/*    */   
/*    */   public void info(String message) {
/* 43 */     this.logger.info(message);
/*    */   }
/*    */   
/*    */   public void warn(String message) {
/* 47 */     this.logger.warn(message);
/*    */   }
/*    */   
/*    */   public void error(String message) {
/* 51 */     this.logger.error(message);
/*    */   }
/*    */   
/*    */   public void error(String message, Exception e) {
/* 55 */     this.logger.error(message, e);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\logging\apachecommons\ApacheCommonsLog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */