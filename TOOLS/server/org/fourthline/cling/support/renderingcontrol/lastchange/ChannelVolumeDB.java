/*    */ package org.fourthline.cling.support.renderingcontrol.lastchange;
/*    */ 
/*    */ import org.fourthline.cling.support.model.Channel;
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
/*    */ public class ChannelVolumeDB
/*    */ {
/*    */   protected Channel channel;
/*    */   protected Integer volumeDB;
/*    */   
/*    */   public ChannelVolumeDB(Channel channel, Integer volumeDB) {
/* 29 */     this.channel = channel;
/* 30 */     this.volumeDB = volumeDB;
/*    */   }
/*    */   
/*    */   public Channel getChannel() {
/* 34 */     return this.channel;
/*    */   }
/*    */   
/*    */   public Integer getVolumeDB() {
/* 38 */     return this.volumeDB;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 43 */     return "VolumeDB: " + getVolumeDB() + " (" + getChannel() + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\renderingcontrol\lastchange\ChannelVolumeDB.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */