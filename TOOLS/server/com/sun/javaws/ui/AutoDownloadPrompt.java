/*    */ package com.sun.javaws.ui;
/*    */ 
/*    */ import com.sun.deploy.resources.ResourceManager;
/*    */ import com.sun.deploy.util.DialogFactory;
/*    */ import com.sun.javaws.jnl.LaunchDesc;
/*    */ import java.awt.Component;
/*    */ import javax.swing.JButton;
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
/*    */ public class AutoDownloadPrompt
/*    */ {
/* 25 */   public static int _result = -1;
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
/*    */   public static boolean prompt(Component paramComponent, LaunchDesc paramLaunchDesc) {
/* 40 */     if (_result >= 0) return (_result == 0);
/*    */     
/* 42 */     String str1 = paramLaunchDesc.getInformation().getTitle();
/* 43 */     String str2 = paramLaunchDesc.getResources().getSelectedJRE().getVersion();
/* 44 */     String str3 = ResourceManager.getString("download.jre.prompt.title");
/*    */     
/* 46 */     String[] arrayOfString = { ResourceManager.getString("download.jre.prompt.text1", str1, str2), "", ResourceManager.getString("download.jre.prompt.text2") };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 54 */     JButton[] arrayOfJButton = { new JButton(ResourceManager.getString("download.jre.prompt.okButton")), new JButton(ResourceManager.getString("download.jre.prompt.cancelButton")) };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 61 */     arrayOfJButton[0].setMnemonic(ResourceManager.getAcceleratorKey("download.jre.prompt.okButton"));
/*    */     
/* 63 */     arrayOfJButton[1].setMnemonic(ResourceManager.getAcceleratorKey("download.jre.prompt.cancelButton"));
/*    */ 
/*    */     
/* 66 */     _result = DialogFactory.showOptionDialog(paramComponent, 4, arrayOfString, str3, (Object[])arrayOfJButton, arrayOfJButton[0]);
/*    */ 
/*    */ 
/*    */     
/* 70 */     return (_result == 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaw\\ui\AutoDownloadPrompt.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */