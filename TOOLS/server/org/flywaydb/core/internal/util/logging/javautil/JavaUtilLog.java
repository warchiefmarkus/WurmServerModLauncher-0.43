/*    */ package org.flywaydb.core.internal.util.logging.javautil;
/*    */ 
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.LogRecord;
/*    */ import java.util.logging.Logger;
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
/*    */ 
/*    */ 
/*    */ public class JavaUtilLog
/*    */   implements Log
/*    */ {
/*    */   private final Logger logger;
/*    */   
/*    */   public JavaUtilLog(Logger logger) {
/* 39 */     this.logger = logger;
/*    */   }
/*    */   
/*    */   public void debug(String message) {
/* 43 */     log(Level.FINE, message, null);
/*    */   }
/*    */   
/*    */   public void info(String message) {
/* 47 */     log(Level.INFO, message, null);
/*    */   }
/*    */   
/*    */   public void warn(String message) {
/* 51 */     log(Level.WARNING, message, null);
/*    */   }
/*    */   
/*    */   public void error(String message) {
/* 55 */     log(Level.SEVERE, message, null);
/*    */   }
/*    */   
/*    */   public void error(String message, Exception e) {
/* 59 */     log(Level.SEVERE, message, e);
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
/*    */   private void log(Level level, String message, Exception e) {
/* 71 */     LogRecord record = new LogRecord(level, message);
/* 72 */     record.setLoggerName(this.logger.getName());
/* 73 */     record.setThrown(e);
/* 74 */     record.setSourceClassName(this.logger.getName());
/* 75 */     record.setSourceMethodName(getMethodName());
/* 76 */     this.logger.log(record);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private String getMethodName() {
/* 83 */     StackTraceElement[] steArray = (new Throwable()).getStackTrace();
/*    */     
/* 85 */     for (StackTraceElement stackTraceElement : steArray) {
/* 86 */       if (this.logger.getName().equals(stackTraceElement.getClassName())) {
/* 87 */         return stackTraceElement.getMethodName();
/*    */       }
/*    */     } 
/*    */     
/* 91 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\logging\javautil\JavaUtilLog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */