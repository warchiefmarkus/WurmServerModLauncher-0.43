/*    */ package org.fourthline.cling.support.model;
/*    */ 
/*    */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
/*    */ import org.fourthline.cling.support.lastchange.LastChange;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AVTransport
/*    */ {
/*    */   protected final UnsignedIntegerFourBytes instanceID;
/*    */   protected final LastChange lastChange;
/*    */   protected MediaInfo mediaInfo;
/*    */   protected TransportInfo transportInfo;
/*    */   protected PositionInfo positionInfo;
/*    */   protected DeviceCapabilities deviceCapabilities;
/*    */   protected TransportSettings transportSettings;
/*    */   
/*    */   public AVTransport(UnsignedIntegerFourBytes instanceID, LastChange lastChange, StorageMedium possiblePlayMedium) {
/* 37 */     this(instanceID, lastChange, new StorageMedium[] { possiblePlayMedium });
/*    */   }
/*    */   
/*    */   public AVTransport(UnsignedIntegerFourBytes instanceID, LastChange lastChange, StorageMedium[] possiblePlayMedia) {
/* 41 */     this.instanceID = instanceID;
/* 42 */     this.lastChange = lastChange;
/* 43 */     setDeviceCapabilities(new DeviceCapabilities(possiblePlayMedia));
/* 44 */     setMediaInfo(new MediaInfo());
/* 45 */     setTransportInfo(new TransportInfo());
/* 46 */     setPositionInfo(new PositionInfo());
/* 47 */     setTransportSettings(new TransportSettings());
/*    */   }
/*    */   
/*    */   public UnsignedIntegerFourBytes getInstanceId() {
/* 51 */     return this.instanceID;
/*    */   }
/*    */   
/*    */   public LastChange getLastChange() {
/* 55 */     return this.lastChange;
/*    */   }
/*    */   
/*    */   public MediaInfo getMediaInfo() {
/* 59 */     return this.mediaInfo;
/*    */   }
/*    */   
/*    */   public void setMediaInfo(MediaInfo mediaInfo) {
/* 63 */     this.mediaInfo = mediaInfo;
/*    */   }
/*    */   
/*    */   public TransportInfo getTransportInfo() {
/* 67 */     return this.transportInfo;
/*    */   }
/*    */   
/*    */   public void setTransportInfo(TransportInfo transportInfo) {
/* 71 */     this.transportInfo = transportInfo;
/*    */   }
/*    */   
/*    */   public PositionInfo getPositionInfo() {
/* 75 */     return this.positionInfo;
/*    */   }
/*    */   
/*    */   public void setPositionInfo(PositionInfo positionInfo) {
/* 79 */     this.positionInfo = positionInfo;
/*    */   }
/*    */   
/*    */   public DeviceCapabilities getDeviceCapabilities() {
/* 83 */     return this.deviceCapabilities;
/*    */   }
/*    */   
/*    */   public void setDeviceCapabilities(DeviceCapabilities deviceCapabilities) {
/* 87 */     this.deviceCapabilities = deviceCapabilities;
/*    */   }
/*    */   
/*    */   public TransportSettings getTransportSettings() {
/* 91 */     return this.transportSettings;
/*    */   }
/*    */   
/*    */   public void setTransportSettings(TransportSettings transportSettings) {
/* 95 */     this.transportSettings = transportSettings;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\AVTransport.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */