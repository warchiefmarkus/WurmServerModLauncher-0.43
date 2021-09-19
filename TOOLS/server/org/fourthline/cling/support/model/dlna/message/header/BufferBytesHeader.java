/*    */ package org.fourthline.cling.support.model.dlna.message.header;
/*    */ 
/*    */ import org.fourthline.cling.model.message.header.InvalidHeaderException;
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
/*    */ public class BufferBytesHeader
/*    */   extends DLNAHeader<UnsignedIntegerFourBytes>
/*    */ {
/*    */   public BufferBytesHeader() {
/* 26 */     setValue(new UnsignedIntegerFourBytes(0L));
/*    */   }
/*    */ 
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/*    */     try {
/* 32 */       setValue(new UnsignedIntegerFourBytes(s));
/*    */       return;
/* 34 */     } catch (NumberFormatException numberFormatException) {
/*    */       
/* 36 */       throw new InvalidHeaderException("Invalid header value: " + s);
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getString() {
/* 41 */     return ((UnsignedIntegerFourBytes)getValue()).getValue().toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\message\header\BufferBytesHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */