/*     */ package org.fourthline.cling.support.model.dlna.types;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.fourthline.cling.model.types.InvalidValueException;
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
/*     */ public class NormalPlayTime
/*     */ {
/*     */   public enum Format
/*     */   {
/*  30 */     SECONDS,
/*  31 */     TIME;
/*     */   }
/*  33 */   static final Pattern pattern = Pattern.compile("^(\\d+):(\\d{1,2}):(\\d{1,2})(\\.(\\d{1,3}))?|(\\d+)(\\.(\\d{1,3}))?$", 2);
/*     */   private long milliseconds;
/*     */   
/*     */   public NormalPlayTime(long milliseconds) {
/*  37 */     if (milliseconds < 0L) {
/*  38 */       throw new InvalidValueException("Invalid parameter milliseconds: " + milliseconds);
/*     */     }
/*     */     
/*  41 */     this.milliseconds = milliseconds;
/*     */   }
/*     */   
/*     */   public NormalPlayTime(long hours, long minutes, long seconds, long milliseconds) throws InvalidValueException {
/*  45 */     if (hours < 0L) {
/*  46 */       throw new InvalidValueException("Invalid parameter hours: " + hours);
/*     */     }
/*     */     
/*  49 */     if (minutes < 0L || minutes > 59L) {
/*  50 */       throw new InvalidValueException("Invalid parameter minutes: " + hours);
/*     */     }
/*     */     
/*  53 */     if (seconds < 0L || seconds > 59L) {
/*  54 */       throw new InvalidValueException("Invalid parameter seconds: " + hours);
/*     */     }
/*  56 */     if (milliseconds < 0L || milliseconds > 999L) {
/*  57 */       throw new InvalidValueException("Invalid parameter milliseconds: " + milliseconds);
/*     */     }
/*     */     
/*  60 */     this.milliseconds = (hours * 60L * 60L + minutes * 60L + seconds) * 1000L + milliseconds;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMilliseconds() {
/*  67 */     return this.milliseconds;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMilliseconds(long milliseconds) {
/*  74 */     if (milliseconds < 0L) {
/*  75 */       throw new InvalidValueException("Invalid parameter milliseconds: " + milliseconds);
/*     */     }
/*     */     
/*  78 */     this.milliseconds = milliseconds;
/*     */   }
/*     */   
/*     */   public String getString() {
/*  82 */     return getString(Format.SECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getString(Format format) {
/*  90 */     long hours, minutes, seconds = TimeUnit.MILLISECONDS.toSeconds(this.milliseconds);
/*  91 */     long ms = this.milliseconds % 1000L;
/*  92 */     switch (format) {
/*     */       case TIME:
/*  94 */         seconds = TimeUnit.MILLISECONDS.toSeconds(this.milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this.milliseconds));
/*  95 */         hours = TimeUnit.MILLISECONDS.toHours(this.milliseconds);
/*  96 */         minutes = TimeUnit.MILLISECONDS.toMinutes(this.milliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(this.milliseconds));
/*  97 */         return String.format(Locale.ROOT, "%d:%02d:%02d.%03d", new Object[] { Long.valueOf(hours), Long.valueOf(minutes), Long.valueOf(seconds), Long.valueOf(ms) });
/*     */     } 
/*  99 */     return String.format(Locale.ROOT, "%d.%03d", new Object[] { Long.valueOf(seconds), Long.valueOf(ms) });
/*     */   }
/*     */ 
/*     */   
/*     */   public static NormalPlayTime valueOf(String s) throws InvalidValueException {
/* 104 */     Matcher matcher = pattern.matcher(s);
/* 105 */     if (matcher.matches()) {
/* 106 */       int msMultiplier = 0;
/*     */       try {
/* 108 */         if (matcher.group(1) != null) {
/* 109 */           msMultiplier = (int)Math.pow(10.0D, (3 - matcher.group(5).length()));
/* 110 */           return new NormalPlayTime(
/* 111 */               Long.parseLong(matcher.group(1)), 
/* 112 */               Long.parseLong(matcher.group(2)), 
/* 113 */               Long.parseLong(matcher.group(3)), 
/* 114 */               Long.parseLong(matcher.group(5)) * msMultiplier);
/*     */         } 
/* 116 */         msMultiplier = (int)Math.pow(10.0D, (3 - matcher.group(8).length()));
/* 117 */         return new NormalPlayTime(
/* 118 */             Long.parseLong(matcher.group(6)) * 1000L + Long.parseLong(matcher.group(8)) * msMultiplier);
/*     */       }
/* 120 */       catch (NumberFormatException numberFormatException) {}
/*     */     } 
/*     */     
/* 123 */     throw new InvalidValueException("Can't parse NormalPlayTime: " + s);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\types\NormalPlayTime.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */