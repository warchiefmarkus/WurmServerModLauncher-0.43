/*    */ package org.flywaydb.core.internal.util.logging.console;
/*    */ 
/*    */ import org.flywaydb.core.internal.util.logging.Log;
/*    */ import org.flywaydb.core.internal.util.logging.LogCreator;
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
/*    */ public class ConsoleLogCreator
/*    */   implements LogCreator
/*    */ {
/*    */   private final ConsoleLog.Level level;
/*    */   
/*    */   public ConsoleLogCreator(ConsoleLog.Level level) {
/* 34 */     this.level = level;
/*    */   }
/*    */   
/*    */   public Log createLogger(Class<?> clazz) {
/* 38 */     return new ConsoleLog(this.level);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\flywaydb\core\interna\\util\logging\console\ConsoleLogCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */