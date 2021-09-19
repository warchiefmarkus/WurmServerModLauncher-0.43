/*     */ package org.fourthline.cling.support.avtransport;
/*     */ 
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.net.URI;
/*     */ import org.fourthline.cling.binding.annotations.UpnpAction;
/*     */ import org.fourthline.cling.binding.annotations.UpnpInputArgument;
/*     */ import org.fourthline.cling.binding.annotations.UpnpOutputArgument;
/*     */ import org.fourthline.cling.binding.annotations.UpnpService;
/*     */ import org.fourthline.cling.binding.annotations.UpnpServiceId;
/*     */ import org.fourthline.cling.binding.annotations.UpnpServiceType;
/*     */ import org.fourthline.cling.binding.annotations.UpnpStateVariable;
/*     */ import org.fourthline.cling.binding.annotations.UpnpStateVariables;
/*     */ import org.fourthline.cling.model.ModelUtil;
/*     */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
/*     */ import org.fourthline.cling.support.avtransport.lastchange.AVTransportLastChangeParser;
/*     */ import org.fourthline.cling.support.avtransport.lastchange.AVTransportVariable;
/*     */ import org.fourthline.cling.support.lastchange.EventedValue;
/*     */ import org.fourthline.cling.support.lastchange.LastChange;
/*     */ import org.fourthline.cling.support.lastchange.LastChangeDelegator;
/*     */ import org.fourthline.cling.support.lastchange.LastChangeParser;
/*     */ import org.fourthline.cling.support.model.DeviceCapabilities;
/*     */ import org.fourthline.cling.support.model.MediaInfo;
/*     */ import org.fourthline.cling.support.model.PlayMode;
/*     */ import org.fourthline.cling.support.model.PositionInfo;
/*     */ import org.fourthline.cling.support.model.RecordMediumWriteStatus;
/*     */ import org.fourthline.cling.support.model.RecordQualityMode;
/*     */ import org.fourthline.cling.support.model.SeekMode;
/*     */ import org.fourthline.cling.support.model.StorageMedium;
/*     */ import org.fourthline.cling.support.model.TransportAction;
/*     */ import org.fourthline.cling.support.model.TransportInfo;
/*     */ import org.fourthline.cling.support.model.TransportSettings;
/*     */ import org.fourthline.cling.support.model.TransportState;
/*     */ import org.fourthline.cling.support.model.TransportStatus;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @UpnpService(serviceId = @UpnpServiceId("AVTransport"), serviceType = @UpnpServiceType(value = "AVTransport", version = 1), stringConvertibleTypes = {LastChange.class})
/*     */ @UpnpStateVariables({@UpnpStateVariable(name = "TransportState", sendEvents = false, allowedValuesEnum = TransportState.class), @UpnpStateVariable(name = "TransportStatus", sendEvents = false, allowedValuesEnum = TransportStatus.class), @UpnpStateVariable(name = "PlaybackStorageMedium", sendEvents = false, defaultValue = "NONE", allowedValuesEnum = StorageMedium.class), @UpnpStateVariable(name = "RecordStorageMedium", sendEvents = false, defaultValue = "NOT_IMPLEMENTED", allowedValuesEnum = StorageMedium.class), @UpnpStateVariable(name = "PossiblePlaybackStorageMedia", sendEvents = false, datatype = "string", defaultValue = "NETWORK"), @UpnpStateVariable(name = "PossibleRecordStorageMedia", sendEvents = false, datatype = "string", defaultValue = "NOT_IMPLEMENTED"), @UpnpStateVariable(name = "CurrentPlayMode", sendEvents = false, defaultValue = "NORMAL", allowedValuesEnum = PlayMode.class), @UpnpStateVariable(name = "TransportPlaySpeed", sendEvents = false, datatype = "string", defaultValue = "1"), @UpnpStateVariable(name = "RecordMediumWriteStatus", sendEvents = false, defaultValue = "NOT_IMPLEMENTED", allowedValuesEnum = RecordMediumWriteStatus.class), @UpnpStateVariable(name = "CurrentRecordQualityMode", sendEvents = false, defaultValue = "NOT_IMPLEMENTED", allowedValuesEnum = RecordQualityMode.class), @UpnpStateVariable(name = "PossibleRecordQualityModes", sendEvents = false, datatype = "string", defaultValue = "NOT_IMPLEMENTED"), @UpnpStateVariable(name = "NumberOfTracks", sendEvents = false, datatype = "ui4", defaultValue = "0"), @UpnpStateVariable(name = "CurrentTrack", sendEvents = false, datatype = "ui4", defaultValue = "0"), @UpnpStateVariable(name = "CurrentTrackDuration", sendEvents = false, datatype = "string"), @UpnpStateVariable(name = "CurrentMediaDuration", sendEvents = false, datatype = "string", defaultValue = "00:00:00"), @UpnpStateVariable(name = "CurrentTrackMetaData", sendEvents = false, datatype = "string", defaultValue = "NOT_IMPLEMENTED"), @UpnpStateVariable(name = "CurrentTrackURI", sendEvents = false, datatype = "string"), @UpnpStateVariable(name = "AVTransportURI", sendEvents = false, datatype = "string"), @UpnpStateVariable(name = "AVTransportURIMetaData", sendEvents = false, datatype = "string", defaultValue = "NOT_IMPLEMENTED"), @UpnpStateVariable(name = "NextAVTransportURI", sendEvents = false, datatype = "string", defaultValue = "NOT_IMPLEMENTED"), @UpnpStateVariable(name = "NextAVTransportURIMetaData", sendEvents = false, datatype = "string", defaultValue = "NOT_IMPLEMENTED"), @UpnpStateVariable(name = "RelativeTimePosition", sendEvents = false, datatype = "string"), @UpnpStateVariable(name = "AbsoluteTimePosition", sendEvents = false, datatype = "string"), @UpnpStateVariable(name = "RelativeCounterPosition", sendEvents = false, datatype = "i4", defaultValue = "2147483647"), @UpnpStateVariable(name = "AbsoluteCounterPosition", sendEvents = false, datatype = "i4", defaultValue = "2147483647"), @UpnpStateVariable(name = "CurrentTransportActions", sendEvents = false, datatype = "string"), @UpnpStateVariable(name = "A_ARG_TYPE_SeekMode", sendEvents = false, allowedValuesEnum = SeekMode.class), @UpnpStateVariable(name = "A_ARG_TYPE_SeekTarget", sendEvents = false, datatype = "string"), @UpnpStateVariable(name = "A_ARG_TYPE_InstanceID", sendEvents = false, datatype = "ui4")})
/*     */ public abstract class AbstractAVTransportService
/*     */   implements LastChangeDelegator
/*     */ {
/*     */   @UpnpStateVariable(eventMaximumRateMilliseconds = 200)
/*     */   private final LastChange lastChange;
/*     */   protected final PropertyChangeSupport propertyChangeSupport;
/*     */   
/*     */   protected AbstractAVTransportService() {
/* 202 */     this.propertyChangeSupport = new PropertyChangeSupport(this);
/* 203 */     this.lastChange = new LastChange((LastChangeParser)new AVTransportLastChangeParser());
/*     */   }
/*     */   
/*     */   protected AbstractAVTransportService(LastChange lastChange) {
/* 207 */     this.propertyChangeSupport = new PropertyChangeSupport(this);
/* 208 */     this.lastChange = lastChange;
/*     */   }
/*     */   
/*     */   protected AbstractAVTransportService(PropertyChangeSupport propertyChangeSupport) {
/* 212 */     this.propertyChangeSupport = propertyChangeSupport;
/* 213 */     this.lastChange = new LastChange((LastChangeParser)new AVTransportLastChangeParser());
/*     */   }
/*     */   
/*     */   protected AbstractAVTransportService(PropertyChangeSupport propertyChangeSupport, LastChange lastChange) {
/* 217 */     this.propertyChangeSupport = propertyChangeSupport;
/* 218 */     this.lastChange = lastChange;
/*     */   }
/*     */ 
/*     */   
/*     */   public LastChange getLastChange() {
/* 223 */     return this.lastChange;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendCurrentState(LastChange lc, UnsignedIntegerFourBytes instanceId) throws Exception {
/* 229 */     MediaInfo mediaInfo = getMediaInfo(instanceId);
/* 230 */     TransportInfo transportInfo = getTransportInfo(instanceId);
/* 231 */     TransportSettings transportSettings = getTransportSettings(instanceId);
/* 232 */     PositionInfo positionInfo = getPositionInfo(instanceId);
/* 233 */     DeviceCapabilities deviceCaps = getDeviceCapabilities(instanceId);
/*     */     
/* 235 */     lc.setEventedValue(instanceId, new EventedValue[] { (EventedValue)new AVTransportVariable.AVTransportURI(
/*     */             
/* 237 */             URI.create(mediaInfo.getCurrentURI())), (EventedValue)new AVTransportVariable.AVTransportURIMetaData(mediaInfo
/* 238 */             .getCurrentURIMetaData()), (EventedValue)new AVTransportVariable.CurrentMediaDuration(mediaInfo
/* 239 */             .getMediaDuration()), (EventedValue)new AVTransportVariable.CurrentPlayMode(transportSettings
/* 240 */             .getPlayMode()), (EventedValue)new AVTransportVariable.CurrentRecordQualityMode(transportSettings
/* 241 */             .getRecQualityMode()), (EventedValue)new AVTransportVariable.CurrentTrack(positionInfo
/* 242 */             .getTrack()), (EventedValue)new AVTransportVariable.CurrentTrackDuration(positionInfo
/* 243 */             .getTrackDuration()), (EventedValue)new AVTransportVariable.CurrentTrackMetaData(positionInfo
/* 244 */             .getTrackMetaData()), (EventedValue)new AVTransportVariable.CurrentTrackURI(
/* 245 */             URI.create(positionInfo.getTrackURI())), (EventedValue)new AVTransportVariable.CurrentTransportActions(
/* 246 */             getCurrentTransportActions(instanceId)), (EventedValue)new AVTransportVariable.NextAVTransportURI(
/* 247 */             URI.create(mediaInfo.getNextURI())), (EventedValue)new AVTransportVariable.NextAVTransportURIMetaData(mediaInfo
/* 248 */             .getNextURIMetaData()), (EventedValue)new AVTransportVariable.NumberOfTracks(mediaInfo
/* 249 */             .getNumberOfTracks()), (EventedValue)new AVTransportVariable.PossiblePlaybackStorageMedia(deviceCaps
/* 250 */             .getPlayMedia()), (EventedValue)new AVTransportVariable.PossibleRecordQualityModes(deviceCaps
/* 251 */             .getRecQualityModes()), (EventedValue)new AVTransportVariable.PossibleRecordStorageMedia(deviceCaps
/* 252 */             .getRecMedia()), (EventedValue)new AVTransportVariable.RecordMediumWriteStatus(mediaInfo
/* 253 */             .getWriteStatus()), (EventedValue)new AVTransportVariable.RecordStorageMedium(mediaInfo
/* 254 */             .getRecordMedium()), (EventedValue)new AVTransportVariable.TransportPlaySpeed(transportInfo
/* 255 */             .getCurrentSpeed()), (EventedValue)new AVTransportVariable.TransportState(transportInfo
/* 256 */             .getCurrentTransportState()), (EventedValue)new AVTransportVariable.TransportStatus(transportInfo
/* 257 */             .getCurrentTransportStatus()) });
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyChangeSupport getPropertyChangeSupport() {
/* 262 */     return this.propertyChangeSupport;
/*     */   }
/*     */   
/*     */   public static UnsignedIntegerFourBytes getDefaultInstanceID() {
/* 266 */     return new UnsignedIntegerFourBytes(0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction
/*     */   public abstract void setAVTransportURI(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes paramUnsignedIntegerFourBytes, @UpnpInputArgument(name = "CurrentURI", stateVariable = "AVTransportURI") String paramString1, @UpnpInputArgument(name = "CurrentURIMetaData", stateVariable = "AVTransportURIMetaData") String paramString2) throws AVTransportException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction
/*     */   public abstract void setNextAVTransportURI(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes paramUnsignedIntegerFourBytes, @UpnpInputArgument(name = "NextURI", stateVariable = "AVTransportURI") String paramString1, @UpnpInputArgument(name = "NextURIMetaData", stateVariable = "AVTransportURIMetaData") String paramString2) throws AVTransportException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "NrTracks", stateVariable = "NumberOfTracks", getterName = "getNumberOfTracks"), @UpnpOutputArgument(name = "MediaDuration", stateVariable = "CurrentMediaDuration", getterName = "getMediaDuration"), @UpnpOutputArgument(name = "CurrentURI", stateVariable = "AVTransportURI", getterName = "getCurrentURI"), @UpnpOutputArgument(name = "CurrentURIMetaData", stateVariable = "AVTransportURIMetaData", getterName = "getCurrentURIMetaData"), @UpnpOutputArgument(name = "NextURI", stateVariable = "NextAVTransportURI", getterName = "getNextURI"), @UpnpOutputArgument(name = "NextURIMetaData", stateVariable = "NextAVTransportURIMetaData", getterName = "getNextURIMetaData"), @UpnpOutputArgument(name = "PlayMedium", stateVariable = "PlaybackStorageMedium", getterName = "getPlayMedium"), @UpnpOutputArgument(name = "RecordMedium", stateVariable = "RecordStorageMedium", getterName = "getRecordMedium"), @UpnpOutputArgument(name = "WriteStatus", stateVariable = "RecordMediumWriteStatus", getterName = "getWriteStatus")})
/*     */   public abstract MediaInfo getMediaInfo(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes paramUnsignedIntegerFourBytes) throws AVTransportException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "CurrentTransportState", stateVariable = "TransportState", getterName = "getCurrentTransportState"), @UpnpOutputArgument(name = "CurrentTransportStatus", stateVariable = "TransportStatus", getterName = "getCurrentTransportStatus"), @UpnpOutputArgument(name = "CurrentSpeed", stateVariable = "TransportPlaySpeed", getterName = "getCurrentSpeed")})
/*     */   public abstract TransportInfo getTransportInfo(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes paramUnsignedIntegerFourBytes) throws AVTransportException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "Track", stateVariable = "CurrentTrack", getterName = "getTrack"), @UpnpOutputArgument(name = "TrackDuration", stateVariable = "CurrentTrackDuration", getterName = "getTrackDuration"), @UpnpOutputArgument(name = "TrackMetaData", stateVariable = "CurrentTrackMetaData", getterName = "getTrackMetaData"), @UpnpOutputArgument(name = "TrackURI", stateVariable = "CurrentTrackURI", getterName = "getTrackURI"), @UpnpOutputArgument(name = "RelTime", stateVariable = "RelativeTimePosition", getterName = "getRelTime"), @UpnpOutputArgument(name = "AbsTime", stateVariable = "AbsoluteTimePosition", getterName = "getAbsTime"), @UpnpOutputArgument(name = "RelCount", stateVariable = "RelativeCounterPosition", getterName = "getRelCount"), @UpnpOutputArgument(name = "AbsCount", stateVariable = "AbsoluteCounterPosition", getterName = "getAbsCount")})
/*     */   public abstract PositionInfo getPositionInfo(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes paramUnsignedIntegerFourBytes) throws AVTransportException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "PlayMedia", stateVariable = "PossiblePlaybackStorageMedia", getterName = "getPlayMediaString"), @UpnpOutputArgument(name = "RecMedia", stateVariable = "PossibleRecordStorageMedia", getterName = "getRecMediaString"), @UpnpOutputArgument(name = "RecQualityModes", stateVariable = "PossibleRecordQualityModes", getterName = "getRecQualityModesString")})
/*     */   public abstract DeviceCapabilities getDeviceCapabilities(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes paramUnsignedIntegerFourBytes) throws AVTransportException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "PlayMode", stateVariable = "CurrentPlayMode", getterName = "getPlayMode"), @UpnpOutputArgument(name = "RecQualityMode", stateVariable = "CurrentRecordQualityMode", getterName = "getRecQualityMode")})
/*     */   public abstract TransportSettings getTransportSettings(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes paramUnsignedIntegerFourBytes) throws AVTransportException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction
/*     */   public abstract void stop(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes paramUnsignedIntegerFourBytes) throws AVTransportException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction
/*     */   public abstract void play(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes paramUnsignedIntegerFourBytes, @UpnpInputArgument(name = "Speed", stateVariable = "TransportPlaySpeed") String paramString) throws AVTransportException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction
/*     */   public abstract void pause(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes paramUnsignedIntegerFourBytes) throws AVTransportException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction
/*     */   public abstract void record(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes paramUnsignedIntegerFourBytes) throws AVTransportException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction
/*     */   public abstract void seek(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes paramUnsignedIntegerFourBytes, @UpnpInputArgument(name = "Unit", stateVariable = "A_ARG_TYPE_SeekMode") String paramString1, @UpnpInputArgument(name = "Target", stateVariable = "A_ARG_TYPE_SeekTarget") String paramString2) throws AVTransportException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction
/*     */   public abstract void next(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes paramUnsignedIntegerFourBytes) throws AVTransportException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction
/*     */   public abstract void previous(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes paramUnsignedIntegerFourBytes) throws AVTransportException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction
/*     */   public abstract void setPlayMode(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes paramUnsignedIntegerFourBytes, @UpnpInputArgument(name = "NewPlayMode", stateVariable = "CurrentPlayMode") String paramString) throws AVTransportException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction
/*     */   public abstract void setRecordQualityMode(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes paramUnsignedIntegerFourBytes, @UpnpInputArgument(name = "NewRecordQualityMode", stateVariable = "CurrentRecordQualityMode") String paramString) throws AVTransportException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction(name = "GetCurrentTransportActions", out = {@UpnpOutputArgument(name = "Actions", stateVariable = "CurrentTransportActions")})
/*     */   public String getCurrentTransportActionsString(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes instanceId) throws AVTransportException {
/*     */     try {
/* 376 */       return ModelUtil.toCommaSeparatedList((Object[])getCurrentTransportActions(instanceId));
/* 377 */     } catch (Exception ex) {
/* 378 */       return "";
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract TransportAction[] getCurrentTransportActions(UnsignedIntegerFourBytes paramUnsignedIntegerFourBytes) throws Exception;
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\avtransport\AbstractAVTransportService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */