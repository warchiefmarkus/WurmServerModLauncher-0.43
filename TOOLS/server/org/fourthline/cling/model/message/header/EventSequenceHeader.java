/*    */ package org.fourthline.cling.model.message.header;
/*    */ 
/*    */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
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
/*    */ public class EventSequenceHeader
/*    */   extends UpnpHeader<UnsignedIntegerFourBytes>
/*    */ {
/*    */   public EventSequenceHeader() {}
/*    */   
/*    */   public EventSequenceHeader(long value) {
/* 29 */     setValue(new UnsignedIntegerFourBytes(value));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 35 */     if (!"0".equals(s)) {
/* 36 */       while (s.startsWith("0")) {
/* 37 */         s = s.substring(1);
/*    */       }
/*    */     }
/*    */     
/*    */     try {
/* 42 */       setValue(new UnsignedIntegerFourBytes(s));
/* 43 */     } catch (NumberFormatException ex) {
/* 44 */       throw new InvalidHeaderException("Invalid event sequence, " + ex.getMessage());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString() {
/* 50 */     return getValue().toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\EventSequenceHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */