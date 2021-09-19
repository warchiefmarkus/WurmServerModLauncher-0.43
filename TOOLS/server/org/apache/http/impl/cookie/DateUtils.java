/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.text.ParsePosition;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import org.apache.http.annotation.Immutable;
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
/*     */ 
/*     */ @Immutable
/*     */ public final class DateUtils
/*     */ {
/*     */   public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
/*     */   public static final String PATTERN_RFC1036 = "EEE, dd-MMM-yy HH:mm:ss zzz";
/*     */   public static final String PATTERN_ASCTIME = "EEE MMM d HH:mm:ss yyyy";
/*  70 */   private static final String[] DEFAULT_PATTERNS = new String[] { "EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy" };
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Date DEFAULT_TWO_DIGIT_YEAR_START;
/*     */ 
/*     */ 
/*     */   
/*  78 */   public static final TimeZone GMT = TimeZone.getTimeZone("GMT");
/*     */   
/*     */   static {
/*  81 */     Calendar calendar = Calendar.getInstance();
/*  82 */     calendar.setTimeZone(GMT);
/*  83 */     calendar.set(2000, 0, 1, 0, 0, 0);
/*  84 */     calendar.set(14, 0);
/*  85 */     DEFAULT_TWO_DIGIT_YEAR_START = calendar.getTime();
/*     */   }
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
/*     */   public static Date parseDate(String dateValue) throws DateParseException {
/* 100 */     return parseDate(dateValue, null, null);
/*     */   }
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
/*     */   public static Date parseDate(String dateValue, String[] dateFormats) throws DateParseException {
/* 115 */     return parseDate(dateValue, dateFormats, null);
/*     */   }
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
/*     */   public static Date parseDate(String dateValue, String[] dateFormats, Date startDate) throws DateParseException {
/* 138 */     if (dateValue == null) {
/* 139 */       throw new IllegalArgumentException("dateValue is null");
/*     */     }
/* 141 */     if (dateFormats == null) {
/* 142 */       dateFormats = DEFAULT_PATTERNS;
/*     */     }
/* 144 */     if (startDate == null) {
/* 145 */       startDate = DEFAULT_TWO_DIGIT_YEAR_START;
/*     */     }
/*     */ 
/*     */     
/* 149 */     if (dateValue.length() > 1 && dateValue.startsWith("'") && dateValue.endsWith("'"))
/*     */     {
/*     */ 
/*     */       
/* 153 */       dateValue = dateValue.substring(1, dateValue.length() - 1);
/*     */     }
/*     */     
/* 156 */     for (String dateFormat : dateFormats) {
/* 157 */       SimpleDateFormat dateParser = DateFormatHolder.formatFor(dateFormat);
/* 158 */       dateParser.set2DigitYearStart(startDate);
/* 159 */       ParsePosition pos = new ParsePosition(0);
/* 160 */       Date result = dateParser.parse(dateValue, pos);
/* 161 */       if (pos.getIndex() != 0) {
/* 162 */         return result;
/*     */       }
/*     */     } 
/*     */     
/* 166 */     throw new DateParseException("Unable to parse the date " + dateValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String formatDate(Date date) {
/* 178 */     return formatDate(date, "EEE, dd MMM yyyy HH:mm:ss zzz");
/*     */   }
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
/*     */   public static String formatDate(Date date, String pattern) {
/* 195 */     if (date == null) throw new IllegalArgumentException("date is null"); 
/* 196 */     if (pattern == null) throw new IllegalArgumentException("pattern is null");
/*     */     
/* 198 */     SimpleDateFormat formatter = DateFormatHolder.formatFor(pattern);
/* 199 */     return formatter.format(date);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clearThreadLocal() {
/* 206 */     DateFormatHolder.clearThreadLocal();
/*     */   }
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
/*     */   static final class DateFormatHolder
/*     */   {
/* 222 */     private static final ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>> THREADLOCAL_FORMATS = new ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>>()
/*     */       {
/*     */         protected SoftReference<Map<String, SimpleDateFormat>> initialValue()
/*     */         {
/* 226 */           return new SoftReference<Map<String, SimpleDateFormat>>(new HashMap<String, SimpleDateFormat>());
/*     */         }
/*     */       };
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
/*     */     public static SimpleDateFormat formatFor(String pattern) {
/* 245 */       SoftReference<Map<String, SimpleDateFormat>> ref = THREADLOCAL_FORMATS.get();
/* 246 */       Map<String, SimpleDateFormat> formats = ref.get();
/* 247 */       if (formats == null) {
/* 248 */         formats = new HashMap<String, SimpleDateFormat>();
/* 249 */         THREADLOCAL_FORMATS.set(new SoftReference<Map<String, SimpleDateFormat>>(formats));
/*     */       } 
/*     */ 
/*     */       
/* 253 */       SimpleDateFormat format = formats.get(pattern);
/* 254 */       if (format == null) {
/* 255 */         format = new SimpleDateFormat(pattern, Locale.US);
/* 256 */         format.setTimeZone(TimeZone.getTimeZone("GMT"));
/* 257 */         formats.put(pattern, format);
/*     */       } 
/*     */       
/* 260 */       return format;
/*     */     }
/*     */     
/*     */     public static void clearThreadLocal() {
/* 264 */       THREADLOCAL_FORMATS.remove();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\cookie\DateUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */