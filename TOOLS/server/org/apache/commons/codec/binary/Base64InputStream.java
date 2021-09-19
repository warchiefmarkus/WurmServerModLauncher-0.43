/*    */ package org.apache.commons.codec.binary;
/*    */ 
/*    */ import java.io.InputStream;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Base64InputStream
/*    */   extends BaseNCodecInputStream
/*    */ {
/*    */   public Base64InputStream(InputStream in) {
/* 53 */     this(in, false);
/*    */   }
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
/*    */   public Base64InputStream(InputStream in, boolean doEncode) {
/* 66 */     super(in, new Base64(false), doEncode);
/*    */   }
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
/*    */   public Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator) {
/* 86 */     super(in, new Base64(lineLength, lineSeparator), doEncode);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\commons\codec\binary\Base64InputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */