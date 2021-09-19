/*    */ package org.fourthline.cling.support.model.dlna;
/*    */ 
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
/*    */ public class DLNAPlaySpeedAttribute
/*    */   extends DLNAAttribute<AVTransportVariable.TransportPlaySpeed[]>
/*    */ {
/*    */   public DLNAPlaySpeedAttribute() {
/* 26 */     setValue(new AVTransportVariable.TransportPlaySpeed[0]);
/*    */   }
/*    */   
/*    */   public DLNAPlaySpeedAttribute(AVTransportVariable.TransportPlaySpeed[] speeds) {
/* 30 */     setValue(speeds);
/*    */   }
/*    */   
/*    */   public DLNAPlaySpeedAttribute(String[] speeds) {
/* 34 */     AVTransportVariable.TransportPlaySpeed[] sp = new AVTransportVariable.TransportPlaySpeed[speeds.length];
/*    */     try {
/* 36 */       for (int i = 0; i < speeds.length; i++) {
/* 37 */         sp[i] = new AVTransportVariable.TransportPlaySpeed(speeds[i]);
/*    */       }
/* 39 */     } catch (InvalidValueException invalidValueException) {
/* 40 */       throw new InvalidDLNAProtocolAttributeException("Can't parse DLNA play speeds.");
/*    */     } 
/* 42 */     setValue(sp);
/*    */   }
/*    */   
/*    */   public void setString(String s, String cf) throws InvalidDLNAProtocolAttributeException {
/* 46 */     AVTransportVariable.TransportPlaySpeed[] value = null;
/* 47 */     if (s != null && s.length() != 0) {
/* 48 */       String[] speeds = s.split(",");
/*    */       try {
/* 50 */         value = new AVTransportVariable.TransportPlaySpeed[speeds.length];
/* 51 */         for (int i = 0; i < speeds.length; i++) {
/* 52 */           value[i] = new AVTransportVariable.TransportPlaySpeed(speeds[i]);
/*    */         }
/* 54 */       } catch (InvalidValueException invalidValueException) {
/* 55 */         value = null;
/*    */       } 
/*    */     } 
/* 58 */     if (value == null) {
/* 59 */       throw new InvalidDLNAProtocolAttributeException("Can't parse DLNA play speeds from: " + s);
/*    */     }
/* 61 */     setValue(value);
/*    */   }
/*    */   
/*    */   public String getString() {
/* 65 */     String s = "";
/* 66 */     for (AVTransportVariable.TransportPlaySpeed speed : getValue()) {
/* 67 */       if (!((String)speed.getValue()).equals("1"))
/*    */       {
/* 69 */         s = s + ((s.length() == 0) ? "" : ",") + speed; } 
/*    */     } 
/* 71 */     return s;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\DLNAPlaySpeedAttribute.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */