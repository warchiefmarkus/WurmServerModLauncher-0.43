/*    */ package org.fourthline.cling.support.model;
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
/*    */ public enum RecordMediumWriteStatus
/*    */ {
/* 23 */   WRITABLE,
/* 24 */   PROTECTED,
/* 25 */   NOT_WRITABLE,
/* 26 */   UNKNOWN,
/* 27 */   NOT_IMPLEMENTED;
/*    */   
/*    */   public static RecordMediumWriteStatus valueOrUnknownOf(String s) {
/* 30 */     if (s == null)
/* 31 */       return UNKNOWN; 
/*    */     try {
/* 33 */       return valueOf(s);
/* 34 */     } catch (IllegalArgumentException ex) {
/* 35 */       return UNKNOWN;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\RecordMediumWriteStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */