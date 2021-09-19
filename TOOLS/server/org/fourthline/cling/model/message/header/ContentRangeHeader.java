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
/*    */ public class ContentRangeHeader
/*    */   extends UpnpHeader<BytesRange>
/*    */ {
/*    */   public static final String PREFIX = "bytes ";
/*    */   
/*    */   public ContentRangeHeader() {}
/*    */   
/*    */   public ContentRangeHeader(BytesRange value) {
/* 34 */     setValue(value);
/*    */   }
/*    */   
/*    */   public ContentRangeHeader(String s) {
/* 38 */     setString(s);
/*    */   }
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/*    */     try {
/* 43 */       setValue(BytesRange.valueOf(s, "bytes "));
/* 44 */     } catch (InvalidValueException invalidValueException) {
/* 45 */       throw new InvalidHeaderException("Invalid Range Header: " + invalidValueException.getMessage());
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getString() {
/* 50 */     return getValue().getString(true, "bytes ");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\ContentRangeHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */