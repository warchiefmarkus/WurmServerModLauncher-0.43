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
/*    */ public class TransferModeHeader
/*    */   extends DLNAHeader<TransferModeHeader.Type>
/*    */ {
/*    */   public enum Type
/*    */   {
/* 25 */     Streaming,
/* 26 */     Interactive,
/* 27 */     Background;
/*    */   }
/*    */   
/*    */   public TransferModeHeader() {
/* 31 */     setValue(Type.Interactive);
/*    */   }
/*    */   
/*    */   public TransferModeHeader(Type mode) {
/* 35 */     setValue(mode);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 40 */     if (s.length() != 0) {
/*    */       try {
/* 42 */         setValue(Type.valueOf(s));
/*    */         return;
/* 44 */       } catch (Exception exception) {}
/*    */     }
/* 46 */     throw new InvalidHeaderException("Invalid TransferMode header value: " + s);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString() {
/* 51 */     return ((Type)getValue()).toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\message\header\TransferModeHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */