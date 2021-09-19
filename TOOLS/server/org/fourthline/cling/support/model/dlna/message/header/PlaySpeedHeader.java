/*    */ package org.fourthline.cling.support.model.dlna.message.header;
/*    */ 
/*    */ import org.fourthline.cling.model.message.header.InvalidHeaderException;
/*    */ import org.fourthline.cling.model.types.InvalidValueException;
/*    */ import org.fourthline.cling.support.avtransport.lastchange.AVTransportVariable;
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
/*    */ public class PlaySpeedHeader
/*    */   extends DLNAHeader<AVTransportVariable.TransportPlaySpeed>
/*    */ {
/*    */   public PlaySpeedHeader() {}
/*    */   
/*    */   public PlaySpeedHeader(AVTransportVariable.TransportPlaySpeed speed) {
/* 30 */     setValue(speed);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 35 */     if (s.length() != 0) {
/*    */       try {
/* 37 */         AVTransportVariable.TransportPlaySpeed t = new AVTransportVariable.TransportPlaySpeed(s);
/* 38 */         setValue(t);
/*    */         return;
/* 40 */       } catch (InvalidValueException invalidValueException) {}
/*    */     }
/* 42 */     throw new InvalidHeaderException("Invalid PlaySpeed header value: " + s);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString() {
/* 47 */     return (String)((AVTransportVariable.TransportPlaySpeed)getValue()).getValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\message\header\PlaySpeedHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */