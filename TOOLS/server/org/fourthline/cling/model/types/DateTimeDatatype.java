/*    */ package org.fourthline.cling.model.types;
/*    */ 
/*    */ import java.text.ParseException;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Calendar;
/*    */ import java.util.Date;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DateTimeDatatype
/*    */   extends AbstractDatatype<Calendar>
/*    */ {
/*    */   protected String[] readFormats;
/*    */   protected String writeFormat;
/*    */   
/*    */   public DateTimeDatatype(String[] readFormats, String writeFormat) {
/* 33 */     this.readFormats = readFormats;
/* 34 */     this.writeFormat = writeFormat;
/*    */   }
/*    */   
/*    */   public Calendar valueOf(String s) throws InvalidValueException {
/* 38 */     if (s.equals("")) return null;
/*    */     
/* 40 */     Date d = getDateValue(s, this.readFormats);
/* 41 */     if (d == null) {
/* 42 */       throw new InvalidValueException("Can't parse date/time from: " + s);
/*    */     }
/*    */     
/* 45 */     Calendar c = Calendar.getInstance(getTimeZone());
/* 46 */     c.setTime(d);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 54 */     return c;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString(Calendar value) throws InvalidValueException {
/* 59 */     if (value == null) return ""; 
/* 60 */     SimpleDateFormat sdt = new SimpleDateFormat(this.writeFormat);
/* 61 */     sdt.setTimeZone(getTimeZone());
/* 62 */     return sdt.format(value.getTime());
/*    */   }
/*    */   
/*    */   protected String normalizeTimeZone(String value) {
/* 66 */     if (value.endsWith("Z")) {
/* 67 */       value = value.substring(0, value.length() - 1) + "+0000";
/* 68 */     } else if (value.length() > 7 && value
/* 69 */       .charAt(value.length() - 3) == ':' && (value
/* 70 */       .charAt(value.length() - 6) == '-' || value.charAt(value.length() - 6) == '+')) {
/*    */       
/* 72 */       value = value.substring(0, value.length() - 3) + value.substring(value.length() - 2);
/*    */     } 
/* 74 */     return value;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Date getDateValue(String value, String[] formats) {
/* 79 */     value = normalizeTimeZone(value);
/*    */     
/* 81 */     Date d = null;
/* 82 */     for (String format : formats) {
/* 83 */       SimpleDateFormat sdt = new SimpleDateFormat(format);
/* 84 */       sdt.setTimeZone(getTimeZone());
/*    */       try {
/* 86 */         d = sdt.parse(value);
/*    */       }
/* 88 */       catch (ParseException parseException) {}
/*    */     } 
/*    */ 
/*    */     
/* 92 */     return d;
/*    */   }
/*    */   
/*    */   protected TimeZone getTimeZone() {
/* 96 */     return TimeZone.getDefault();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\DateTimeDatatype.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */