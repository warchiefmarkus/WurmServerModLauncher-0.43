/*    */ package org.seamless.util.logging;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.io.StringWriter;
/*    */ import java.text.DateFormat;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.logging.Formatter;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.LogRecord;
/*    */ import java.util.logging.StreamHandler;
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
/*    */ public class SystemOutLoggingHandler
/*    */   extends StreamHandler
/*    */ {
/*    */   public SystemOutLoggingHandler() {
/* 38 */     super(System.out, new SimpleFormatter());
/* 39 */     setLevel(Level.ALL);
/*    */   }
/*    */   
/*    */   public void close() {
/* 43 */     flush();
/*    */   }
/*    */   
/*    */   public void publish(LogRecord record) {
/* 47 */     super.publish(record);
/* 48 */     flush();
/*    */   }
/*    */   
/*    */   public static class SimpleFormatter extends Formatter {
/*    */     public String format(LogRecord record) {
/* 53 */       StringBuffer buf = new StringBuffer(180);
/* 54 */       DateFormat dateFormat = new SimpleDateFormat("kk:mm:ss,SS");
/*    */       
/* 56 */       buf.append("[").append(pad(Thread.currentThread().getName(), 32)).append("] ");
/* 57 */       buf.append(pad(record.getLevel().toString(), 12));
/* 58 */       buf.append(" - ");
/* 59 */       buf.append(pad(dateFormat.format(new Date(record.getMillis())), 24));
/* 60 */       buf.append(" - ");
/* 61 */       buf.append(toClassString(record.getSourceClassName(), 30));
/* 62 */       buf.append('#');
/* 63 */       buf.append(record.getSourceMethodName());
/* 64 */       buf.append(": ");
/* 65 */       buf.append(formatMessage(record));
/*    */       
/* 67 */       buf.append("\n");
/*    */       
/* 69 */       Throwable throwable = record.getThrown();
/* 70 */       if (throwable != null) {
/* 71 */         StringWriter sink = new StringWriter();
/* 72 */         throwable.printStackTrace(new PrintWriter(sink, true));
/* 73 */         buf.append(sink.toString());
/*    */       } 
/*    */       
/* 76 */       return buf.toString();
/*    */     }
/*    */     
/*    */     public String pad(String s, int size) {
/* 80 */       if (s.length() < size) {
/* 81 */         for (int i = s.length(); i < size - s.length(); i++) {
/* 82 */           s = s + " ";
/*    */         }
/*    */       }
/* 85 */       return s;
/*    */     }
/*    */     
/*    */     public String toClassString(String name, int maxLength) {
/* 89 */       return (name.length() > maxLength) ? name.substring(name.length() - maxLength) : name;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\logging\SystemOutLoggingHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */