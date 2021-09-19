/*    */ package org.fourthline.cling.support.model.dlna.message.header;
/*    */ 
/*    */ import org.fourthline.cling.model.message.header.InvalidHeaderException;
/*    */ import org.fourthline.cling.support.model.dlna.types.BufferInfoType;
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
/*    */ 
/*    */ 
/*    */ public class BufferInfoHeader
/*    */   extends DLNAHeader<BufferInfoType>
/*    */ {
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 30 */     if (s.length() != 0) {
/*    */       try {
/* 32 */         setValue(BufferInfoType.valueOf(s));
/*    */         return;
/* 34 */       } catch (Exception exception) {}
/*    */     }
/* 36 */     throw new InvalidHeaderException("Invalid BufferInfo header value: " + s);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString() {
/* 41 */     return ((BufferInfoType)getValue()).getString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\message\header\BufferInfoHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */