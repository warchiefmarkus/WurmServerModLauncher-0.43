/*    */ package com.sun.javaws;
/*    */ 
/*    */ import java.awt.Window;
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
/*    */ public abstract class ExtensionInstallHandler
/*    */ {
/*    */   private static ExtensionInstallHandler _installHandler;
/*    */   
/*    */   public static synchronized ExtensionInstallHandler getInstance() {
/* 34 */     if (_installHandler == null) {
/* 35 */       _installHandler = ExtensionInstallHandlerFactory.newInstance();
/*    */     }
/* 37 */     return _installHandler;
/*    */   }
/*    */   
/*    */   public abstract boolean doPreRebootActions(Window paramWindow);
/*    */   
/*    */   public abstract boolean doReboot();
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\ExtensionInstallHandler.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */