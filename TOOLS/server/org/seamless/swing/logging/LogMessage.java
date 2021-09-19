/*    */ package org.seamless.swing.logging;
/*    */ 
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.logging.Level;
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
/*    */ public class LogMessage
/*    */ {
/*    */   private Level level;
/* 27 */   private Long createdOn = Long.valueOf((new Date()).getTime());
/* 28 */   private String thread = Thread.currentThread().getName();
/*    */   private String source;
/*    */   private String message;
/*    */   
/*    */   public LogMessage(String message) {
/* 33 */     this(Level.INFO, message);
/*    */   }
/*    */   
/*    */   public LogMessage(String source, String message) {
/* 37 */     this(Level.INFO, source, message);
/*    */   }
/*    */   
/*    */   public LogMessage(Level level, String message) {
/* 41 */     this(level, null, message);
/*    */   }
/*    */   
/*    */   public LogMessage(Level level, String source, String message) {
/* 45 */     this.level = level;
/* 46 */     this.source = source;
/* 47 */     this.message = message;
/*    */   }
/*    */   
/*    */   public Level getLevel() {
/* 51 */     return this.level;
/*    */   }
/*    */   
/*    */   public Long getCreatedOn() {
/* 55 */     return this.createdOn;
/*    */   }
/*    */   
/*    */   public String getThread() {
/* 59 */     return this.thread;
/*    */   }
/*    */   
/*    */   public String getSource() {
/* 63 */     return this.source;
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 67 */     return this.message;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 72 */     SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
/* 73 */     return getLevel() + " - " + dateFormat.format(new Date(getCreatedOn().longValue())) + " - " + getThread() + " : " + getSource() + " : " + getMessage();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\swing\logging\LogMessage.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */