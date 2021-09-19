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
/*    */ 
/*    */ 
/*    */ public class IntegerDatatype
/*    */   extends AbstractDatatype<Integer>
/*    */ {
/*    */   private int byteSize;
/*    */   
/*    */   public IntegerDatatype(int byteSize) {
/* 28 */     this.byteSize = byteSize;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isHandlingJavaType(Class<int> type) {
/* 33 */     return (type == int.class || Integer.class.isAssignableFrom(type));
/*    */   }
/*    */   
/*    */   public Integer valueOf(String s) throws InvalidValueException {
/* 37 */     if (s.equals("")) return null; 
/*    */     try {
/* 39 */       Integer value = Integer.valueOf(Integer.parseInt(s.trim()));
/* 40 */       if (!isValid(value)) {
/* 41 */         throw new InvalidValueException("Not a " + getByteSize() + " byte(s) integer: " + s);
/*    */       }
/*    */       
/* 44 */       return value;
/* 45 */     } catch (NumberFormatException ex) {
/*    */ 
/*    */ 
/*    */       
/* 49 */       if (s.equals("NOT_IMPLEMENTED")) {
/* 50 */         return Integer.valueOf(getMaxValue());
/*    */       }
/* 52 */       throw new InvalidValueException("Can't convert string to number: " + s, ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValid(Integer value) {
/* 58 */     return (value == null || (value.intValue() >= getMinValue() && value.intValue() <= getMaxValue()));
/*    */   }
/*    */   
/*    */   public int getMinValue() {
/* 62 */     switch (getByteSize()) {
/*    */       case 1:
/* 64 */         return -128;
/*    */       case 2:
/* 66 */         return -32768;
/*    */       case 4:
/* 68 */         return Integer.MIN_VALUE;
/*    */     } 
/* 70 */     throw new IllegalArgumentException("Invalid integer byte size: " + getByteSize());
/*    */   }
/*    */   
/*    */   public int getMaxValue() {
/* 74 */     switch (getByteSize()) {
/*    */       case 1:
/* 76 */         return 127;
/*    */       case 2:
/* 78 */         return 32767;
/*    */       case 4:
/* 80 */         return Integer.MAX_VALUE;
/*    */     } 
/* 82 */     throw new IllegalArgumentException("Invalid integer byte size: " + getByteSize());
/*    */   }
/*    */   
/*    */   public int getByteSize() {
/* 86 */     return this.byteSize;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\IntegerDatatype.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */