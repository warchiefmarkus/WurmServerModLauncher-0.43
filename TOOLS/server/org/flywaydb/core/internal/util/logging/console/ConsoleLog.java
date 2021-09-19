/*    */ package org.flywaydb.core.internal.util.logging.console;
/*    */ 
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
/*    */ public class ConsoleLog
/*    */   implements Log
/*    */ {
/*    */   private final Level level;
/*    */   
/*    */   public enum Level
/*    */   {
/* 25 */     DEBUG, INFO, WARN;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ConsoleLog(Level level) {
/* 36 */     this.level = level;
/*    */   }
/*    */   
/*    */   public void debug(String message) {
/* 40 */     if (this.level == Level.DEBUG) {
/* 41 */       System.out.println("DEBUG: " + message);
/*    */     }
/*    */   }
/*    */   
/*    */   public void info(String message) {
/* 46 */     if (this.level.compareTo(Level.INFO) <= 0) {
/* 47 */       System.out.println(message);
/*    */     }
/*    */   }
/*    */   
/*    */   public void warn(String message) {
/* 52 */     System.out.println("WARNING: " + message);
/*    */   }
/*    */   
/*    */   public void error(String message) {
/* 56 */     System.err.println("ERROR: " + message);
/*    */   }
/*    */   
/*    */   public void error(String message, Exception e) {
/* 60 */     System.err.println("ERROR: " + message);
/* 61 */     e.printStackTrace(System.err);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\logging\console\ConsoleLog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */