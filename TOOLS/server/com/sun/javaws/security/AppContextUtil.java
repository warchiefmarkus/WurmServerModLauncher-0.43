/*    */ package com.sun.javaws.security;
/*    */ 
/*    */ import sun.awt.AppContext;
/*    */ import sun.awt.SunToolkit;
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
/*    */ public class AppContextUtil
/*    */ {
/* 19 */   private static AppContext _mainAppContext = null;
/* 20 */   private static AppContext _securityAppContext = null;
/*    */ 
/*    */   
/*    */   public static void createSecurityAppContext() {
/* 24 */     if (_mainAppContext == null) {
/* 25 */       _mainAppContext = AppContext.getAppContext();
/*    */     }
/*    */     
/* 28 */     if (_securityAppContext == null) {
/* 29 */       SunToolkit.createNewAppContext();
/* 30 */       _securityAppContext = AppContext.getAppContext();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean isSecurityAppContext() {
/* 36 */     return (AppContext.getAppContext() == _securityAppContext);
/*    */   }
/*    */   
/*    */   public static boolean isApplicationAppContext() {
/* 40 */     return (AppContext.getAppContext() == _mainAppContext);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\security\AppContextUtil.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */