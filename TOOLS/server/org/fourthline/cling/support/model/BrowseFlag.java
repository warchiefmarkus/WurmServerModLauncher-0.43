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
/*    */ 
/*    */ public enum BrowseFlag
/*    */ {
/* 24 */   METADATA("BrowseMetadata"),
/* 25 */   DIRECT_CHILDREN("BrowseDirectChildren");
/*    */   
/*    */   private String protocolString;
/*    */   
/*    */   BrowseFlag(String protocolString) {
/* 30 */     this.protocolString = protocolString;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 35 */     return this.protocolString;
/*    */   }
/*    */   
/*    */   public static BrowseFlag valueOrNullOf(String s) {
/* 39 */     for (BrowseFlag browseFlag : values()) {
/* 40 */       if (browseFlag.toString().equals(s))
/* 41 */         return browseFlag; 
/*    */     } 
/* 43 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\BrowseFlag.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */