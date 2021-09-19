/*     */ package com.sun.jnlp;
/*     */ 
/*     */ import com.sun.deploy.config.Config;
/*     */ import com.sun.deploy.config.JREInfo;
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.deploy.util.TraceLevel;
/*     */ import com.sun.javaws.LaunchErrorDialog;
/*     */ import com.sun.javaws.LaunchSelection;
/*     */ import com.sun.javaws.LocalApplicationProperties;
/*     */ import com.sun.javaws.Main;
/*     */ import com.sun.javaws.ui.DownloadWindow;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Date;
/*     */ import javax.jnlp.ExtensionInstallerService;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ExtensionInstallerServiceImpl
/*     */   implements ExtensionInstallerService
/*     */ {
/*     */   private LocalApplicationProperties _lap;
/*     */   private DownloadWindow _window;
/*     */   private String _target;
/*     */   private String _installPath;
/*     */   private boolean _failedJREInstall = false;
/*  37 */   static ExtensionInstallerServiceImpl _sharedInstance = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ExtensionInstallerServiceImpl(String paramString, LocalApplicationProperties paramLocalApplicationProperties, DownloadWindow paramDownloadWindow) {
/*  43 */     this._lap = paramLocalApplicationProperties;
/*  44 */     this._window = paramDownloadWindow;
/*  45 */     this._installPath = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized ExtensionInstallerServiceImpl getInstance() {
/*  53 */     return _sharedInstance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized void initialize(String paramString, LocalApplicationProperties paramLocalApplicationProperties, DownloadWindow paramDownloadWindow) {
/*  61 */     if (_sharedInstance == null) {
/*  62 */       _sharedInstance = new ExtensionInstallerServiceImpl(paramString, paramLocalApplicationProperties, paramDownloadWindow);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getInstallPath() {
/*  67 */     return this._installPath;
/*     */   }
/*     */   
/*  70 */   public String getExtensionVersion() { return this._lap.getVersionId(); } public URL getExtensionLocation() {
/*  71 */     return this._lap.getLocation();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getInstalledJRE(URL paramURL, String paramString) {
/*  76 */     JREInfo jREInfo = LaunchSelection.selectJRE(paramURL, paramString);
/*  77 */     return (jREInfo != null) ? jREInfo.getPath() : null;
/*     */   }
/*     */   
/*     */   public void setHeading(String paramString) {
/*  81 */     this._window.setStatus(paramString);
/*  82 */   } public void setStatus(String paramString) { this._window.setProgressText(paramString); }
/*  83 */   public void updateProgress(int paramInt) { this._window.setProgressBarValue(paramInt); }
/*  84 */   public void hideProgressBar() { this._window.setProgressBarVisible(false); } public void hideStatusWindow() {
/*  85 */     this._window.getFrame().setVisible(false);
/*     */   }
/*     */   
/*     */   public void setJREInfo(String paramString1, String paramString2) {
/*  89 */     int i = JNLPClassLoader.getInstance().getDefaultSecurityModel();
/*  90 */     if (i != 1 && i != 2)
/*     */     {
/*  92 */       throw new SecurityException("Unsigned extension installer attempting to call setJREInfo.");
/*     */     }
/*     */ 
/*     */     
/*  96 */     Trace.println("setJREInfo: " + paramString2, TraceLevel.EXTENSIONS);
/*     */     
/*  98 */     if (paramString2 != null && (new File(paramString2)).exists()) {
/*     */       
/* 100 */       JREInfo.addJRE(new JREInfo(paramString1, getExtensionVersion(), getExtensionLocation().toString(), paramString2, Config.getOSName(), Config.getOSArch(), true, false));
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 106 */       Trace.println("jre install failed: jrePath invalid", TraceLevel.EXTENSIONS);
/*     */       
/* 108 */       this._failedJREInstall = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNativeLibraryInfo(String paramString) {
/* 114 */     Trace.println("setNativeLibInfo: " + paramString, TraceLevel.EXTENSIONS);
/*     */     
/* 116 */     this._lap.setNativeLibDirectory(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void installFailed() {
/* 123 */     Trace.println("installFailed", TraceLevel.EXTENSIONS);
/*     */ 
/*     */     
/* 126 */     Main.systemExit(1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void installSucceeded(boolean paramBoolean) {
/* 132 */     if (this._failedJREInstall) {
/*     */       return;
/*     */     }
/* 135 */     Trace.println("installSucceded", TraceLevel.EXTENSIONS);
/*     */     
/* 137 */     AccessController.doPrivileged(new PrivilegedAction(this) { private final ExtensionInstallerServiceImpl this$0;
/*     */           public Object run() {
/* 139 */             Config.store();
/* 140 */             return null;
/*     */           } }
/*     */       );
/*     */     
/* 144 */     this._lap.setInstallDirectory(this._installPath);
/* 145 */     this._lap.setLastAccessed(new Date());
/* 146 */     if (paramBoolean) {
/* 147 */       this._lap.setRebootNeeded(true);
/*     */     } else {
/* 149 */       this._lap.setLocallyInstalled(true);
/*     */     } 
/*     */     try {
/* 152 */       AccessController.doPrivileged(new PrivilegedExceptionAction(this) { private final ExtensionInstallerServiceImpl this$0;
/*     */             public Object run() throws IOException {
/* 154 */               this.this$0._lap.store();
/* 155 */               return null;
/*     */             } }
/*     */         );
/* 158 */     } catch (PrivilegedActionException privilegedActionException) {
/* 159 */       if (privilegedActionException.getException() instanceof IOException) {
/*     */         
/* 161 */         LaunchErrorDialog.show(this._window.getFrame(), privilegedActionException.getException(), false);
/*     */       } else {
/* 163 */         Trace.ignoredException(privilegedActionException.getException());
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 169 */     AccessController.doPrivileged(new PrivilegedAction(this) { private final ExtensionInstallerServiceImpl this$0;
/*     */           public Object run() {
/* 171 */             Main.systemExit(0);
/* 172 */             return null;
/*     */           } }
/*     */       );
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\jnlp\ExtensionInstallerServiceImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */