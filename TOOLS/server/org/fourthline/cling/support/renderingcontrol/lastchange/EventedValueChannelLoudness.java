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
/*    */ public class EventedValueChannelLoudness
/*    */   extends EventedValue<ChannelLoudness>
/*    */ {
/*    */   public EventedValueChannelLoudness(ChannelLoudness value) {
/* 33 */     super(value);
/*    */   }
/*    */   
/*    */   public EventedValueChannelLoudness(Map.Entry<String, String>[] attributes) {
/* 37 */     super((Map.Entry[])attributes);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ChannelLoudness valueOf(Map.Entry<String, String>[] attributes) throws InvalidValueException {
/* 42 */     Channel channel = null;
/* 43 */     Boolean loudness = null;
/* 44 */     for (Map.Entry<String, String> attribute : attributes) {
/* 45 */       if (((String)attribute.getKey()).equals("channel"))
/* 46 */         channel = Channel.valueOf(attribute.getValue()); 
/* 47 */       if (((String)attribute.getKey()).equals("val"))
/* 48 */         loudness = (new BooleanDatatype()).valueOf(attribute.getValue()); 
/*    */     } 
/* 50 */     return (channel != null && loudness != null) ? new ChannelLoudness(channel, loudness) : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Map.Entry<String, String>[] getAttributes() {
/* 55 */     return (Map.Entry<String, String>[])new Map.Entry[] { (Map.Entry)new AbstractMap.SimpleEntry("val", (new BooleanDatatype())
/*    */ 
/*    */           
/* 58 */           .getString(((ChannelLoudness)getValue()).getLoudness())), (Map.Entry)new AbstractMap.SimpleEntry("channel", ((ChannelLoudness)
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
/* 69 */     return ((ChannelLoudness)getValue()).toString();
/*    */   }
/*    */ 
/*    */   
/*    */   protected Datatype getDatatype() {
/* 74 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\renderingcontrol\lastchange\EventedValueChannelLoudness.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */