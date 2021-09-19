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
/*    */ public class UnsignedIntegerOneByteDatatype
/*    */   extends AbstractDatatype<UnsignedIntegerOneByte>
/*    */ {
/*    */   public UnsignedIntegerOneByte valueOf(String s) throws InvalidValueException {
/* 24 */     if (s.equals("")) return null; 
/*    */     try {
/* 26 */       return new UnsignedIntegerOneByte(s);
/* 27 */     } catch (NumberFormatException ex) {
/* 28 */       throw new InvalidValueException("Can't convert string to number or not in range: " + s, ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\UnsignedIntegerOneByteDatatype.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */