/*    */ package org.fourthline.cling.model.message.header;
/*    */ 
/*    */ import org.seamless.util.io.HexBin;
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
/*    */ public class InterfaceMacHeader
/*    */   extends UpnpHeader<byte[]>
/*    */ {
/*    */   public InterfaceMacHeader() {}
/*    */   
/*    */   public InterfaceMacHeader(byte[] value) {
/* 31 */     setValue(value);
/*    */   }
/*    */   
/*    */   public InterfaceMacHeader(String s) {
/* 35 */     setString(s);
/*    */   }
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 39 */     byte[] bytes = HexBin.stringToBytes(s, ":");
/* 40 */     setValue(bytes);
/* 41 */     if (bytes.length != 6) {
/* 42 */       throw new InvalidHeaderException("Invalid MAC address: " + s);
/*    */     }
/*    */   }
/*    */   
/*    */   public String getString() {
/* 47 */     return HexBin.bytesToString(getValue(), ":");
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 52 */     return "(" + getClass().getSimpleName() + ") '" + getString() + "'";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\InterfaceMacHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */