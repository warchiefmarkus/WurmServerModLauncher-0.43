/*    */ package org.flywaydb.core.internal.util.logging.slf4j;
/*    */ 
/*    */ import org.flywaydb.core.internal.util.logging.Log;
/*    */ import org.slf4j.Logger;
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
/*    */ public class Slf4jLog
/*    */   implements Log
/*    */ {
/*    */   private final Logger logger;
/*    */   
/*    */   public Slf4jLog(Logger logger) {
/* 36 */     this.logger = logger;
/*    */   }
/*    */   
/*    */   public void debug(String message) {
/* 40 */     this.logger.debug(message);
/*    */   }
/*    */   
/*    */   public void info(String message) {
/* 44 */     this.logger.info(message);
/*    */   }
/*    */   
/*    */   public void warn(String message) {
/* 48 */     this.logger.warn(message);
/*    */   }
/*    */   
/*    */   public void error(String message) {
/* 52 */     this.logger.error(message);
/*    */   }
/*    */   
/*    */   public void error(String message, Exception e) {
/* 56 */     this.logger.error(message, e);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\logging\slf4j\Slf4jLog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */