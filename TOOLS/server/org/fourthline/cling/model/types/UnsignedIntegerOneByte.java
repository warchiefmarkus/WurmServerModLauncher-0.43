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
/*    */ public final class UnsignedIntegerOneByte
/*    */   extends UnsignedVariableInteger
/*    */ {
/*    */   public UnsignedIntegerOneByte(long value) throws NumberFormatException {
/* 24 */     super(value);
/*    */   }
/*    */   
/*    */   public UnsignedIntegerOneByte(String s) throws NumberFormatException {
/* 28 */     super(s);
/*    */   }
/*    */   
/*    */   public UnsignedVariableInteger.Bits getBits() {
/* 32 */     return UnsignedVariableInteger.Bits.EIGHT;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\UnsignedIntegerOneByte.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */