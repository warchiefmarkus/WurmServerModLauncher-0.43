/*     */ package com.sun.javaws.ui;
/*     */ 
/*     */ import com.sun.deploy.config.Config;
/*     */ import com.sun.deploy.resources.ResourceManager;
/*     */ import com.sun.deploy.util.DeployUIManager;
/*     */ import com.sun.deploy.util.DialogFactory;
/*     */ import com.sun.javaws.Main;
/*     */ import com.sun.javaws.SplashScreen;
/*     */ import com.sun.javaws.jnl.LaunchDesc;
/*     */ import com.sun.javaws.jnl.ShortcutDesc;
/*     */ import java.awt.Component;
/*     */ import java.awt.Frame;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.LookAndFeel;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DesktopIntegration
/*     */   extends JOptionPane
/*     */ {
/*  25 */   private int _answer = 2;
/*     */ 
/*     */   
/*     */   public DesktopIntegration(Frame paramFrame, String paramString, boolean paramBoolean1, boolean paramBoolean2) {
/*  29 */     initComponents(paramString, paramBoolean1, paramBoolean2);
/*     */   }
/*     */   
/*     */   private void initComponents(String paramString, boolean paramBoolean1, boolean paramBoolean2) {
/*  33 */     Object[] arrayOfObject = new Object[2];
/*  34 */     JButton[] arrayOfJButton = new JButton[3];
/*     */     
/*  36 */     arrayOfJButton[0] = new JButton(ResourceManager.getString("install.yesButton"));
/*     */     
/*  38 */     arrayOfJButton[0].setMnemonic(ResourceManager.getVKCode("install.yesMnemonic"));
/*     */     
/*  40 */     JButton jButton1 = arrayOfJButton[0];
/*     */     
/*  42 */     arrayOfJButton[1] = new JButton(ResourceManager.getString("install.noButton"));
/*     */     
/*  44 */     arrayOfJButton[1].setMnemonic(ResourceManager.getVKCode("install.noMnemonic"));
/*     */     
/*  46 */     JButton jButton2 = arrayOfJButton[1];
/*     */     
/*  48 */     arrayOfJButton[2] = new JButton(ResourceManager.getString("install.configButton"));
/*     */     
/*  50 */     arrayOfJButton[2].setMnemonic(ResourceManager.getVKCode("install.configMnemonic"));
/*     */     
/*  52 */     JButton jButton3 = arrayOfJButton[2];
/*     */ 
/*     */     
/*  55 */     for (byte b = 0; b < 3; b++) {
/*  56 */       arrayOfJButton[b].addActionListener(new ActionListener(this, jButton3, jButton1, jButton2) { private final JButton val$configureButton;
/*     */             public void actionPerformed(ActionEvent param1ActionEvent) {
/*  58 */               JButton jButton = (JButton)param1ActionEvent.getSource();
/*  59 */               if (jButton == this.val$configureButton) {
/*  60 */                 Main.launchJavaControlPanel("advanced");
/*     */                 return;
/*     */               } 
/*  63 */               if (jButton == this.val$yesButton) { this.this$0._answer = 1; }
/*  64 */               else if (jButton == this.val$noButton) { this.this$0._answer = 0; }
/*  65 */                Component component = (Component)param1ActionEvent.getSource();
/*  66 */               JDialog jDialog = null;
/*     */ 
/*     */               
/*  69 */               while (component.getParent() != null) {
/*  70 */                 if (component instanceof JDialog) {
/*  71 */                   jDialog = (JDialog)component;
/*     */                 }
/*  73 */                 component = component.getParent();
/*     */               } 
/*     */               
/*  76 */               if (jDialog != null)
/*  77 */                 jDialog.setVisible(false); 
/*     */             }
/*     */             private final JButton val$yesButton; private final JButton val$noButton;
/*     */             private final DesktopIntegration this$0; }
/*     */         );
/*     */     } 
/*  83 */     String str = null;
/*     */     
/*  85 */     if (Config.getOSName().equalsIgnoreCase("Windows")) {
/*     */       
/*  87 */       if (paramBoolean1 && paramBoolean2) {
/*  88 */         str = ResourceManager.getString("install.windows.both.message", paramString);
/*  89 */       } else if (paramBoolean1) {
/*  90 */         str = ResourceManager.getString("install.desktop.message", paramString);
/*  91 */       } else if (paramBoolean2) {
/*  92 */         str = ResourceManager.getString("install.windows.menu.message", paramString);
/*     */       }
/*     */     
/*     */     }
/*  96 */     else if (paramBoolean1 && paramBoolean2) {
/*  97 */       str = ResourceManager.getString("install.gnome.both.message", paramString);
/*  98 */     } else if (paramBoolean1) {
/*  99 */       str = ResourceManager.getString("install.desktop.message", paramString);
/* 100 */     } else if (paramBoolean2) {
/* 101 */       str = ResourceManager.getString("install.gnome.menu.message", paramString);
/*     */     } 
/*     */ 
/*     */     
/* 105 */     setOptions((Object[])arrayOfJButton);
/* 106 */     setMessage(str);
/* 107 */     setMessageType(2);
/* 108 */     setInitialValue(arrayOfJButton[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int showDTIDialog(Frame paramFrame, LaunchDesc paramLaunchDesc) {
/* 113 */     String str = paramLaunchDesc.getInformation().getTitle();
/* 114 */     ShortcutDesc shortcutDesc = paramLaunchDesc.getInformation().getShortcut();
/*     */     
/* 116 */     boolean bool1 = (shortcutDesc == null) ? true : shortcutDesc.getDesktop();
/* 117 */     boolean bool2 = (shortcutDesc == null) ? true : shortcutDesc.getMenu();
/*     */     
/* 119 */     LookAndFeel lookAndFeel = DeployUIManager.setLookAndFeel();
/*     */     
/* 121 */     DesktopIntegration desktopIntegration = new DesktopIntegration(paramFrame, str, bool1, bool2);
/* 122 */     JDialog jDialog = desktopIntegration.createDialog(paramFrame, ResourceManager.getString("install.title", str));
/* 123 */     DialogFactory.positionDialog(jDialog);
/* 124 */     SplashScreen.hide();
/* 125 */     jDialog.setVisible(true);
/*     */     
/* 127 */     DeployUIManager.restoreLookAndFeel(lookAndFeel);
/*     */     
/* 129 */     return desktopIntegration._answer;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaw\\ui\DesktopIntegration.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */