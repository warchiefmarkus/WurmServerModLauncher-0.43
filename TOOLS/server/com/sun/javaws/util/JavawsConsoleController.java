/*     */ package com.sun.javaws.util;
/*     */ 
/*     */ import com.sun.deploy.config.Config;
/*     */ import com.sun.deploy.net.proxy.DynamicProxyManager;
/*     */ import com.sun.deploy.resources.ResourceManager;
/*     */ import com.sun.deploy.util.ConsoleController;
/*     */ import com.sun.deploy.util.ConsoleWindow;
/*     */ import com.sun.javaws.Globals;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.SwingUtilities;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavawsConsoleController
/*     */   implements ConsoleController
/*     */ {
/*  25 */   private static ConsoleWindow console = null;
/*  26 */   private static JavawsConsoleController jcc = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JavawsConsoleController getInstance() {
/*  32 */     if (jcc == null) {
/*  33 */       if (Globals.isJavaVersionAtLeast14()) {
/*  34 */         jcc = new JavawsConsoleController14();
/*     */       } else {
/*  36 */         jcc = new JavawsConsoleController();
/*     */       } 
/*     */     }
/*  39 */     return jcc;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLogger(Logger paramLogger) {}
/*     */ 
/*     */   
/*     */   public void setConsole(ConsoleWindow paramConsoleWindow) {
/*  47 */     if (console == null) {
/*  48 */       console = paramConsoleWindow;
/*     */     }
/*     */   }
/*     */   
/*     */   public ConsoleWindow getConsole() {
/*  53 */     return console;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIconifiedOnClose() {
/*  60 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDoubleBuffered() {
/*  67 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDumpStackSupported() {
/*  74 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String dumpAllStacks() {
/*  83 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreadGroup getMainThreadGroup() {
/*  90 */     return Thread.currentThread().getThreadGroup();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSecurityPolicyReloadSupported() {
/*  97 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reloadSecurityPolicy() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isProxyConfigReloadSupported() {
/* 112 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reloadProxyConfig() {
/* 120 */     DynamicProxyManager.reset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDumpClassLoaderSupported() {
/* 127 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String dumpClassLoaders() {
/* 136 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClearClassLoaderSupported() {
/* 143 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearClassLoaders() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLoggingSupported() {
/* 157 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean toggleLogging() {
/* 166 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isJCovSupported() {
/* 173 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean dumpJCovData() {
/* 182 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getProductName() {
/* 189 */     return ResourceManager.getString("product.javaws.name", "1.5.0_04");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void invokeLater(Runnable paramRunnable) {
/* 196 */     SwingUtilities.invokeLater(paramRunnable);
/*     */   }
/*     */   
/*     */   public static void showConsoleIfEnable() {
/* 200 */     if (Config.getProperty("deployment.console.startup.mode").equals("SHOW"))
/*     */     {
/* 202 */       console.showConsole(true);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaw\\util\JavawsConsoleController.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */