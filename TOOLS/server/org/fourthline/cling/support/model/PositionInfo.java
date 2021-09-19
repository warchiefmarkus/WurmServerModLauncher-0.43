/*     */ package org.fourthline.cling.support.model;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.fourthline.cling.model.ModelUtil;
/*     */ import org.fourthline.cling.model.action.ActionArgumentValue;
/*     */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
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
/*     */ public class PositionInfo
/*     */ {
/*  29 */   private UnsignedIntegerFourBytes track = new UnsignedIntegerFourBytes(0L);
/*  30 */   private String trackDuration = "00:00:00";
/*  31 */   private String trackMetaData = "NOT_IMPLEMENTED";
/*  32 */   private String trackURI = "";
/*  33 */   private String relTime = "00:00:00";
/*  34 */   private String absTime = "00:00:00";
/*  35 */   private int relCount = Integer.MAX_VALUE;
/*  36 */   private int absCount = Integer.MAX_VALUE;
/*     */ 
/*     */   
/*     */   public PositionInfo() {}
/*     */   
/*     */   public PositionInfo(Map<String, ActionArgumentValue> args) {
/*  42 */     this(((UnsignedIntegerFourBytes)((ActionArgumentValue)args
/*  43 */         .get("Track")).getValue()).getValue().longValue(), (String)((ActionArgumentValue)args
/*  44 */         .get("TrackDuration")).getValue(), (String)((ActionArgumentValue)args
/*  45 */         .get("TrackMetaData")).getValue(), (String)((ActionArgumentValue)args
/*  46 */         .get("TrackURI")).getValue(), (String)((ActionArgumentValue)args
/*  47 */         .get("RelTime")).getValue(), (String)((ActionArgumentValue)args
/*  48 */         .get("AbsTime")).getValue(), ((Integer)((ActionArgumentValue)args
/*  49 */         .get("RelCount")).getValue()).intValue(), ((Integer)((ActionArgumentValue)args
/*  50 */         .get("AbsCount")).getValue()).intValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public PositionInfo(PositionInfo copy, String relTime, String absTime) {
/*  55 */     this.track = copy.track;
/*  56 */     this.trackDuration = copy.trackDuration;
/*  57 */     this.trackMetaData = copy.trackMetaData;
/*  58 */     this.trackURI = copy.trackURI;
/*  59 */     this.relTime = relTime;
/*  60 */     this.absTime = absTime;
/*  61 */     this.relCount = copy.relCount;
/*  62 */     this.absCount = copy.absCount;
/*     */   }
/*     */   
/*     */   public PositionInfo(PositionInfo copy, long relTimeSeconds, long absTimeSeconds) {
/*  66 */     this.track = copy.track;
/*  67 */     this.trackDuration = copy.trackDuration;
/*  68 */     this.trackMetaData = copy.trackMetaData;
/*  69 */     this.trackURI = copy.trackURI;
/*  70 */     this.relTime = ModelUtil.toTimeString(relTimeSeconds);
/*  71 */     this.absTime = ModelUtil.toTimeString(absTimeSeconds);
/*  72 */     this.relCount = copy.relCount;
/*  73 */     this.absCount = copy.absCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public PositionInfo(long track, String trackDuration, String trackURI, String relTime, String absTime) {
/*  78 */     this.track = new UnsignedIntegerFourBytes(track);
/*  79 */     this.trackDuration = trackDuration;
/*  80 */     this.trackURI = trackURI;
/*  81 */     this.relTime = relTime;
/*  82 */     this.absTime = absTime;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PositionInfo(long track, String trackDuration, String trackMetaData, String trackURI, String relTime, String absTime, int relCount, int absCount) {
/*  88 */     this.track = new UnsignedIntegerFourBytes(track);
/*  89 */     this.trackDuration = trackDuration;
/*  90 */     this.trackMetaData = trackMetaData;
/*  91 */     this.trackURI = trackURI;
/*  92 */     this.relTime = relTime;
/*  93 */     this.absTime = absTime;
/*  94 */     this.relCount = relCount;
/*  95 */     this.absCount = absCount;
/*     */   }
/*     */   
/*     */   public PositionInfo(long track, String trackMetaData, String trackURI) {
/*  99 */     this.track = new UnsignedIntegerFourBytes(track);
/* 100 */     this.trackMetaData = trackMetaData;
/* 101 */     this.trackURI = trackURI;
/*     */   }
/*     */   
/*     */   public UnsignedIntegerFourBytes getTrack() {
/* 105 */     return this.track;
/*     */   }
/*     */   
/*     */   public String getTrackDuration() {
/* 109 */     return this.trackDuration;
/*     */   }
/*     */   
/*     */   public String getTrackMetaData() {
/* 113 */     return this.trackMetaData;
/*     */   }
/*     */   
/*     */   public String getTrackURI() {
/* 117 */     return this.trackURI;
/*     */   }
/*     */   
/*     */   public String getRelTime() {
/* 121 */     return this.relTime;
/*     */   }
/*     */   
/*     */   public String getAbsTime() {
/* 125 */     return this.absTime;
/*     */   }
/*     */   
/*     */   public int getRelCount() {
/* 129 */     return this.relCount;
/*     */   }
/*     */   
/*     */   public int getAbsCount() {
/* 133 */     return this.absCount;
/*     */   }
/*     */   
/*     */   public void setTrackDuration(String trackDuration) {
/* 137 */     this.trackDuration = trackDuration;
/*     */   }
/*     */   
/*     */   public void setRelTime(String relTime) {
/* 141 */     this.relTime = relTime;
/*     */   }
/*     */   
/*     */   public long getTrackDurationSeconds() {
/* 145 */     return (getTrackDuration() == null) ? 0L : ModelUtil.fromTimeString(getTrackDuration());
/*     */   }
/*     */   
/*     */   public long getTrackElapsedSeconds() {
/* 149 */     return (getRelTime() == null || getRelTime().equals("NOT_IMPLEMENTED")) ? 0L : ModelUtil.fromTimeString(getRelTime());
/*     */   }
/*     */   
/*     */   public long getTrackRemainingSeconds() {
/* 153 */     return getTrackDurationSeconds() - getTrackElapsedSeconds();
/*     */   }
/*     */   
/*     */   public int getElapsedPercent() {
/* 157 */     long elapsed = getTrackElapsedSeconds();
/* 158 */     long total = getTrackDurationSeconds();
/* 159 */     if (elapsed == 0L || total == 0L) return 0; 
/* 160 */     return (new Double(elapsed / total / 100.0D)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 166 */     return "(PositionInfo) Track: " + getTrack() + " RelTime: " + getRelTime() + " Duration: " + getTrackDuration() + " Percent: " + getElapsedPercent();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\PositionInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */