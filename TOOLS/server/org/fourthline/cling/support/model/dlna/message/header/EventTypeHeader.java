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
/*    */ public class EventTypeHeader
/*    */   extends DLNAHeader<String>
/*    */ {
/* 25 */   static final Pattern pattern = Pattern.compile("^[0-9]{4}$", 2);
/*    */   
/*    */   public EventTypeHeader() {
/* 28 */     setValue("0000");
/*    */   }
/*    */ 
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 33 */     if (pattern.matcher(s).matches()) {
/* 34 */       setValue(s);
/*    */       return;
/*    */     } 
/* 37 */     throw new InvalidHeaderException("Invalid EventType header value: " + s);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString() {
/* 42 */     return ((String)getValue()).toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\message\header\EventTypeHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */