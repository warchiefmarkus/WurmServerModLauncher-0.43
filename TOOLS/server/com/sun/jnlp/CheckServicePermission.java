/*    */ package com.sun.jnlp;
/*    */ 
/*    */ import java.awt.AWTPermission;
/*    */ import java.io.FilePermission;
/*    */ import java.security.AccessControlException;
/*    */ import java.security.AccessController;
/*    */ import java.security.Permission;
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
/*    */ public final class CheckServicePermission
/*    */ {
/*    */   private static boolean checkPermission(Permission paramPermission) {
/*    */     try {
/* 29 */       AccessController.checkPermission(paramPermission);
/* 30 */       return true;
/* 31 */     } catch (AccessControlException accessControlException) {
/* 32 */       return false;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   static boolean hasFileAccessPermissions() {
/* 38 */     return checkPermission(new FilePermission("*", "read,write"));
/*    */   }
/*    */ 
/*    */   
/*    */   static boolean hasPrintAccessPermissions() {
/* 43 */     return checkPermission(new RuntimePermission("queuePrintJob"));
/*    */   }
/*    */ 
/*    */   
/*    */   static boolean hasClipboardPermissions() {
/* 48 */     return checkPermission(new AWTPermission("accessClipboard"));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\jnlp\CheckServicePermission.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */