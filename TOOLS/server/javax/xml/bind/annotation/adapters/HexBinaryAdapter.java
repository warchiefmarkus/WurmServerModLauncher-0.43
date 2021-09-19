/*    */ package javax.xml.bind.annotation.adapters;
/*    */ 
/*    */ import javax.xml.bind.DatatypeConverter;
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
/*    */ public final class HexBinaryAdapter
/*    */   extends XmlAdapter<String, byte[]>
/*    */ {
/*    */   public byte[] unmarshal(String s) {
/* 21 */     if (s == null) return null; 
/* 22 */     return DatatypeConverter.parseHexBinary(s);
/*    */   }
/*    */   
/*    */   public String marshal(byte[] bytes) {
/* 26 */     if (bytes == null) return null; 
/* 27 */     return DatatypeConverter.printHexBinary(bytes);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\annotation\adapters\HexBinaryAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */