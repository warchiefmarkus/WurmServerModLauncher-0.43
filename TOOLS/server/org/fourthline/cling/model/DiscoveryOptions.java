/*    */ package org.fourthline.cling.model;
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
/*    */ 
/*    */ 
/*    */ public class DiscoveryOptions
/*    */ {
/*    */   protected boolean advertised;
/*    */   protected boolean byeByeBeforeFirstAlive;
/*    */   
/*    */   public DiscoveryOptions(boolean advertised) {
/* 33 */     this.advertised = advertised;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DiscoveryOptions(boolean advertised, boolean byeByeBeforeFirstAlive) {
/* 44 */     this.advertised = advertised;
/* 45 */     this.byeByeBeforeFirstAlive = byeByeBeforeFirstAlive;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isAdvertised() {
/* 53 */     return this.advertised;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isByeByeBeforeFirstAlive() {
/* 61 */     return this.byeByeBeforeFirstAlive;
/*    */   }
/*    */ 
/*    */   
/* 65 */   private static String simpleName = DiscoveryOptions.class.getSimpleName();
/*    */   
/*    */   public String toString() {
/* 68 */     return "(" + simpleName + ")" + " advertised: " + isAdvertised() + " byebyeBeforeFirstAlive: " + isByeByeBeforeFirstAlive();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\DiscoveryOptions.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */