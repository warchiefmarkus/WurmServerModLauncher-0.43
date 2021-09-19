/*     */ package org.fourthline.cling.support.avtransport.lastchange;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Pattern;
/*     */ import org.fourthline.cling.model.types.InvalidValueException;
/*     */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
/*     */ import org.fourthline.cling.support.lastchange.EventedValue;
/*     */ import org.fourthline.cling.support.lastchange.EventedValueEnum;
/*     */ import org.fourthline.cling.support.lastchange.EventedValueEnumArray;
/*     */ import org.fourthline.cling.support.lastchange.EventedValueString;
/*     */ import org.fourthline.cling.support.lastchange.EventedValueURI;
/*     */ import org.fourthline.cling.support.lastchange.EventedValueUnsignedIntegerFourBytes;
/*     */ import org.fourthline.cling.support.model.PlayMode;
/*     */ import org.fourthline.cling.support.model.RecordQualityMode;
/*     */ import org.fourthline.cling.support.model.StorageMedium;
/*     */ import org.fourthline.cling.support.model.TransportAction;
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
/*     */ public class AVTransportVariable
/*     */ {
/*  44 */   public static Set<Class<? extends EventedValue>> ALL = new HashSet<Class<? extends EventedValue>>()
/*     */     {
/*     */     
/*     */     };
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
/*     */   public static class TransportState
/*     */     extends EventedValueEnum<org.fourthline.cling.support.model.TransportState>
/*     */   {
/*     */     public TransportState(org.fourthline.cling.support.model.TransportState avTransportState) {
/*  74 */       super((Enum)avTransportState);
/*     */     }
/*     */     
/*     */     public TransportState(Map.Entry<String, String>[] attributes) {
/*  78 */       super((Map.Entry[])attributes);
/*     */     }
/*     */ 
/*     */     
/*     */     protected org.fourthline.cling.support.model.TransportState enumValueOf(String s) {
/*  83 */       return org.fourthline.cling.support.model.TransportState.valueOf(s);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class TransportStatus extends EventedValueEnum<org.fourthline.cling.support.model.TransportStatus> {
/*     */     public TransportStatus(org.fourthline.cling.support.model.TransportStatus transportStatus) {
/*  89 */       super((Enum)transportStatus);
/*     */     }
/*     */     
/*     */     public TransportStatus(Map.Entry<String, String>[] attributes) {
/*  93 */       super((Map.Entry[])attributes);
/*     */     }
/*     */ 
/*     */     
/*     */     protected org.fourthline.cling.support.model.TransportStatus enumValueOf(String s) {
/*  98 */       return org.fourthline.cling.support.model.TransportStatus.valueOf(s);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class RecordStorageMedium
/*     */     extends EventedValueEnum<StorageMedium> {
/*     */     public RecordStorageMedium(StorageMedium storageMedium) {
/* 105 */       super((Enum)storageMedium);
/*     */     }
/*     */     
/*     */     public RecordStorageMedium(Map.Entry<String, String>[] attributes) {
/* 109 */       super((Map.Entry[])attributes);
/*     */     }
/*     */ 
/*     */     
/*     */     protected StorageMedium enumValueOf(String s) {
/* 114 */       return StorageMedium.valueOf(s);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class PossibleRecordStorageMedia extends EventedValueEnumArray<StorageMedium> {
/*     */     public PossibleRecordStorageMedia(StorageMedium[] e) {
/* 120 */       super((Enum[])e);
/*     */     }
/*     */     
/*     */     public PossibleRecordStorageMedia(Map.Entry<String, String>[] attributes) {
/* 124 */       super((Map.Entry[])attributes);
/*     */     }
/*     */ 
/*     */     
/*     */     protected StorageMedium[] enumValueOf(String[] names) {
/* 129 */       List<StorageMedium> list = new ArrayList<>();
/* 130 */       for (String s : names) {
/* 131 */         list.add(StorageMedium.valueOf(s));
/*     */       }
/* 133 */       return list.<StorageMedium>toArray(new StorageMedium[list.size()]);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class PossiblePlaybackStorageMedia extends PossibleRecordStorageMedia {
/*     */     public PossiblePlaybackStorageMedia(StorageMedium[] e) {
/* 139 */       super(e);
/*     */     }
/*     */     
/*     */     public PossiblePlaybackStorageMedia(Map.Entry<String, String>[] attributes) {
/* 143 */       super(attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class CurrentPlayMode extends EventedValueEnum<PlayMode> {
/*     */     public CurrentPlayMode(PlayMode playMode) {
/* 149 */       super((Enum)playMode);
/*     */     }
/*     */     
/*     */     public CurrentPlayMode(Map.Entry<String, String>[] attributes) {
/* 153 */       super((Map.Entry[])attributes);
/*     */     }
/*     */ 
/*     */     
/*     */     protected PlayMode enumValueOf(String s) {
/* 158 */       return PlayMode.valueOf(s);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class TransportPlaySpeed extends EventedValueString {
/* 163 */     static final Pattern pattern = Pattern.compile("^-?\\d+(/\\d+)?$", 2);
/*     */     
/*     */     public TransportPlaySpeed(String value) {
/* 166 */       super(value);
/* 167 */       if (!pattern.matcher(value).matches()) {
/* 168 */         throw new InvalidValueException("Can't parse TransportPlaySpeed speeds.");
/*     */       }
/*     */     }
/*     */     
/*     */     public TransportPlaySpeed(Map.Entry<String, String>[] attributes) {
/* 173 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class RecordMediumWriteStatus extends EventedValueEnum<org.fourthline.cling.support.model.RecordMediumWriteStatus> {
/*     */     public RecordMediumWriteStatus(org.fourthline.cling.support.model.RecordMediumWriteStatus recordMediumWriteStatus) {
/* 179 */       super((Enum)recordMediumWriteStatus);
/*     */     }
/*     */     
/*     */     public RecordMediumWriteStatus(Map.Entry<String, String>[] attributes) {
/* 183 */       super((Map.Entry[])attributes);
/*     */     }
/*     */ 
/*     */     
/*     */     protected org.fourthline.cling.support.model.RecordMediumWriteStatus enumValueOf(String s) {
/* 188 */       return org.fourthline.cling.support.model.RecordMediumWriteStatus.valueOf(s);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class CurrentRecordQualityMode extends EventedValueEnum<RecordQualityMode> {
/*     */     public CurrentRecordQualityMode(RecordQualityMode recordQualityMode) {
/* 194 */       super((Enum)recordQualityMode);
/*     */     }
/*     */     
/*     */     public CurrentRecordQualityMode(Map.Entry<String, String>[] attributes) {
/* 198 */       super((Map.Entry[])attributes);
/*     */     }
/*     */ 
/*     */     
/*     */     protected RecordQualityMode enumValueOf(String s) {
/* 203 */       return RecordQualityMode.valueOf(s);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class PossibleRecordQualityModes extends EventedValueEnumArray<RecordQualityMode> {
/*     */     public PossibleRecordQualityModes(RecordQualityMode[] e) {
/* 209 */       super((Enum[])e);
/*     */     }
/*     */     
/*     */     public PossibleRecordQualityModes(Map.Entry<String, String>[] attributes) {
/* 213 */       super((Map.Entry[])attributes);
/*     */     }
/*     */ 
/*     */     
/*     */     protected RecordQualityMode[] enumValueOf(String[] names) {
/* 218 */       List<RecordQualityMode> list = new ArrayList<>();
/* 219 */       for (String s : names) {
/* 220 */         list.add(RecordQualityMode.valueOf(s));
/*     */       }
/* 222 */       return list.<RecordQualityMode>toArray(new RecordQualityMode[list.size()]);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class NumberOfTracks extends EventedValueUnsignedIntegerFourBytes {
/*     */     public NumberOfTracks(UnsignedIntegerFourBytes value) {
/* 228 */       super(value);
/*     */     }
/*     */     
/*     */     public NumberOfTracks(Map.Entry<String, String>[] attributes) {
/* 232 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class CurrentTrack extends EventedValueUnsignedIntegerFourBytes {
/*     */     public CurrentTrack(UnsignedIntegerFourBytes value) {
/* 238 */       super(value);
/*     */     }
/*     */     
/*     */     public CurrentTrack(Map.Entry<String, String>[] attributes) {
/* 242 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class CurrentTrackDuration extends EventedValueString {
/*     */     public CurrentTrackDuration(String value) {
/* 248 */       super(value);
/*     */     }
/*     */     
/*     */     public CurrentTrackDuration(Map.Entry<String, String>[] attributes) {
/* 252 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class CurrentMediaDuration extends EventedValueString {
/*     */     public CurrentMediaDuration(String value) {
/* 258 */       super(value);
/*     */     }
/*     */     
/*     */     public CurrentMediaDuration(Map.Entry<String, String>[] attributes) {
/* 262 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class CurrentTrackMetaData extends EventedValueString {
/*     */     public CurrentTrackMetaData(String value) {
/* 268 */       super(value);
/*     */     }
/*     */     
/*     */     public CurrentTrackMetaData(Map.Entry<String, String>[] attributes) {
/* 272 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class CurrentTrackURI extends EventedValueURI {
/*     */     public CurrentTrackURI(URI value) {
/* 278 */       super(value);
/*     */     }
/*     */     
/*     */     public CurrentTrackURI(Map.Entry<String, String>[] attributes) {
/* 282 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class AVTransportURI extends EventedValueURI {
/*     */     public AVTransportURI(URI value) {
/* 288 */       super(value);
/*     */     }
/*     */     
/*     */     public AVTransportURI(Map.Entry<String, String>[] attributes) {
/* 292 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class NextAVTransportURI extends EventedValueURI {
/*     */     public NextAVTransportURI(URI value) {
/* 298 */       super(value);
/*     */     }
/*     */     
/*     */     public NextAVTransportURI(Map.Entry<String, String>[] attributes) {
/* 302 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class AVTransportURIMetaData extends EventedValueString {
/*     */     public AVTransportURIMetaData(String value) {
/* 308 */       super(value);
/*     */     }
/*     */     
/*     */     public AVTransportURIMetaData(Map.Entry<String, String>[] attributes) {
/* 312 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class NextAVTransportURIMetaData extends EventedValueString {
/*     */     public NextAVTransportURIMetaData(String value) {
/* 318 */       super(value);
/*     */     }
/*     */     
/*     */     public NextAVTransportURIMetaData(Map.Entry<String, String>[] attributes) {
/* 322 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class CurrentTransportActions extends EventedValueEnumArray<TransportAction> {
/*     */     public CurrentTransportActions(TransportAction[] e) {
/* 328 */       super((Enum[])e);
/*     */     }
/*     */     
/*     */     public CurrentTransportActions(Map.Entry<String, String>[] attributes) {
/* 332 */       super((Map.Entry[])attributes);
/*     */     }
/*     */ 
/*     */     
/*     */     protected TransportAction[] enumValueOf(String[] names) {
/* 337 */       if (names == null) return new TransportAction[0]; 
/* 338 */       List<TransportAction> list = new ArrayList<>();
/* 339 */       for (String s : names) {
/* 340 */         list.add(TransportAction.valueOf(s));
/*     */       }
/* 342 */       return list.<TransportAction>toArray(new TransportAction[list.size()]);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class RelativeTimePosition extends EventedValueString {
/*     */     public RelativeTimePosition(String value) {
/* 348 */       super(value);
/*     */     }
/*     */     
/*     */     public RelativeTimePosition(Map.Entry<String, String>[] attributes) {
/* 352 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class AbsoluteTimePosition extends EventedValueString {
/*     */     public AbsoluteTimePosition(String value) {
/* 358 */       super(value);
/*     */     }
/*     */     
/*     */     public AbsoluteTimePosition(Map.Entry<String, String>[] attributes) {
/* 362 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class RelativeCounterPosition extends EventedValueString {
/*     */     public RelativeCounterPosition(String value) {
/* 368 */       super(value);
/*     */     }
/*     */     
/*     */     public RelativeCounterPosition(Map.Entry<String, String>[] attributes) {
/* 372 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class AbsoluteCounterPosition extends EventedValueString {
/*     */     public AbsoluteCounterPosition(String value) {
/* 378 */       super(value);
/*     */     }
/*     */     
/*     */     public AbsoluteCounterPosition(Map.Entry<String, String>[] attributes) {
/* 382 */       super((Map.Entry[])attributes);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\avtransport\lastchange\AVTransportVariable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */