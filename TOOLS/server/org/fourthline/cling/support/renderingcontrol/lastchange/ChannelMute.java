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
/*    */ public class ChannelMute
/*    */ {
/*    */   protected Channel channel;
/*    */   protected Boolean mute;
/*    */   
/*    */   public ChannelMute(Channel channel, Boolean mute) {
/* 29 */     this.channel = channel;
/* 30 */     this.mute = mute;
/*    */   }
/*    */   
/*    */   public Channel getChannel() {
/* 34 */     return this.channel;
/*    */   }
/*    */   
/*    */   public Boolean getMute() {
/* 38 */     return this.mute;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 43 */     return "Mute: " + getMute() + " (" + getChannel() + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\renderingcontrol\lastchange\ChannelMute.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */