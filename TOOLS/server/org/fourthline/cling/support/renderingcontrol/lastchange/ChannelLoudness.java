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
/*    */ public class ChannelLoudness
/*    */ {
/*    */   protected Channel channel;
/*    */   protected Boolean loudness;
/*    */   
/*    */   public ChannelLoudness(Channel channel, Boolean loudness) {
/* 29 */     this.channel = channel;
/* 30 */     this.loudness = loudness;
/*    */   }
/*    */   
/*    */   public Channel getChannel() {
/* 34 */     return this.channel;
/*    */   }
/*    */   
/*    */   public Boolean getLoudness() {
/* 38 */     return this.loudness;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 43 */     return "Loudness: " + getLoudness() + " (" + getChannel() + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\renderingcontrol\lastchange\ChannelLoudness.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */