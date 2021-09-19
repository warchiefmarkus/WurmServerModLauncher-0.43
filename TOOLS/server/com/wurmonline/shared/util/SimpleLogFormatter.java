/*    */ package com.wurmonline.shared.util;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.io.StringWriter;
/*    */ import java.security.AccessController;
/*    */ import java.text.FieldPosition;
/*    */ import java.text.MessageFormat;
/*    */ import java.util.Date;
/*    */ import java.util.logging.Formatter;
/*    */ import java.util.logging.LogRecord;
/*    */ import sun.security.action.GetPropertyAction;
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
/*    */ public class SimpleLogFormatter
/*    */   extends Formatter
/*    */ {
/* 28 */   Date dat = new Date();
/*    */   private static final String FORMAT = "{0,date} {0,time}";
/*    */   private MessageFormat formatter;
/* 31 */   private Object[] args = new Object[1];
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   private final String lineSeparator = AccessController.<String>doPrivileged(new GetPropertyAction("line.separator"));
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
/*    */   public synchronized String format(LogRecord record) {
/* 49 */     StringBuilder sb = new StringBuilder();
/*    */     
/* 51 */     this.dat.setTime(record.getMillis());
/* 52 */     this.args[0] = this.dat;
/* 53 */     StringBuffer text = new StringBuffer();
/* 54 */     if (this.formatter == null)
/*    */     {
/* 56 */       this.formatter = new MessageFormat("{0,date} {0,time}");
/*    */     }
/* 58 */     this.formatter.format(this.args, text, (FieldPosition)null);
/* 59 */     sb.append(text);
/* 60 */     sb.append('.');
/* 61 */     sb.append(record.getMillis() % 1000L);
/* 62 */     sb.append(' ');
/* 63 */     sb.append(record.getThreadID());
/* 64 */     sb.append(' ');
/* 65 */     if (record.getSourceClassName() != null) {
/*    */       
/* 67 */       sb.append(record.getSourceClassName());
/*    */     }
/*    */     else {
/*    */       
/* 71 */       sb.append(record.getLoggerName());
/*    */     } 
/* 73 */     if (record.getSourceMethodName() != null) {
/*    */       
/* 75 */       sb.append(' ');
/* 76 */       sb.append(record.getSourceMethodName());
/*    */     } 
/* 78 */     sb.append(' ');
/*    */     
/* 80 */     String message = formatMessage(record);
/* 81 */     sb.append(record.getLevel().getLocalizedName());
/* 82 */     sb.append(": ");
/* 83 */     sb.append(message);
/* 84 */     sb.append(this.lineSeparator);
/* 85 */     if (record.getThrown() != null) {
/*    */       
/*    */       try {
/*    */         
/* 89 */         StringWriter sw = new StringWriter();
/* 90 */         PrintWriter pw = new PrintWriter(sw);
/* 91 */         record.getThrown().printStackTrace(pw);
/* 92 */         pw.close();
/* 93 */         sb.append(sw.toString());
/*    */       }
/* 95 */       catch (Exception exception) {}
/*    */     }
/*    */ 
/*    */     
/* 99 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\share\\util\SimpleLogFormatter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */