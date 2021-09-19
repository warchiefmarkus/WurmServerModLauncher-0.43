/*     */ package org.fourthline.cling.android;
/*     */ 
/*     */ import android.util.Log;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.util.logging.Formatter;
/*     */ import java.util.logging.Handler;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.LogRecord;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FixedAndroidLogHandler
/*     */   extends Handler
/*     */ {
/*  34 */   private static final Formatter THE_FORMATTER = new Formatter()
/*     */     {
/*     */       public String format(LogRecord r) {
/*  37 */         Throwable thrown = r.getThrown();
/*  38 */         if (thrown != null) {
/*  39 */           StringWriter sw = new StringWriter();
/*  40 */           PrintWriter pw = new PrintWriter(sw);
/*  41 */           sw.write(r.getMessage());
/*  42 */           sw.write("\n");
/*  43 */           thrown.printStackTrace(pw);
/*  44 */           pw.flush();
/*  45 */           return sw.toString();
/*     */         } 
/*  47 */         return r.getMessage();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FixedAndroidLogHandler() {
/*  56 */     setFormatter(THE_FORMATTER);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void publish(LogRecord record) {
/*     */     try {
/*  72 */       int level = getAndroidLevel(record.getLevel());
/*  73 */       String tag = record.getLoggerName();
/*     */       
/*  75 */       if (tag == null) {
/*     */         
/*  77 */         tag = "null";
/*     */       } else {
/*     */         
/*  80 */         int length = tag.length();
/*  81 */         if (length > 23) {
/*     */ 
/*     */           
/*  84 */           int lastPeriod = tag.lastIndexOf(".");
/*  85 */           if (length - lastPeriod - 1 <= 23) {
/*  86 */             tag = tag.substring(lastPeriod + 1);
/*     */           } else {
/*     */             
/*  89 */             tag = tag.substring(tag.length() - 23);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 118 */       String message = getFormatter().format(record);
/* 119 */       Log.println(level, tag, message);
/* 120 */     } catch (RuntimeException e) {
/* 121 */       Log.e("AndroidHandler", "Error logging message.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int getAndroidLevel(Level level) {
/* 133 */     int value = level.intValue();
/* 134 */     if (value >= 1000)
/* 135 */       return 6; 
/* 136 */     if (value >= 900)
/* 137 */       return 5; 
/* 138 */     if (value >= 800) {
/* 139 */       return 4;
/*     */     }
/* 141 */     return 3;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\android\FixedAndroidLogHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */