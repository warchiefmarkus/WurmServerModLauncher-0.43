/*    */ package org.fourthline.cling.model.message.header;
/*    */ 
/*    */ import org.fourthline.cling.model.types.BytesRange;
/*    */ import org.fourthline.cling.model.types.InvalidValueException;
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
/*    */ public class RangeHeader
/*    */   extends UpnpHeader<BytesRange>
/*    */ {
/*    */   public RangeHeader() {}
/*    */   
/*    */   public RangeHeader(BytesRange value) {
/* 32 */     setValue(value);
/*    */   }
/*    */   
/*    */   public RangeHeader(String s) {
/* 36 */     setString(s);
/*    */   }
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/*    */     try {
/* 41 */       setValue(BytesRange.valueOf(s));
/* 42 */     } catch (InvalidValueException invalidValueException) {
/* 43 */       throw new InvalidHeaderException("Invalid Range Header: " + invalidValueException.getMessage());
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getString() {
/* 48 */     return getValue().getString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\RangeHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */