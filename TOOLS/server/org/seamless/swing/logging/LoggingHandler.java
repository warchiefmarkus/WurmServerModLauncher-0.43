/*    */ package org.seamless.swing.logging;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.logging.Handler;
/*    */ import java.util.logging.LogRecord;
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
/*    */ public abstract class LoggingHandler
/*    */   extends Handler
/*    */ {
/* 26 */   public int sourcePathElements = 3;
/*    */ 
/*    */   
/*    */   public LoggingHandler() {}
/*    */   
/*    */   public LoggingHandler(int sourcePathElements) {
/* 32 */     this.sourcePathElements = sourcePathElements;
/*    */   }
/*    */   
/*    */   public void publish(LogRecord logRecord) {
/* 36 */     LogMessage logMessage = new LogMessage(logRecord.getLevel(), getSource(logRecord), logRecord.getMessage());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 42 */     log(logMessage);
/*    */   }
/*    */ 
/*    */   
/*    */   public void flush() {}
/*    */ 
/*    */   
/*    */   public void close() throws SecurityException {}
/*    */   
/*    */   protected String getSource(LogRecord record) {
/* 52 */     StringBuilder sb = new StringBuilder(180);
/* 53 */     String[] split = record.getSourceClassName().split("\\.");
/* 54 */     if (split.length > this.sourcePathElements) {
/* 55 */       split = Arrays.<String>copyOfRange(split, split.length - this.sourcePathElements, split.length);
/*    */     }
/* 57 */     for (String s : split) {
/* 58 */       sb.append(s).append(".");
/*    */     }
/* 60 */     sb.append(record.getSourceMethodName());
/* 61 */     return sb.toString();
/*    */   }
/*    */   
/*    */   protected abstract void log(LogMessage paramLogMessage);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\swing\logging\LoggingHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */