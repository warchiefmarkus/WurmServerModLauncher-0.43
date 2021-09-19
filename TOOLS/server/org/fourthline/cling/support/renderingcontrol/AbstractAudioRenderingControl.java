/*     */ package org.fourthline.cling.support.renderingcontrol;
/*     */ 
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import org.fourthline.cling.binding.annotations.UpnpAction;
/*     */ import org.fourthline.cling.binding.annotations.UpnpInputArgument;
/*     */ import org.fourthline.cling.binding.annotations.UpnpOutputArgument;
/*     */ import org.fourthline.cling.binding.annotations.UpnpService;
/*     */ import org.fourthline.cling.binding.annotations.UpnpServiceId;
/*     */ import org.fourthline.cling.binding.annotations.UpnpServiceType;
/*     */ import org.fourthline.cling.binding.annotations.UpnpStateVariable;
/*     */ import org.fourthline.cling.binding.annotations.UpnpStateVariables;
/*     */ import org.fourthline.cling.model.types.ErrorCode;
/*     */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
/*     */ import org.fourthline.cling.model.types.UnsignedIntegerTwoBytes;
/*     */ import org.fourthline.cling.support.lastchange.EventedValue;
/*     */ import org.fourthline.cling.support.lastchange.LastChange;
/*     */ import org.fourthline.cling.support.lastchange.LastChangeDelegator;
/*     */ import org.fourthline.cling.support.lastchange.LastChangeParser;
/*     */ import org.fourthline.cling.support.model.Channel;
/*     */ import org.fourthline.cling.support.model.PresetName;
/*     */ import org.fourthline.cling.support.model.VolumeDBRange;
/*     */ import org.fourthline.cling.support.renderingcontrol.lastchange.ChannelLoudness;
/*     */ import org.fourthline.cling.support.renderingcontrol.lastchange.ChannelMute;
/*     */ import org.fourthline.cling.support.renderingcontrol.lastchange.ChannelVolume;
/*     */ import org.fourthline.cling.support.renderingcontrol.lastchange.ChannelVolumeDB;
/*     */ import org.fourthline.cling.support.renderingcontrol.lastchange.RenderingControlLastChangeParser;
/*     */ import org.fourthline.cling.support.renderingcontrol.lastchange.RenderingControlVariable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @UpnpService(serviceId = @UpnpServiceId("RenderingControl"), serviceType = @UpnpServiceType(value = "RenderingControl", version = 1), stringConvertibleTypes = {LastChange.class})
/*     */ @UpnpStateVariables({@UpnpStateVariable(name = "PresetNameList", sendEvents = false, datatype = "string"), @UpnpStateVariable(name = "Mute", sendEvents = false, datatype = "boolean"), @UpnpStateVariable(name = "Volume", sendEvents = false, datatype = "ui2", allowedValueMinimum = 0L, allowedValueMaximum = 100L), @UpnpStateVariable(name = "VolumeDB", sendEvents = false, datatype = "i2", allowedValueMinimum = -36864L, allowedValueMaximum = 32767L), @UpnpStateVariable(name = "Loudness", sendEvents = false, datatype = "boolean"), @UpnpStateVariable(name = "A_ARG_TYPE_Channel", sendEvents = false, allowedValuesEnum = Channel.class), @UpnpStateVariable(name = "A_ARG_TYPE_PresetName", sendEvents = false, allowedValuesEnum = PresetName.class), @UpnpStateVariable(name = "A_ARG_TYPE_InstanceID", sendEvents = false, datatype = "ui4")})
/*     */ public abstract class AbstractAudioRenderingControl
/*     */   implements LastChangeDelegator
/*     */ {
/*     */   @UpnpStateVariable(eventMaximumRateMilliseconds = 200)
/*     */   private final LastChange lastChange;
/*     */   protected final PropertyChangeSupport propertyChangeSupport;
/*     */   
/*     */   protected AbstractAudioRenderingControl() {
/*  98 */     this.propertyChangeSupport = new PropertyChangeSupport(this);
/*  99 */     this.lastChange = new LastChange((LastChangeParser)new RenderingControlLastChangeParser());
/*     */   }
/*     */   
/*     */   protected AbstractAudioRenderingControl(LastChange lastChange) {
/* 103 */     this.propertyChangeSupport = new PropertyChangeSupport(this);
/* 104 */     this.lastChange = lastChange;
/*     */   }
/*     */   
/*     */   protected AbstractAudioRenderingControl(PropertyChangeSupport propertyChangeSupport) {
/* 108 */     this.propertyChangeSupport = propertyChangeSupport;
/* 109 */     this.lastChange = new LastChange((LastChangeParser)new RenderingControlLastChangeParser());
/*     */   }
/*     */   
/*     */   protected AbstractAudioRenderingControl(PropertyChangeSupport propertyChangeSupport, LastChange lastChange) {
/* 113 */     this.propertyChangeSupport = propertyChangeSupport;
/* 114 */     this.lastChange = lastChange;
/*     */   }
/*     */ 
/*     */   
/*     */   public LastChange getLastChange() {
/* 119 */     return this.lastChange;
/*     */   }
/*     */ 
/*     */   
/*     */   public void appendCurrentState(LastChange lc, UnsignedIntegerFourBytes instanceId) throws Exception {
/* 124 */     for (Channel channel : getCurrentChannels()) {
/* 125 */       String channelString = channel.name();
/* 126 */       lc.setEventedValue(instanceId, new EventedValue[] { (EventedValue)new RenderingControlVariable.Mute(new ChannelMute(channel, 
/*     */                 
/* 128 */                 Boolean.valueOf(getMute(instanceId, channelString)))), (EventedValue)new RenderingControlVariable.Loudness(new ChannelLoudness(channel, 
/* 129 */                 Boolean.valueOf(getLoudness(instanceId, channelString)))), (EventedValue)new RenderingControlVariable.Volume(new ChannelVolume(channel, 
/* 130 */                 Integer.valueOf(getVolume(instanceId, channelString).getValue().intValue()))), (EventedValue)new RenderingControlVariable.VolumeDB(new ChannelVolumeDB(channel, 
/* 131 */                 getVolumeDB(instanceId, channelString))), (EventedValue)new RenderingControlVariable.PresetNameList(PresetName.FactoryDefaults
/* 132 */               .name()) });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyChangeSupport getPropertyChangeSupport() {
/* 138 */     return this.propertyChangeSupport;
/*     */   }
/*     */   
/*     */   public static UnsignedIntegerFourBytes getDefaultInstanceID() {
/* 142 */     return new UnsignedIntegerFourBytes(0L);
/*     */   }
/*     */   
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "CurrentPresetNameList", stateVariable = "PresetNameList")})
/*     */   public String listPresets(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes instanceId) throws RenderingControlException {
/* 147 */     return PresetName.FactoryDefaults.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction
/*     */   public void selectPreset(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes instanceId, @UpnpInputArgument(name = "PresetName") String presetName) throws RenderingControlException {}
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "CurrentMute", stateVariable = "Mute")})
/*     */   public abstract boolean getMute(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes paramUnsignedIntegerFourBytes, @UpnpInputArgument(name = "Channel") String paramString) throws RenderingControlException;
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction
/*     */   public abstract void setMute(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes paramUnsignedIntegerFourBytes, @UpnpInputArgument(name = "Channel") String paramString, @UpnpInputArgument(name = "DesiredMute", stateVariable = "Mute") boolean paramBoolean) throws RenderingControlException;
/*     */ 
/*     */   
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "CurrentVolume", stateVariable = "Volume")})
/*     */   public abstract UnsignedIntegerTwoBytes getVolume(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes paramUnsignedIntegerFourBytes, @UpnpInputArgument(name = "Channel") String paramString) throws RenderingControlException;
/*     */ 
/*     */   
/*     */   @UpnpAction
/*     */   public abstract void setVolume(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes paramUnsignedIntegerFourBytes, @UpnpInputArgument(name = "Channel") String paramString, @UpnpInputArgument(name = "DesiredVolume", stateVariable = "Volume") UnsignedIntegerTwoBytes paramUnsignedIntegerTwoBytes) throws RenderingControlException;
/*     */ 
/*     */   
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "CurrentVolume", stateVariable = "VolumeDB")})
/*     */   public Integer getVolumeDB(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes instanceId, @UpnpInputArgument(name = "Channel") String channelName) throws RenderingControlException {
/* 176 */     return Integer.valueOf(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction
/*     */   public void setVolumeDB(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes instanceId, @UpnpInputArgument(name = "Channel") String channelName, @UpnpInputArgument(name = "DesiredVolume", stateVariable = "VolumeDB") Integer desiredVolumeDB) throws RenderingControlException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "MinValue", stateVariable = "VolumeDB", getterName = "getMinValue"), @UpnpOutputArgument(name = "MaxValue", stateVariable = "VolumeDB", getterName = "getMaxValue")})
/*     */   public VolumeDBRange getVolumeDBRange(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes instanceId, @UpnpInputArgument(name = "Channel") String channelName) throws RenderingControlException {
/* 196 */     return new VolumeDBRange(Integer.valueOf(0), Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "CurrentLoudness", stateVariable = "Loudness")})
/*     */   public boolean getLoudness(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes instanceId, @UpnpInputArgument(name = "Channel") String channelName) throws RenderingControlException {
/* 202 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction
/*     */   public void setLoudness(@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes instanceId, @UpnpInputArgument(name = "Channel") String channelName, @UpnpInputArgument(name = "DesiredLoudness", stateVariable = "Loudness") boolean desiredLoudness) throws RenderingControlException {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Channel[] getCurrentChannels();
/*     */ 
/*     */ 
/*     */   
/*     */   protected Channel getChannel(String channelName) throws RenderingControlException {
/*     */     try {
/* 220 */       return Channel.valueOf(channelName);
/* 221 */     } catch (IllegalArgumentException ex) {
/* 222 */       throw new RenderingControlException(ErrorCode.ARGUMENT_VALUE_INVALID, "Unsupported audio channel: " + channelName);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\renderingcontrol\AbstractAudioRenderingControl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */