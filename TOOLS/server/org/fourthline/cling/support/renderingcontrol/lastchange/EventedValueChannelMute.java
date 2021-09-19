/*    */ package org.fourthline.cling.support.renderingcontrol.lastchange;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.fourthline.cling.model.types.BooleanDatatype;
/*    */ import org.fourthline.cling.model.types.Datatype;
/*    */ import org.fourthline.cling.model.types.InvalidValueException;
/*    */ import org.fourthline.cling.support.lastchange.EventedValue;
/*    */ import org.fourthline.cling.support.model.Channel;
/*    */ import org.fourthline.cling.support.shared.AbstractMap;
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
/*    */ public class EventedValueChannelMute
/*    */   extends EventedValue<ChannelMute>
/*    */ {
/*    */   public EventedValueChannelMute(ChannelMute value) {
/* 33 */     super(value);
/*    */   }
/*    */   
/*    */   public EventedValueChannelMute(Map.Entry<String, String>[] attributes) {
/* 37 */     super((Map.Entry[])attributes);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ChannelMute valueOf(Map.Entry<String, String>[] attributes) throws InvalidValueException {
/* 42 */     Channel channel = null;
/* 43 */     Boolean mute = null;
/* 44 */     for (Map.Entry<String, String> attribute : attributes) {
/* 45 */       if (((String)attribute.getKey()).equals("channel"))
/* 46 */         channel = Channel.valueOf(attribute.getValue()); 
/* 47 */       if (((String)attribute.getKey()).equals("val"))
/* 48 */         mute = (new BooleanDatatype()).valueOf(attribute.getValue()); 
/*    */     } 
/* 50 */     return (channel != null && mute != null) ? new ChannelMute(channel, mute) : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Map.Entry<String, String>[] getAttributes() {
/* 55 */     return (Map.Entry<String, String>[])new Map.Entry[] { (Map.Entry)new AbstractMap.SimpleEntry("val", (new BooleanDatatype())
/*    */ 
/*    */           
/* 58 */           .getString(((ChannelMute)getValue()).getMute())), (Map.Entry)new AbstractMap.SimpleEntry("channel", ((ChannelMute)
/*    */ 
/*    */ 
/*    */           
/* 62 */           getValue()).getChannel().name()) };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 69 */     return ((ChannelMute)getValue()).toString();
/*    */   }
/*    */ 
/*    */   
/*    */   protected Datatype getDatatype() {
/* 74 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\renderingcontrol\lastchange\EventedValueChannelMute.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */