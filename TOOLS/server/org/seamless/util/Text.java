/*    */ package org.seamless.util;
/*    */ 
/*    */ import java.math.BigDecimal;
/*    */ import java.text.DateFormat;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Calendar;
/*    */ import java.util.Date;
/*    */ import java.util.GregorianCalendar;
/*    */ import java.util.TimeZone;
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
/*    */ public class Text
/*    */ {
/*    */   public static final String ISO8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ssz";
/*    */   
/*    */   public static String ltrim(String s) {
/* 28 */     return s.replaceAll("(?s)^\\s+", "");
/*    */   }
/*    */   
/*    */   public static String rtrim(String s) {
/* 32 */     return s.replaceAll("(?s)\\s+$", "");
/*    */   }
/*    */ 
/*    */   
/*    */   public static String displayFilesize(long fileSizeInBytes) {
/* 37 */     if (fileSizeInBytes >= 1073741824L)
/* 38 */       return new BigDecimal(fileSizeInBytes / 1024L / 1024L / 1024L) + " GiB"; 
/* 39 */     if (fileSizeInBytes >= 1048576L)
/* 40 */       return new BigDecimal(fileSizeInBytes / 1024L / 1024L) + " MiB"; 
/* 41 */     if (fileSizeInBytes >= 1024L) {
/* 42 */       return new BigDecimal(fileSizeInBytes / 1024L) + " KiB";
/*    */     }
/* 44 */     return new BigDecimal(fileSizeInBytes) + " bytes";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Calendar fromISO8601String(TimeZone targetTimeZone, String s) {
/* 51 */     DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
/* 52 */     format.setTimeZone(targetTimeZone);
/*    */     try {
/* 54 */       Calendar cal = new GregorianCalendar();
/* 55 */       cal.setTime(format.parse(s));
/* 56 */       return cal;
/* 57 */     } catch (Exception ex) {
/* 58 */       throw new RuntimeException(ex);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static String toISO8601String(TimeZone targetTimeZone, Date datetime) {
/* 63 */     Calendar cal = new GregorianCalendar();
/* 64 */     cal.setTime(datetime);
/* 65 */     return toISO8601String(targetTimeZone, cal);
/*    */   }
/*    */   
/*    */   public static String toISO8601String(TimeZone targetTimeZone, long unixTime) {
/* 69 */     Calendar cal = new GregorianCalendar();
/* 70 */     cal.setTimeInMillis(unixTime);
/* 71 */     return toISO8601String(targetTimeZone, cal);
/*    */   }
/*    */   
/*    */   public static String toISO8601String(TimeZone targetTimeZone, Calendar cal) {
/* 75 */     DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
/* 76 */     format.setTimeZone(targetTimeZone);
/*    */     try {
/* 78 */       return format.format(cal.getTime());
/* 79 */     } catch (Exception ex) {
/* 80 */       throw new RuntimeException(ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\Text.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */