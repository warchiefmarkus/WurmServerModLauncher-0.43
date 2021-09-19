/*     */ package org.fourthline.cling.support.model.dlna.types;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ public class NormalPlayTimeRange
/*     */ {
/*     */   public static final String PREFIX = "npt=";
/*     */   private NormalPlayTime timeStart;
/*     */   private NormalPlayTime timeEnd;
/*     */   private NormalPlayTime timeDuration;
/*     */   
/*     */   public NormalPlayTimeRange(long timeStart, long timeEnd) {
/*  32 */     this.timeStart = new NormalPlayTime(timeStart);
/*  33 */     this.timeEnd = new NormalPlayTime(timeEnd);
/*     */   }
/*     */   
/*     */   public NormalPlayTimeRange(NormalPlayTime timeStart, NormalPlayTime timeEnd) {
/*  37 */     this.timeStart = timeStart;
/*  38 */     this.timeEnd = timeEnd;
/*     */   }
/*     */   
/*     */   public NormalPlayTimeRange(NormalPlayTime timeStart, NormalPlayTime timeEnd, NormalPlayTime timeDuration) {
/*  42 */     this.timeStart = timeStart;
/*  43 */     this.timeEnd = timeEnd;
/*  44 */     this.timeDuration = timeDuration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NormalPlayTime getTimeStart() {
/*  51 */     return this.timeStart;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NormalPlayTime getTimeEnd() {
/*  58 */     return this.timeEnd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NormalPlayTime getTimeDuration() {
/*  65 */     return this.timeDuration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getString() {
/*  73 */     return getString(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getString(boolean includeDuration) {
/*  81 */     String s = "npt=";
/*     */     
/*  83 */     s = s + this.timeStart.getString() + "-";
/*  84 */     if (this.timeEnd != null) {
/*  85 */       s = s + this.timeEnd.getString();
/*     */     }
/*  87 */     if (includeDuration) {
/*  88 */       s = s + "/" + ((this.timeDuration != null) ? this.timeDuration.getString() : "*");
/*     */     }
/*     */     
/*  91 */     return s;
/*     */   }
/*     */   
/*     */   public static NormalPlayTimeRange valueOf(String s) throws InvalidValueException {
/*  95 */     return valueOf(s, false);
/*     */   }
/*     */   
/*     */   public static NormalPlayTimeRange valueOf(String s, boolean mandatoryTimeEnd) throws InvalidValueException {
/*  99 */     if (s.startsWith("npt=")) {
/* 100 */       NormalPlayTime timeEnd = null, timeDuration = null;
/* 101 */       String[] params = s.substring("npt=".length()).split("[-/]");
/* 102 */       switch (params.length) {
/*     */         case 3:
/* 104 */           if (params[2].length() != 0 && !params[2].equals("*")) {
/* 105 */             timeDuration = NormalPlayTime.valueOf(params[2]);
/*     */           }
/*     */         case 2:
/* 108 */           if (params[1].length() != 0) {
/* 109 */             timeEnd = NormalPlayTime.valueOf(params[1]);
/*     */           }
/*     */         case 1:
/* 112 */           if (params[0].length() != 0 && (!mandatoryTimeEnd || (mandatoryTimeEnd && params.length > 1))) {
/* 113 */             NormalPlayTime timeStart = NormalPlayTime.valueOf(params[0]);
/* 114 */             return new NormalPlayTimeRange(timeStart, timeEnd, timeDuration);
/*     */           } 
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 120 */     throw new InvalidValueException("Can't parse NormalPlayTimeRange: " + s);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\types\NormalPlayTimeRange.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */