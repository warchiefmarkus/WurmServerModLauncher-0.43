/*    */ package org.fourthline.cling.support.model.dlna.message.header;
/*    */ 
/*    */ import org.fourthline.cling.model.message.header.InvalidHeaderException;
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
/*    */ public class SupportedHeader
/*    */   extends DLNAHeader<String[]>
/*    */ {
/*    */   public SupportedHeader() {
/* 25 */     setValue(new String[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 30 */     if (s.length() != 0) {
/* 31 */       if (s.endsWith(";"))
/* 32 */         s = s.substring(0, s.length() - 1); 
/* 33 */       setValue(s.split("\\s*,\\s*"));
/*    */       return;
/*    */     } 
/* 36 */     throw new InvalidHeaderException("Invalid Supported header value: " + s);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString() {
/* 41 */     String[] v = (String[])getValue();
/* 42 */     String r = (v.length > 0) ? v[0] : "";
/* 43 */     for (int i = 1; i < v.length; i++) {
/* 44 */       r = r + "," + v[i];
/*    */     }
/* 46 */     return r;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\message\header\SupportedHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */