/*    */ package org.fourthline.cling.model.message.header;
/*    */ 
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
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
/*    */ public class LocationHeader
/*    */   extends UpnpHeader<URL>
/*    */ {
/*    */   public LocationHeader() {}
/*    */   
/*    */   public LocationHeader(URL value) {
/* 34 */     setValue(value);
/*    */   }
/*    */   
/*    */   public LocationHeader(String s) {
/* 38 */     setString(s);
/*    */   }
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/*    */     try {
/* 43 */       URL url = new URL(s);
/* 44 */       setValue(url);
/* 45 */     } catch (MalformedURLException ex) {
/* 46 */       throw new InvalidHeaderException("Invalid URI: " + ex.getMessage());
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getString() {
/* 51 */     return getValue().toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\LocationHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */