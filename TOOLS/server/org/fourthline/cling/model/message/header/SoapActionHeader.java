/*    */ package org.fourthline.cling.model.message.header;
/*    */ 
/*    */ import java.net.URI;
/*    */ import org.fourthline.cling.model.types.SoapActionType;
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
/*    */ public class SoapActionHeader
/*    */   extends UpnpHeader<SoapActionType>
/*    */ {
/*    */   public SoapActionHeader() {}
/*    */   
/*    */   public SoapActionHeader(URI uri) {
/* 31 */     setValue(SoapActionType.valueOf(uri.toString()));
/*    */   }
/*    */   
/*    */   public SoapActionHeader(SoapActionType value) {
/* 35 */     setValue(value);
/*    */   }
/*    */   
/*    */   public SoapActionHeader(String s) throws InvalidHeaderException {
/* 39 */     setString(s);
/*    */   }
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/*    */     try {
/* 44 */       if (!s.startsWith("\"") && s.endsWith("\"")) {
/* 45 */         throw new InvalidHeaderException("Invalid SOAP action header, must be enclosed in doublequotes:" + s);
/*    */       }
/*    */       
/* 48 */       SoapActionType t = SoapActionType.valueOf(s.substring(1, s.length() - 1));
/* 49 */       setValue(t);
/* 50 */     } catch (RuntimeException ex) {
/* 51 */       throw new InvalidHeaderException("Invalid SOAP action header value, " + ex.getMessage());
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getString() {
/* 56 */     return "\"" + getValue().toString() + "\"";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\SoapActionHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */