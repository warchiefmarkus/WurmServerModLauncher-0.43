/*    */ package org.fourthline.cling.model.types;
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
/*    */ public class UnsignedIntegerTwoBytesDatatype
/*    */   extends AbstractDatatype<UnsignedIntegerTwoBytes>
/*    */ {
/*    */   public UnsignedIntegerTwoBytes valueOf(String s) throws InvalidValueException {
/* 24 */     if (s.equals("")) return null; 
/*    */     try {
/* 26 */       return new UnsignedIntegerTwoBytes(s);
/* 27 */     } catch (NumberFormatException ex) {
/* 28 */       throw new InvalidValueException("Can't convert string to number or not in range: " + s, ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\UnsignedIntegerTwoBytesDatatype.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */