/*    */ package org.fourthline.cling.support.model.item;
/*    */ 
/*    */ import java.net.URI;
/*    */ import org.fourthline.cling.support.model.DIDLObject;
/*    */ import org.fourthline.cling.support.model.Res;
/*    */ import org.fourthline.cling.support.model.container.Container;
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
/*    */ public class VideoBroadcast
/*    */   extends VideoItem
/*    */ {
/* 30 */   public static final DIDLObject.Class CLASS = new DIDLObject.Class("object.item.videoItem.videoBroadcast");
/*    */   
/*    */   public VideoBroadcast() {
/* 33 */     setClazz(CLASS);
/*    */   }
/*    */   
/*    */   public VideoBroadcast(Item other) {
/* 37 */     super(other);
/*    */   }
/*    */   
/*    */   public VideoBroadcast(String id, Container parent, String title, String creator, Res... resource) {
/* 41 */     this(id, parent.getId(), title, creator, resource);
/*    */   }
/*    */   
/*    */   public VideoBroadcast(String id, String parentID, String title, String creator, Res... resource) {
/* 45 */     super(id, parentID, title, creator, resource);
/* 46 */     setClazz(CLASS);
/*    */   }
/*    */   
/*    */   public URI getIcon() {
/* 50 */     return (URI)getFirstPropertyValue(DIDLObject.Property.UPNP.ICON.class);
/*    */   }
/*    */   
/*    */   public VideoBroadcast setIcon(URI icon) {
/* 54 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.ICON(icon));
/* 55 */     return this;
/*    */   }
/*    */   
/*    */   public String getRegion() {
/* 59 */     return (String)getFirstPropertyValue(DIDLObject.Property.UPNP.REGION.class);
/*    */   }
/*    */   
/*    */   public VideoBroadcast setRegion(String region) {
/* 63 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.REGION(region));
/* 64 */     return this;
/*    */   }
/*    */   
/*    */   public Integer getChannelNr() {
/* 68 */     return (Integer)getFirstPropertyValue(DIDLObject.Property.UPNP.CHANNEL_NR.class);
/*    */   }
/*    */   
/*    */   public VideoBroadcast setChannelNr(Integer channelNr) {
/* 72 */     replaceFirstProperty((DIDLObject.Property)new DIDLObject.Property.UPNP.CHANNEL_NR(channelNr));
/* 73 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\item\VideoBroadcast.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */