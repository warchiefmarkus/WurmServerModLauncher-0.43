/*     */ package org.fourthline.cling.support.model.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.fourthline.cling.support.model.DIDLObject;
/*     */ import org.fourthline.cling.support.model.Res;
/*     */ import org.fourthline.cling.support.model.StorageMedium;
/*     */ import org.fourthline.cling.support.model.container.Container;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Movie
/*     */   extends VideoItem
/*     */ {
/*  31 */   public static final DIDLObject.Class CLASS = new DIDLObject.Class("object.item.videoItem.movie");
/*     */   
/*     */   public Movie() {
/*  34 */     setClazz(CLASS);
/*     */   }
/*     */   
/*     */   public Movie(Item other) {
/*  38 */     super(other);
/*     */   }
/*     */   
/*     */   public Movie(String id, Container parent, String title, String creator, Res... resource) {
/*  42 */     this(id, parent.getId(), title, creator, resource);
/*     */   }
/*     */   
/*     */   public Movie(String id, String parentID, String title, String creator, Res... resource) {
/*  46 */     super(id, parentID, title, creator, resource);
/*  47 */     setClazz(CLASS);
/*     */   }
/*     */   
/*     */   public StorageMedium getStorageMedium() {
/*  51 */     return (StorageMedium)getFirstPropertyValue(DIDLObject.Property.UPNP.STORAGE_MEDIUM.class);
/*     */   }
/*     */   
/*     */   public Movie setStorageMedium(StorageMedium storageMedium) {
/*  55 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.STORAGE_MEDIUM(storageMedium));
/*  56 */     return this;
/*     */   }
/*     */   
/*     */   public Integer getDVDRegionCode() {
/*  60 */     return (Integer)getFirstPropertyValue(DIDLObject.Property.UPNP.DVD_REGION_CODE.class);
/*     */   }
/*     */   
/*     */   public Movie setDVDRegionCode(Integer DVDRegionCode) {
/*  64 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.DVD_REGION_CODE(DVDRegionCode));
/*  65 */     return this;
/*     */   }
/*     */   
/*     */   public String getChannelName() {
/*  69 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.CHANNEL_NAME.class);
/*     */   }
/*     */   
/*     */   public Movie setChannelName(String channelName) {
/*  73 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.CHANNEL_NAME(channelName));
/*  74 */     return this;
/*     */   }
/*     */   
/*     */   public String getFirstScheduledStartTime() {
/*  78 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.SCHEDULED_START_TIME.class);
/*     */   }
/*     */   
/*     */   public String[] getScheduledStartTimes() {
/*  82 */     List<String> list = getPropertyValues(DIDLObject.Property.UPNP.SCHEDULED_START_TIME.class);
/*  83 */     return list.<String>toArray(new String[list.size()]);
/*     */   }
/*     */   
/*     */   public Movie setScheduledStartTimes(String[] strings) {
/*  87 */     removeProperties(DIDLObject.Property.UPNP.SCHEDULED_START_TIME.class);
/*  88 */     for (String s : strings) {
/*  89 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.SCHEDULED_START_TIME(s));
/*     */     }
/*  91 */     return this;
/*     */   }
/*     */   
/*     */   public String getFirstScheduledEndTime() {
/*  95 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.SCHEDULED_END_TIME.class);
/*     */   }
/*     */   
/*     */   public String[] getScheduledEndTimes() {
/*  99 */     List<String> list = getPropertyValues(DIDLObject.Property.UPNP.SCHEDULED_END_TIME.class);
/* 100 */     return list.<String>toArray(new String[list.size()]);
/*     */   }
/*     */   
/*     */   public Movie setScheduledEndTimes(String[] strings) {
/* 104 */     removeProperties(DIDLObject.Property.UPNP.SCHEDULED_END_TIME.class);
/* 105 */     for (String s : strings) {
/* 106 */       addProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.SCHEDULED_END_TIME(s));
/*     */     }
/* 108 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\item\Movie.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */