/*     */ package org.seamless.util.time;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Date;
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
/*     */ public class DateRange
/*     */   implements Serializable
/*     */ {
/*     */   protected Date start;
/*     */   protected Date end;
/*     */   
/*     */   public enum Preset
/*     */   {
/*  27 */     ALL((String)new DateRange(null)),
/*  28 */     YEAR_TO_DATE((String)new DateRange(new Date(DateRange.getCurrentYear(), 0, 1))),
/*  29 */     MONTH_TO_DATE((String)new DateRange(new Date(DateRange.getCurrentYear(), DateRange.getCurrentMonth(), 1))),
/*  30 */     LAST_MONTH((String)DateRange.getMonthOf(new Date(DateRange.getCurrentYear(), DateRange.getCurrentMonth() - 1, 1))),
/*  31 */     LAST_YEAR((String)new DateRange(new Date(DateRange.getCurrentYear() - 1, 0, 1), new Date(DateRange.getCurrentYear() - 1, 11, 31)));
/*     */     
/*     */     DateRange dateRange;
/*     */     
/*     */     Preset(DateRange dateRange) {
/*  36 */       this.dateRange = dateRange;
/*     */     }
/*     */     
/*     */     public DateRange getDateRange() {
/*  40 */       return this.dateRange;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DateRange() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public DateRange(Date start) {
/*  52 */     this.start = start;
/*     */   }
/*     */   
/*     */   public DateRange(Date start, Date end) {
/*  56 */     this.start = start;
/*  57 */     this.end = end;
/*     */   }
/*     */   
/*     */   public DateRange(String startUnixtime, String endUnixtime) throws NumberFormatException {
/*  61 */     if (startUnixtime != null) {
/*  62 */       this.start = new Date(Long.valueOf(startUnixtime).longValue());
/*     */     }
/*  64 */     if (endUnixtime != null) {
/*  65 */       this.end = new Date(Long.valueOf(endUnixtime).longValue());
/*     */     }
/*     */   }
/*     */   
/*     */   public Date getStart() {
/*  70 */     return this.start;
/*     */   }
/*     */   
/*     */   public Date getEnd() {
/*  74 */     return this.end;
/*     */   }
/*     */   
/*     */   public boolean isStartAfter(Date date) {
/*  78 */     return (getStart() != null && getStart().getTime() > date.getTime());
/*     */   }
/*     */   
/*     */   public Date getOneDayBeforeStart() {
/*  82 */     if (getStart() == null) {
/*  83 */       throw new IllegalStateException("Can't get day before start date because start date is null");
/*     */     }
/*  85 */     return new Date(getStart().getTime() - 86400000L);
/*     */   }
/*     */   
/*     */   public static int getCurrentYear() {
/*  89 */     return (new Date()).getYear();
/*     */   }
/*     */   
/*     */   public static int getCurrentMonth() {
/*  93 */     return (new Date()).getMonth();
/*     */   }
/*     */   
/*     */   public static int getCurrentDayOfMonth() {
/*  97 */     return (new Date()).getDate();
/*     */   }
/*     */   
/*     */   public boolean hasStartOrEnd() {
/* 101 */     return (getStart() != null || getEnd() != null);
/*     */   }
/*     */   
/*     */   public static int getDaysInMonth(Date date) {
/* 105 */     int month = date.getMonth();
/* 106 */     int year = date.getYear() + 1900;
/* 107 */     boolean isLeapYear = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
/* 108 */     int[] daysInMonth = { 31, isLeapYear ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
/* 109 */     return daysInMonth[month];
/*     */   }
/*     */   
/*     */   public static DateRange getMonthOf(Date date) {
/* 113 */     return new DateRange(new Date(date.getYear(), date.getMonth(), 1), new Date(date.getYear(), date.getMonth(), getDaysInMonth(date)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInRange(Date date) {
/* 120 */     return (getStart() != null && getStart().getTime() < date.getTime() && (getEnd() == null || getEnd().getTime() > date.getTime()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValid() {
/* 125 */     return (getStart() != null && (getEnd() == null || getStart().getTime() <= getEnd().getTime()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 130 */     if (this == o) return true; 
/* 131 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/* 133 */     DateRange dateRange = (DateRange)o;
/*     */     
/* 135 */     if ((this.end != null) ? !this.end.equals(dateRange.end) : (dateRange.end != null)) return false; 
/* 136 */     if ((this.start != null) ? !this.start.equals(dateRange.start) : (dateRange.start != null)) return false;
/*     */     
/* 138 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 143 */     int result = (this.start != null) ? this.start.hashCode() : 0;
/* 144 */     result = 31 * result + ((this.end != null) ? this.end.hashCode() : 0);
/* 145 */     return result;
/*     */   }
/*     */   
/*     */   public static DateRange valueOf(String s) {
/* 149 */     if (!s.contains("dr=")) return null; 
/* 150 */     String dr = s.substring(s.indexOf("dr=") + 3);
/* 151 */     dr = dr.substring(0, dr.indexOf(";"));
/* 152 */     String[] split = dr.split(",");
/* 153 */     if (split.length != 2) return null; 
/*     */     try {
/* 155 */       return new DateRange(!split[0].equals("0") ? new Date(Long.valueOf(split[0]).longValue()) : null, !split[1].equals("0") ? new Date(Long.valueOf(split[1]).longValue()) : null);
/*     */ 
/*     */     
/*     */     }
/* 159 */     catch (Exception ex) {
/* 160 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 166 */     StringBuilder sb = new StringBuilder();
/* 167 */     sb.append("dr=");
/* 168 */     sb.append((getStart() != null) ? Long.valueOf(getStart().getTime()) : "0");
/* 169 */     sb.append(",");
/* 170 */     sb.append((getEnd() != null) ? Long.valueOf(getEnd().getTime()) : "0");
/* 171 */     sb.append(";");
/* 172 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\time\DateRange.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */