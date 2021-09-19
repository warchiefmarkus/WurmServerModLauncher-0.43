/*    */ package com.wurmonline.website;
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
/*    */ public class LoginInfo
/*    */ {
/*    */   private String name;
/*    */   
/*    */   public LoginInfo(String aName) {
/* 24 */     this.name = aName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 33 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isAdmin() {
/* 38 */     return this.name.equals("admin");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\website\LoginInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */