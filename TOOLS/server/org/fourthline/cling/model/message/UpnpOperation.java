/*    */ package org.fourthline.cling.model.message;
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
/*    */ public abstract class UpnpOperation
/*    */ {
/* 25 */   private int httpMinorVersion = 1;
/*    */   
/*    */   public int getHttpMinorVersion() {
/* 28 */     return this.httpMinorVersion;
/*    */   }
/*    */   
/*    */   public void setHttpMinorVersion(int httpMinorVersion) {
/* 32 */     this.httpMinorVersion = httpMinorVersion;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\UpnpOperation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */