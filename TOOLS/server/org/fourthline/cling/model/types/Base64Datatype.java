/*    */ package org.fourthline.cling.model.types;
/*    */ 
/*    */ import org.seamless.util.io.Base64Coder;
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
/*    */ public class Base64Datatype
/*    */   extends AbstractDatatype<byte[]>
/*    */ {
/*    */   public Class<byte[]> getValueType() {
/* 29 */     return byte[].class;
/*    */   }
/*    */   
/*    */   public byte[] valueOf(String s) throws InvalidValueException {
/* 33 */     if (s.equals("")) return null; 
/*    */     try {
/* 35 */       return Base64Coder.decode(s);
/* 36 */     } catch (Exception ex) {
/* 37 */       throw new InvalidValueException(ex.getMessage(), ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString(byte[] value) throws InvalidValueException {
/* 43 */     if (value == null) return ""; 
/*    */     try {
/* 45 */       return new String(Base64Coder.encode(value), "UTF-8");
/* 46 */     } catch (Exception ex) {
/* 47 */       throw new InvalidValueException(ex.getMessage(), ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\types\Base64Datatype.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */