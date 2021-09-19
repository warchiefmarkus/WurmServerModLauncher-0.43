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
/*    */ public class ChannelVolume
/*    */ {
/*    */   protected Channel channel;
/*    */   protected Integer volume;
/*    */   
/*    */   public ChannelVolume(Channel channel, Integer volume) {
/* 29 */     this.channel = channel;
/* 30 */     this.volume = volume;
/*    */   }
/*    */   
/*    */   public Channel getChannel() {
/* 34 */     return this.channel;
/*    */   }
/*    */   
/*    */   public Integer getVolume() {
/* 38 */     return this.volume;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 43 */     return "Volume: " + getVolume() + " (" + getChannel() + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\renderingcontrol\lastchange\ChannelVolume.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */