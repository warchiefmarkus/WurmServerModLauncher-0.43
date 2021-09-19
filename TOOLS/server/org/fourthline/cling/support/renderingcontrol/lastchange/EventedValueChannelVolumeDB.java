/*    */ package org.fourthline.cling.support.renderingcontrol.lastchange;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.fourthline.cling.model.types.Datatype;
/*    */ import org.fourthline.cling.model.types.InvalidValueException;
/*    */ import org.fourthline.cling.model.types.UnsignedIntegerTwoBytes;
/*    */ import org.fourthline.cling.model.types.UnsignedIntegerTwoBytesDatatype;
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
/*    */ public class EventedValueChannelVolumeDB
/*    */   extends EventedValue<ChannelVolumeDB>
/*    */ {
/*    */   public EventedValueChannelVolumeDB(ChannelVolumeDB value) {
/* 34 */     super(value);
/*    */   }
/*    */   
/*    */   public EventedValueChannelVolumeDB(Map.Entry<String, String>[] attributes) {
/* 38 */     super((Map.Entry[])attributes);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ChannelVolumeDB valueOf(Map.Entry<String, String>[] attributes) throws InvalidValueException {
/* 43 */     Channel channel = null;
/* 44 */     Integer volumeDB = null;
/* 45 */     for (Map.Entry<String, String> attribute : attributes) {
/* 46 */       if (((String)attribute.getKey()).equals("channel"))
/* 47 */         channel = Channel.valueOf(attribute.getValue()); 
/* 48 */       if (((String)attribute.getKey()).equals("val"))
/* 49 */         volumeDB = Integer.valueOf((new UnsignedIntegerTwoBytesDatatype())
/* 50 */             .valueOf(attribute.getValue())
/* 51 */             .getValue().intValue()); 
/*    */     } 
/* 53 */     return (channel != null && volumeDB != null) ? new ChannelVolumeDB(channel, volumeDB) : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Map.Entry<String, String>[] getAttributes() {
/* 58 */     return (Map.Entry<String, String>[])new Map.Entry[] { (Map.Entry)new AbstractMap.SimpleEntry("val", (new UnsignedIntegerTwoBytesDatatype())
/*    */ 
/*    */           
/* 61 */           .getString(new UnsignedIntegerTwoBytes(((ChannelVolumeDB)
/* 62 */               getValue()).getVolumeDB().intValue()))), (Map.Entry)new AbstractMap.SimpleEntry("channel", ((ChannelVolumeDB)
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 67 */           getValue()).getChannel().name()) };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 74 */     return ((ChannelVolumeDB)getValue()).toString();
/*    */   }
/*    */ 
/*    */   
/*    */   protected Datatype getDatatype() {
/* 79 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\renderingcontrol\lastchange\EventedValueChannelVolumeDB.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */