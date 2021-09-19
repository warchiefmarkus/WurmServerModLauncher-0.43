/*    */ package org.fourthline.cling.support.model.dlna.message.header;
/*    */ 
/*    */ import java.util.regex.Pattern;
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
/*    */ public class WCTHeader
/*    */   extends DLNAHeader<Boolean>
/*    */ {
/* 25 */   static final Pattern pattern = Pattern.compile("^[01]{1}$", 2);
/*    */   
/*    */   public WCTHeader() {
/* 28 */     setValue(Boolean.valueOf(false));
/*    */   }
/*    */ 
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 33 */     if (pattern.matcher(s).matches()) {
/* 34 */       setValue(Boolean.valueOf(s.equals("1")));
/*    */       return;
/*    */     } 
/* 37 */     throw new InvalidHeaderException("Invalid SCID header value: " + s);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString() {
/* 42 */     return ((Boolean)getValue()).booleanValue() ? "1" : "0";
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\message\header\WCTHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */