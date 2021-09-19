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
/*    */ public final class UnsignedIntegerTwoBytes
/*    */   extends UnsignedVariableInteger
/*    */ {
/*    */   public UnsignedIntegerTwoBytes(long value) throws NumberFormatException {
/* 24 */     super(value);
/*    */   }
/*    */   
/*    */   public UnsignedIntegerTwoBytes(String s) throws NumberFormatException {
/* 28 */     super(s);
/*    */   }
/*    */   
/*    */   public UnsignedVariableInteger.Bits getBits() {
/* 32 */     return UnsignedVariableInteger.Bits.SIXTEEN;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\UnsignedIntegerTwoBytes.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */