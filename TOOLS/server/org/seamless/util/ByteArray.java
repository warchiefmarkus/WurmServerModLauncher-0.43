/*    */ package org.seamless.util;
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
/*    */ public class ByteArray
/*    */ {
/*    */   public static byte[] toPrimitive(Byte[] array) {
/* 23 */     byte[] bytes = new byte[array.length];
/* 24 */     for (int i = 0; i < array.length; i++) {
/* 25 */       bytes[i] = array[i].byteValue();
/*    */     }
/* 27 */     return bytes;
/*    */   }
/*    */   
/*    */   public static Byte[] toWrapper(byte[] array) {
/* 31 */     Byte[] wrappers = new Byte[array.length];
/* 32 */     for (int i = 0; i < array.length; i++) {
/* 33 */       wrappers[i] = Byte.valueOf(array[i]);
/*    */     }
/* 35 */     return wrappers;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamles\\util\ByteArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */