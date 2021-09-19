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
/*    */ public final class UnsignedIntegerFourBytes
/*    */   extends UnsignedVariableInteger
/*    */ {
/*    */   public UnsignedIntegerFourBytes(long value) throws NumberFormatException {
/* 24 */     super(value);
/*    */   }
/*    */   
/*    */   public UnsignedIntegerFourBytes(String s) throws NumberFormatException {
/* 28 */     super(s);
/*    */   }
/*    */   
/*    */   public UnsignedVariableInteger.Bits getBits() {
/* 32 */     return UnsignedVariableInteger.Bits.THIRTYTWO;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\UnsignedIntegerFourBytes.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */