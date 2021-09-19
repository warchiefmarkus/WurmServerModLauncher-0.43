/*    */ package org.fourthline.cling.support.model.dlna.message.header;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.fourthline.cling.model.message.header.InvalidHeaderException;
/*    */ import org.fourthline.cling.model.types.PragmaType;
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
/*    */ public class PragmaHeader
/*    */   extends DLNAHeader<List<PragmaType>>
/*    */ {
/*    */   public PragmaHeader() {
/* 32 */     setValue(new ArrayList());
/*    */   }
/*    */ 
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 37 */     if (s.length() != 0) {
/* 38 */       if (s.endsWith(";")) {
/* 39 */         s = s.substring(0, s.length() - 1);
/*    */       }
/* 41 */       String[] list = s.split("\\s*;\\s*");
/* 42 */       List<PragmaType> value = new ArrayList<>();
/* 43 */       for (String pragma : list) {
/* 44 */         value.add(PragmaType.valueOf(pragma));
/*    */       }
/*    */       return;
/*    */     } 
/* 48 */     throw new InvalidHeaderException("Invalid Pragma header value: " + s);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString() {
/* 53 */     List<PragmaType> v = (List<PragmaType>)getValue();
/* 54 */     String r = "";
/* 55 */     for (PragmaType pragma : v) {
/* 56 */       r = r + ((r.length() == 0) ? "" : ",") + pragma.getString();
/*    */     }
/* 58 */     return r;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\message\header\PragmaHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */