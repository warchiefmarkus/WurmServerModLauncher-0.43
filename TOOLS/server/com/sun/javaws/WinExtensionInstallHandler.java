/*    */ package com.sun.javaws;
/*    */ 
/*    */ import com.sun.deploy.resources.ResourceManager;
/*    */ import com.sun.deploy.util.DialogFactory;
/*    */ import com.sun.deploy.util.WinRegistry;
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
/*    */ public class WinExtensionInstallHandler
/*    */   extends ExtensionInstallHandler
/*    */ {
/*    */   private static final String KEY_RUNONCE = "Software\\Microsoft\\Windows\\CurrentVersion\\RunOnce";
/*    */   
/*    */   static {
/* 37 */     NativeLibrary.getInstance().load();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean doPreRebootActions(Window paramWindow) {
/* 42 */     int[] arrayOfInt = { 1 };
/* 43 */     paramWindow.setVisible(true);
/* 44 */     paramWindow.requestFocus();
/* 45 */     arrayOfInt[0] = DialogFactory.showConfirmDialog(paramWindow, ResourceManager.getString("extensionInstall.rebootMessage"), ResourceManager.getString("extensionInstall.rebootTitle"));
/*    */ 
/*    */     
/* 48 */     paramWindow.setVisible(false);
/*    */     
/* 50 */     return (arrayOfInt[0] == 0);
/*    */   }
/*    */   
/*    */   public boolean doReboot() {
/* 54 */     return WinRegistry.doReboot();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\WinExtensionInstallHandler.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */