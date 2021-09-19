/*    */ package com.sun.tools.xjc.api;
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
/*    */ public enum SpecVersion
/*    */ {
/* 45 */   V2_0, V2_1;
/*    */   
/*    */   public static final SpecVersion LATEST;
/*    */ 
/*    */   
/*    */   public boolean isLaterThan(SpecVersion t) {
/* 51 */     return (ordinal() >= t.ordinal());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static SpecVersion parse(String token) {
/* 60 */     if (token.equals("2.0")) {
/* 61 */       return V2_0;
/*    */     }
/* 63 */     if (token.equals("2.1"))
/* 64 */       return V2_1; 
/* 65 */     return null;
/*    */   }
/*    */   static {
/* 68 */     LATEST = V2_1;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\api\SpecVersion.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */