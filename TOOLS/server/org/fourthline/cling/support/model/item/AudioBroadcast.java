/*    */ package org.fourthline.cling.support.model.item;
/*    */ 
/*    */ import org.fourthline.cling.support.model.DIDLObject;
/*    */ import org.fourthline.cling.support.model.Res;
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
/*    */ public class AudioBroadcast
/*    */   extends AudioItem
/*    */ {
/* 27 */   public static final DIDLObject.Class CLASS = new DIDLObject.Class("object.item.audioItem.audioBroadcast");
/*    */   
/*    */   public AudioBroadcast() {
/* 30 */     setClazz(CLASS);
/*    */   }
/*    */   
/*    */   public AudioBroadcast(Item other) {
/* 34 */     super(other);
/*    */   }
/*    */   
/*    */   public AudioBroadcast(String id, String parentID, String title, String creator, Res... resource) {
/* 38 */     super(id, parentID, title, creator, resource);
/* 39 */     setClazz(CLASS);
/*    */   }
/*    */   
/*    */   public String getRegion() {
/* 43 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.REGION.class);
/*    */   }
/*    */   
/*    */   public AudioBroadcast setRegion(String region) {
/* 47 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.REGION(region));
/* 48 */     return this;
/*    */   }
/*    */   
/*    */   public String getRadioCallSign() {
/* 52 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.RADIO_CALL_SIGN.class);
/*    */   }
/*    */   
/*    */   public AudioBroadcast setRadioCallSign(String radioCallSign) {
/* 56 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.RADIO_CALL_SIGN(radioCallSign));
/* 57 */     return this;
/*    */   }
/*    */   
/*    */   public String getRadioStationID() {
/* 61 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.RADIO_STATION_ID.class);
/*    */   }
/*    */   
/*    */   public AudioBroadcast setRadioStationID(String radioStationID) {
/* 65 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.RADIO_STATION_ID(radioStationID));
/* 66 */     return this;
/*    */   }
/*    */   
/*    */   public String getRadioBand() {
/* 70 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.RADIO_BAND.class);
/*    */   }
/*    */   
/*    */   public AudioBroadcast setRadioBand(String radioBand) {
/* 74 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.RADIO_BAND(radioBand));
/* 75 */     return this;
/*    */   }
/*    */   
/*    */   public Integer getChannelNr() {
/* 79 */     return (Integer)getFirstPropertyValue(DIDLObject.Property.UPNP.CHANNEL_NR.class);
/*    */   }
/*    */   
/*    */   public AudioBroadcast setChannelNr(Integer channelNr) {
/* 83 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.CHANNEL_NR(channelNr));
/* 84 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\item\AudioBroadcast.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */