/*     */ package org.fourthline.cling.support.model;
/*     */ 
/*     */ import java.util.Map;
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
/*     */ public class MediaInfo
/*     */ {
/*  28 */   private String currentURI = "";
/*  29 */   private String currentURIMetaData = "";
/*  30 */   private String nextURI = "NOT_IMPLEMENTED";
/*  31 */   private String nextURIMetaData = "NOT_IMPLEMENTED";
/*     */   
/*  33 */   private UnsignedIntegerFourBytes numberOfTracks = new UnsignedIntegerFourBytes(0L);
/*  34 */   private String mediaDuration = "00:00:00";
/*  35 */   private StorageMedium playMedium = StorageMedium.NONE;
/*  36 */   private StorageMedium recordMedium = StorageMedium.NOT_IMPLEMENTED;
/*  37 */   private RecordMediumWriteStatus writeStatus = RecordMediumWriteStatus.NOT_IMPLEMENTED;
/*     */ 
/*     */   
/*     */   public MediaInfo() {}
/*     */   
/*     */   public MediaInfo(Map<String, ActionArgumentValue> args) {
/*  43 */     this((String)((ActionArgumentValue)args
/*  44 */         .get("CurrentURI")).getValue(), (String)((ActionArgumentValue)args
/*  45 */         .get("CurrentURIMetaData")).getValue(), (String)((ActionArgumentValue)args
/*  46 */         .get("NextURI")).getValue(), (String)((ActionArgumentValue)args
/*  47 */         .get("NextURIMetaData")).getValue(), (UnsignedIntegerFourBytes)((ActionArgumentValue)args
/*     */         
/*  49 */         .get("NrTracks")).getValue(), (String)((ActionArgumentValue)args
/*  50 */         .get("MediaDuration")).getValue(), 
/*  51 */         StorageMedium.valueOrVendorSpecificOf((String)((ActionArgumentValue)args.get("PlayMedium")).getValue()), 
/*  52 */         StorageMedium.valueOrVendorSpecificOf((String)((ActionArgumentValue)args.get("RecordMedium")).getValue()), 
/*  53 */         RecordMediumWriteStatus.valueOrUnknownOf((String)((ActionArgumentValue)args.get("WriteStatus")).getValue()));
/*     */   }
/*     */ 
/*     */   
/*     */   public MediaInfo(String currentURI, String currentURIMetaData) {
/*  58 */     this.currentURI = currentURI;
/*  59 */     this.currentURIMetaData = currentURIMetaData;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaInfo(String currentURI, String currentURIMetaData, UnsignedIntegerFourBytes numberOfTracks, String mediaDuration, StorageMedium playMedium) {
/*  65 */     this.currentURI = currentURI;
/*  66 */     this.currentURIMetaData = currentURIMetaData;
/*  67 */     this.numberOfTracks = numberOfTracks;
/*  68 */     this.mediaDuration = mediaDuration;
/*  69 */     this.playMedium = playMedium;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaInfo(String currentURI, String currentURIMetaData, UnsignedIntegerFourBytes numberOfTracks, String mediaDuration, StorageMedium playMedium, StorageMedium recordMedium, RecordMediumWriteStatus writeStatus) {
/*  76 */     this.currentURI = currentURI;
/*  77 */     this.currentURIMetaData = currentURIMetaData;
/*  78 */     this.numberOfTracks = numberOfTracks;
/*  79 */     this.mediaDuration = mediaDuration;
/*  80 */     this.playMedium = playMedium;
/*  81 */     this.recordMedium = recordMedium;
/*  82 */     this.writeStatus = writeStatus;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaInfo(String currentURI, String currentURIMetaData, String nextURI, String nextURIMetaData, UnsignedIntegerFourBytes numberOfTracks, String mediaDuration, StorageMedium playMedium) {
/*  89 */     this.currentURI = currentURI;
/*  90 */     this.currentURIMetaData = currentURIMetaData;
/*  91 */     this.nextURI = nextURI;
/*  92 */     this.nextURIMetaData = nextURIMetaData;
/*  93 */     this.numberOfTracks = numberOfTracks;
/*  94 */     this.mediaDuration = mediaDuration;
/*  95 */     this.playMedium = playMedium;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaInfo(String currentURI, String currentURIMetaData, String nextURI, String nextURIMetaData, UnsignedIntegerFourBytes numberOfTracks, String mediaDuration, StorageMedium playMedium, StorageMedium recordMedium, RecordMediumWriteStatus writeStatus) {
/* 103 */     this.currentURI = currentURI;
/* 104 */     this.currentURIMetaData = currentURIMetaData;
/* 105 */     this.nextURI = nextURI;
/* 106 */     this.nextURIMetaData = nextURIMetaData;
/* 107 */     this.numberOfTracks = numberOfTracks;
/* 108 */     this.mediaDuration = mediaDuration;
/* 109 */     this.playMedium = playMedium;
/* 110 */     this.recordMedium = recordMedium;
/* 111 */     this.writeStatus = writeStatus;
/*     */   }
/*     */   
/*     */   public String getCurrentURI() {
/* 115 */     return this.currentURI;
/*     */   }
/*     */   
/*     */   public String getCurrentURIMetaData() {
/* 119 */     return this.currentURIMetaData;
/*     */   }
/*     */   
/*     */   public String getNextURI() {
/* 123 */     return this.nextURI;
/*     */   }
/*     */   
/*     */   public String getNextURIMetaData() {
/* 127 */     return this.nextURIMetaData;
/*     */   }
/*     */   
/*     */   public UnsignedIntegerFourBytes getNumberOfTracks() {
/* 131 */     return this.numberOfTracks;
/*     */   }
/*     */   
/*     */   public String getMediaDuration() {
/* 135 */     return this.mediaDuration;
/*     */   }
/*     */   
/*     */   public StorageMedium getPlayMedium() {
/* 139 */     return this.playMedium;
/*     */   }
/*     */   
/*     */   public StorageMedium getRecordMedium() {
/* 143 */     return this.recordMedium;
/*     */   }
/*     */   
/*     */   public RecordMediumWriteStatus getWriteStatus() {
/* 147 */     return this.writeStatus;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\MediaInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */