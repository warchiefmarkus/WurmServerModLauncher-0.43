/*      */ package com.sun.javaws;
/*      */ import com.sun.deploy.config.Config;
/*      */ import com.sun.deploy.config.JREInfo;
/*      */ import com.sun.deploy.resources.ResourceManager;
/*      */ import com.sun.deploy.si.SingleInstanceManager;
/*      */ import com.sun.deploy.util.DialogFactory;
/*      */ import com.sun.deploy.util.PerfLogger;
/*      */ import com.sun.deploy.util.Trace;
/*      */ import com.sun.deploy.util.TraceLevel;
/*      */ import com.sun.deploy.util.URLUtil;
/*      */ import com.sun.javaws.cache.Cache;
/*      */ import com.sun.javaws.cache.DiskCacheEntry;
/*      */ import com.sun.javaws.cache.DownloadProtocol;
/*      */ import com.sun.javaws.exceptions.ExitException;
/*      */ import com.sun.javaws.exceptions.FailedDownloadingResourceException;
/*      */ import com.sun.javaws.exceptions.JNLPException;
/*      */ import com.sun.javaws.exceptions.JreExecException;
/*      */ import com.sun.javaws.exceptions.LaunchDescException;
/*      */ import com.sun.javaws.exceptions.MissingFieldException;
/*      */ import com.sun.javaws.exceptions.NoLocalJREException;
/*      */ import com.sun.javaws.jnl.AppletDesc;
/*      */ import com.sun.javaws.jnl.AssociationDesc;
/*      */ import com.sun.javaws.jnl.JREDesc;
/*      */ import com.sun.javaws.jnl.LaunchDesc;
/*      */ import com.sun.javaws.jnl.LaunchDescFactory;
/*      */ import com.sun.javaws.security.AppPolicy;
/*      */ import com.sun.javaws.ui.AutoDownloadPrompt;
/*      */ import com.sun.javaws.ui.DownloadWindow;
/*      */ import com.sun.javaws.util.JavawsConsoleController;
/*      */ import com.sun.javaws.util.VersionID;
/*      */ import com.sun.javaws.util.VersionString;
/*      */ import com.sun.jnlp.AppletContainer;
/*      */ import com.sun.jnlp.AppletContainerCallback;
/*      */ import com.sun.jnlp.BasicServiceImpl;
/*      */ import com.sun.jnlp.JNLPClassLoader;
/*      */ import java.applet.Applet;
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Frame;
/*      */ import java.awt.event.WindowAdapter;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.event.WindowListener;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.net.Authenticator;
/*      */ import java.net.URL;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Date;
/*      */ import java.util.Properties;
/*      */ import javax.swing.JFrame;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.UnsupportedLookAndFeelException;
/*      */ 
/*      */ public class Launcher implements Runnable {
/*   61 */   private DownloadWindow _downloadWindow = null;
/*      */ 
/*      */ 
/*      */   
/*      */   private LaunchDesc _launchDesc;
/*      */ 
/*      */ 
/*      */   
/*      */   private String[] _args;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean _exit = true;
/*      */ 
/*      */ 
/*      */   
/*      */   private JAuthenticator _ja;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean _shownDownloadWindow;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void launch(String[] paramArrayOfString, boolean paramBoolean) {
/*   87 */     this._args = paramArrayOfString;
/*   88 */     this._exit = paramBoolean;
/*   89 */     (new Thread(Main.getLaunchThreadGroup(), this, "javawsApplicationMain")).start();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void removeTempJnlpFile(LaunchDesc paramLaunchDesc) {
/*   96 */     DiskCacheEntry diskCacheEntry = null;
/*      */     
/*      */     try {
/*   99 */       if (paramLaunchDesc.isApplicationDescriptor()) {
/*  100 */         diskCacheEntry = DownloadProtocol.getCachedLaunchedFile(paramLaunchDesc.getCanonicalHome());
/*      */       }
/*      */     }
/*  103 */     catch (JNLPException jNLPException) {
/*  104 */       Trace.ignoredException((Exception)jNLPException);
/*      */     } 
/*      */     
/*  107 */     if (diskCacheEntry == null)
/*      */       return; 
/*  109 */     File file = diskCacheEntry.getFile();
/*      */     
/*  111 */     if (this._args != null && file != null && JnlpxArgs.shouldRemoveArgumentFile()) {
/*      */ 
/*      */       
/*  114 */       (new File(this._args[0])).delete();
/*      */ 
/*      */       
/*  117 */       JnlpxArgs.setShouldRemoveArgumentFile(String.valueOf(false));
/*      */ 
/*      */       
/*  120 */       this._args[0] = file.getPath();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void run() {
/*  127 */     LaunchDesc launchDesc = this._launchDesc;
/*      */     
/*  129 */     boolean bool1 = LaunchDownload.updateLaunchDescInCache(launchDesc);
/*      */ 
/*      */     
/*  132 */     removeTempJnlpFile(launchDesc);
/*      */ 
/*      */     
/*  135 */     if (launchDesc.getResources() != null) {
/*  136 */       Globals.getDebugOptionsFromProperties(launchDesc.getResources().getResourceProperties());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  145 */     if (Config.getBooleanProperty("deployment.security.authenticator")) {
/*  146 */       this._ja = JAuthenticator.getInstance(this._downloadWindow.getFrame());
/*  147 */       Authenticator.setDefault((Authenticator)this._ja);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  155 */     byte b = 0;
/*  156 */     boolean bool2 = false;
/*  157 */     boolean bool3 = Globals.isSilentMode();
/*  158 */     boolean bool4 = (Globals.isImportMode() || launchDesc.getLaunchType() == 3) ? true : false;
/*      */     
/*      */     try {
/*      */       do {
/*  162 */         bool2 = (b == 3) ? true : false;
/*  163 */         this._downloadWindow.setLaunchDesc(launchDesc, true);
/*  164 */         launchDesc = handleLaunchFile(launchDesc, this._args, !bool2, bool4, bool3, bool1);
/*      */         
/*  166 */         b++;
/*  167 */       } while (launchDesc != null && !bool2);
/*  168 */     } catch (ExitException exitException) {
/*  169 */       boolean bool = (exitException.getReason() == 0) ? false : true;
/*  170 */       if (exitException.getReason() == 2) {
/*  171 */         LaunchErrorDialog.show((this._downloadWindow == null) ? null : this._downloadWindow.getFrame(), exitException.getException(), this._exit);
/*      */       }
/*      */ 
/*      */       
/*  175 */       if (this._exit) {
/*  176 */         Main.systemExit(bool);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private LaunchDesc handleLaunchFile(LaunchDesc paramLaunchDesc, String[] paramArrayOfString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) throws ExitException {
/*  185 */     VersionString versionString = new VersionString(paramLaunchDesc.getSpecVersion());
/*  186 */     VersionID versionID = new VersionID("1.5");
/*      */     
/*  188 */     if (!versionString.contains(new VersionID("1.5")))
/*      */     {
/*  190 */       if (!versionString.contains(new VersionID("1.0"))) {
/*  191 */         JNLPException.setDefaultLaunchDesc(paramLaunchDesc);
/*  192 */         handleJnlpFileException(paramLaunchDesc, (Exception)new LaunchDescException(paramLaunchDesc, ResourceManager.getString("launch.error.badjnlversion", paramLaunchDesc.getSpecVersion()), null));
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  199 */     if (paramLaunchDesc.getResources() == null) {
/*  200 */       handleJnlpFileException(paramLaunchDesc, (Exception)new LaunchDescException(paramLaunchDesc, ResourceManager.getString("launch.error.noappresources", paramLaunchDesc.getSpecVersion()), null));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  207 */     if (!paramBoolean2 && !paramLaunchDesc.isLibrary() && 
/*  208 */       !paramLaunchDesc.isJRESpecified()) {
/*  209 */       LaunchDescException launchDescException = new LaunchDescException(paramLaunchDesc, ResourceManager.getString("launch.error.missingjreversion"), null);
/*      */ 
/*      */       
/*  212 */       handleJnlpFileException(paramLaunchDesc, (Exception)launchDescException);
/*      */     } 
/*      */     
/*  215 */     boolean bool = (paramLaunchDesc.getLaunchType() == 4) ? true : false;
/*      */     
/*  217 */     return handleApplicationDesc(paramLaunchDesc, paramArrayOfString, bool, paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private LaunchDesc handleApplicationDesc(LaunchDesc paramLaunchDesc, String[] paramArrayOfString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5) throws ExitException {
/*  226 */     JNLPException.setDefaultLaunchDesc(paramLaunchDesc);
/*      */ 
/*      */     
/*  229 */     JFrame jFrame = this._downloadWindow.getFrame();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  235 */     URL uRL = paramLaunchDesc.getCanonicalHome();
/*  236 */     if (uRL == null) {
/*  237 */       LaunchDescException launchDescException = new LaunchDescException(paramLaunchDesc, ResourceManager.getString("launch.error.nomainjar"), null);
/*      */       
/*  239 */       throw new ExitException(launchDescException, 2);
/*      */     } 
/*  241 */     LocalApplicationProperties localApplicationProperties = null;
/*  242 */     if (paramBoolean1) {
/*      */ 
/*      */       
/*  245 */       localApplicationProperties = Cache.getLocalApplicationProperties(paramArrayOfString[0], paramLaunchDesc);
/*      */ 
/*      */       
/*  248 */       if (localApplicationProperties == null || !Globals.isInstallMode()) {
/*  249 */         handleJnlpFileException(paramLaunchDesc, (Exception)new MissingFieldException(paramLaunchDesc.getSource(), "<application-desc>|<applet-desc>"));
/*      */       }
/*      */ 
/*      */       
/*  253 */       uRL = localApplicationProperties.getLocation();
/*      */     } else {
/*  255 */       localApplicationProperties = Cache.getLocalApplicationProperties(uRL, paramLaunchDesc);
/*      */     } 
/*      */     
/*  258 */     Trace.println("LaunchDesc location: " + uRL + ", version: " + localApplicationProperties.getVersionId(), TraceLevel.BASIC);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  263 */     boolean bool = LaunchDownload.isInCache(paramLaunchDesc);
/*  264 */     boolean bool1 = (bool && Globals.isOffline()) ? true : false;
/*      */ 
/*      */ 
/*      */     
/*  268 */     JREInfo jREInfo = null;
/*  269 */     if (!paramBoolean3) {
/*  270 */       jREInfo = LaunchSelection.selectJRE(paramLaunchDesc);
/*      */       
/*  272 */       if (jREInfo == null) {
/*  273 */         String str = Config.getProperty("deployment.javaws.autodownload");
/*      */         
/*  275 */         if (str != null && str.equalsIgnoreCase("NEVER")) {
/*      */ 
/*      */           
/*  278 */           String str1 = paramLaunchDesc.getResources().getSelectedJRE().getVersion();
/*      */           
/*  280 */           throw new ExitException(new NoLocalJREException(paramLaunchDesc, str1, false), 2);
/*      */         } 
/*      */         
/*  283 */         if (str != null && str.equalsIgnoreCase("PROMPT"))
/*      */         {
/*  285 */           if (!AutoDownloadPrompt.prompt(jFrame, paramLaunchDesc)) {
/*      */ 
/*      */             
/*  288 */             String str1 = paramLaunchDesc.getResources().getSelectedJRE().getVersion();
/*      */             
/*  290 */             throw new ExitException(new NoLocalJREException(paramLaunchDesc, str1, true), 2);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  300 */     int i = Config.getIntProperty("deployment.javaws.update.timeout");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  308 */     boolean bool2 = (!bool || (!paramBoolean3 && jREInfo == null) || (!bool1 && (localApplicationProperties.forceUpdateCheck() || paramBoolean1 || (new RapidUpdateCheck(this)).doUpdateCheck(paramLaunchDesc, localApplicationProperties, i)))) ? true : false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  315 */     Trace.println("Offline mode: " + bool1 + "\nIsInCache: " + bool + "\nforceUpdate: " + bool2 + "\nInstalled JRE: " + jREInfo + "\nIsInstaller: " + paramBoolean1, TraceLevel.BASIC);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  322 */     if (bool2 && bool1) {
/*  323 */       throw new ExitException(new OfflineLaunchException(), 2);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  332 */     ArrayList arrayList = new ArrayList();
/*      */     
/*  334 */     if (bool2) {
/*  335 */       LaunchDesc launchDesc = downloadResources(paramLaunchDesc, (!paramBoolean3 && jREInfo == null), (!paramBoolean1 && paramBoolean2), arrayList, paramBoolean4);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  340 */       if (launchDesc != null) {
/*      */         
/*  342 */         removeTempJnlpFile(paramLaunchDesc);
/*  343 */         return launchDesc;
/*      */       } 
/*      */ 
/*      */       
/*  347 */       if (localApplicationProperties.forceUpdateCheck()) {
/*  348 */         localApplicationProperties.setForceUpdateCheck(false); 
/*  349 */         try { localApplicationProperties.store(); }
/*  350 */         catch (IOException iOException) { Trace.ignoredException(iOException); }
/*      */       
/*      */       } 
/*  353 */       if (!paramBoolean4) {
/*  354 */         checkCacheMax();
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  359 */     if (SingleInstanceManager.isServerRunning(paramLaunchDesc.getCanonicalHome().toString())) {
/*      */       
/*  361 */       String[] arrayOfString = Globals.getApplicationArgs();
/*      */       
/*  363 */       if (arrayOfString != null) {
/*  364 */         paramLaunchDesc.getApplicationDescriptor().setArguments(arrayOfString);
/*      */       }
/*      */ 
/*      */       
/*  368 */       if (SingleInstanceManager.connectToServer(paramLaunchDesc.toString()))
/*      */       {
/*      */         
/*  371 */         throw new ExitException(null, 0);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  376 */     if (!paramBoolean4) {
/*  377 */       SplashScreen.generateCustomSplash(jFrame, paramLaunchDesc, (bool2 || paramBoolean5));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  383 */     if (!paramBoolean3 && !arrayList.isEmpty()) {
/*  384 */       if (paramBoolean1);
/*      */ 
/*      */       
/*  387 */       executeInstallers(arrayList);
/*      */     } 
/*      */ 
/*      */     
/*  391 */     if (!paramBoolean4 && this._downloadWindow.getFrame() != null) {
/*  392 */       String str = ResourceManager.getString("launch.launchApplication");
/*  393 */       if (paramLaunchDesc.getLaunchType() == 4) {
/*  394 */         str = ResourceManager.getString("launch.launchInstaller");
/*      */       }
/*  396 */       this._downloadWindow.showLaunchingApplication(str);
/*      */     } 
/*      */     
/*  399 */     if (!paramBoolean3) {
/*      */ 
/*      */       
/*  402 */       if (jREInfo == null) {
/*      */         
/*  404 */         Config.refreshProps();
/*  405 */         jREInfo = LaunchSelection.selectJRE(paramLaunchDesc);
/*  406 */         if (jREInfo == null) {
/*  407 */           LaunchDescException launchDescException = new LaunchDescException(paramLaunchDesc, ResourceManager.getString("launch.error.missingjreversion"), null);
/*      */ 
/*      */           
/*  410 */           throw new ExitException(launchDescException, 2);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  415 */       JREDesc jREDesc = paramLaunchDesc.getResources().getSelectedJRE();
/*  416 */       long l1 = jREDesc.getMinHeap();
/*  417 */       long l2 = jREDesc.getMaxHeap();
/*  418 */       boolean bool3 = JnlpxArgs.isCurrentRunningJREHeap(l1, l2);
/*      */       
/*  420 */       Properties properties = paramLaunchDesc.getResources().getResourceProperties();
/*  421 */       String str = jREDesc.getVmArgs();
/*  422 */       boolean bool4 = JnlpxArgs.isAuxArgsMatch(properties, str);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  430 */       boolean bool5 = (JPDA.getDebuggeeType() == 1 || JPDA.getDebuggeeType() == 3) ? true : false;
/*      */ 
/*      */ 
/*      */       
/*  434 */       if (bool5 || !JnlpxArgs.getJVMCommand().equals(new File(jREInfo.getPath())) || !bool3 || !bool4) {
/*      */ 
/*      */         
/*      */         try {
/*      */ 
/*      */           
/*  440 */           paramArrayOfString = insertApplicationArgs(paramArrayOfString);
/*  441 */           execProgram(jREInfo, paramArrayOfString, l1, l2, properties, str);
/*  442 */         } catch (IOException iOException) {
/*  443 */           throw new ExitException(new JreExecException(jREInfo.getPath(), iOException), 2);
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  448 */         if (JnlpxArgs.shouldRemoveArgumentFile()) {
/*  449 */           JnlpxArgs.setShouldRemoveArgumentFile(String.valueOf(false));
/*      */         }
/*  451 */         throw new ExitException(null, 0);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  456 */     JnlpxArgs.removeArgumentFile(paramArrayOfString);
/*      */     
/*  458 */     if (paramBoolean3) {
/*  459 */       this._downloadWindow.disposeWindow();
/*      */       
/*  461 */       notifyLocalInstallHandler(paramLaunchDesc, localApplicationProperties, (bool2 || paramBoolean5), paramBoolean3, paramBoolean4, null);
/*      */       
/*  463 */       Trace.println("Exiting after import", TraceLevel.BASIC);
/*  464 */       throw new ExitException(null, 0);
/*      */     } 
/*      */     
/*  467 */     Trace.println("continuing launch in this VM", TraceLevel.BASIC);
/*      */     
/*  469 */     continueLaunch(localApplicationProperties, bool1, uRL, paramLaunchDesc, (bool2 || paramBoolean5), paramBoolean3, paramBoolean4);
/*      */ 
/*      */ 
/*      */     
/*  473 */     return null;
/*      */   }
/*      */   
/*      */   public static void checkCacheMax() {
/*  477 */     long l = Config.getCacheSizeMax();
/*  478 */     if (l > 0L)
/*  479 */       try { long l1 = Cache.getCacheSize();
/*  480 */         if (l1 > l * 90L / 100L) {
/*  481 */           String str = Config.getTempDirectory() + File.separator + "cachemax.timestamp";
/*      */           
/*  483 */           File file = new File(str);
/*  484 */           file.createNewFile();
/*  485 */           long l2 = file.lastModified();
/*  486 */           long l3 = (new Date()).getTime();
/*  487 */           if (l3 - l2 > 60000L) {
/*  488 */             file.setLastModified(l3);
/*  489 */             String str1 = ResourceManager.getString("jnlp.cache.warning.title");
/*      */             
/*  491 */             String str2 = ResourceManager.getString("jnlp.cache.warning.message", sizeString(l1), sizeString(l));
/*      */ 
/*      */             
/*  494 */             SwingUtilities.invokeAndWait(new Runnable(str2, str1) { private final String val$message; private final String val$title;
/*      */                   public void run() {
/*  496 */                     DialogFactory.showMessageDialog(3, this.val$message, this.val$title, true);
/*      */                   } }
/*      */               );
/*      */           }
/*      */         
/*      */         }
/*      */          }
/*      */       
/*  504 */       catch (Exception exception)
/*  505 */       { Trace.ignoredException(exception); }
/*      */        
/*      */   }
/*      */   
/*      */   private String[] insertApplicationArgs(String[] paramArrayOfString) {
/*  510 */     String[] arrayOfString1 = Globals.getApplicationArgs();
/*  511 */     if (arrayOfString1 == null) {
/*  512 */       return paramArrayOfString;
/*      */     }
/*  514 */     String[] arrayOfString2 = new String[arrayOfString1.length + paramArrayOfString.length];
/*      */     byte b1;
/*  516 */     for (b1 = 0; b1 < arrayOfString1.length; b1++) {
/*  517 */       arrayOfString2[b1] = arrayOfString1[b1];
/*      */     }
/*  519 */     for (byte b2 = 0; b2 < paramArrayOfString.length; b2++) {
/*  520 */       arrayOfString2[b1++] = paramArrayOfString[b2];
/*      */     }
/*  522 */     return arrayOfString2;
/*      */   }
/*      */ 
/*      */   
/*      */   private static String sizeString(long paramLong) {
/*  527 */     if (paramLong > 1048576L) {
/*  528 */       return "" + (paramLong / 1048576L) + "Mb";
/*      */     }
/*  530 */     return "" + paramLong + "bytes";
/*      */   }
/*      */   
/*      */   private static class EatInput implements Runnable {
/*      */     private InputStream _is;
/*      */     
/*      */     EatInput(InputStream param1InputStream) {
/*  537 */       this._is = param1InputStream;
/*      */     }
/*      */     
/*      */     public void run() {
/*  541 */       byte[] arrayOfByte = new byte[1024];
/*      */       try {
/*  543 */         int i = 0;
/*  544 */         while (i != -1) {
/*  545 */           i = this._is.read(arrayOfByte);
/*      */         }
/*  547 */       } catch (IOException iOException) {}
/*      */     }
/*      */     
/*      */     private static void eatInput(InputStream param1InputStream) {
/*  551 */       EatInput eatInput = new EatInput(param1InputStream);
/*  552 */       (new Thread(eatInput)).start();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void executeInstallers(ArrayList paramArrayList) throws ExitException {
/*  559 */     if (this._downloadWindow.getFrame() != null) {
/*  560 */       String str = ResourceManager.getString("launch.launchInstaller");
/*  561 */       this._downloadWindow.showLaunchingApplication(str);
/*      */ 
/*      */       
/*  564 */       (new Thread(new Runnable(this) { private final Launcher this$0;
/*      */             public void run() {
/*      */               try {
/*  567 */                 Thread.sleep(5000L);
/*  568 */               } catch (Exception exception) {}
/*  569 */               this.this$0._downloadWindow.setVisible(false);
/*      */             } }
/*      */         )).start();
/*      */     } 
/*  573 */     for (byte b = 0; b < paramArrayList.size(); b++) {
/*  574 */       File file = paramArrayList.get(b);
/*      */       
/*      */       try {
/*  577 */         LaunchDesc launchDesc = LaunchDescFactory.buildDescriptor(file);
/*  578 */         LocalApplicationProperties localApplicationProperties = Cache.getLocalApplicationProperties(file.getPath(), launchDesc);
/*      */         
/*  580 */         localApplicationProperties.setLocallyInstalled(false);
/*  581 */         localApplicationProperties.store();
/*      */ 
/*      */ 
/*      */         
/*  585 */         Trace.println("Installing extension: " + file, TraceLevel.EXTENSIONS);
/*      */         
/*  587 */         String[] arrayOfString = { "-installer", file.getAbsolutePath() };
/*      */ 
/*      */         
/*  590 */         JREInfo jREInfo = LaunchSelection.selectJRE(launchDesc);
/*  591 */         if (jREInfo == null) {
/*  592 */           this._downloadWindow.setVisible(true);
/*      */           
/*  594 */           LaunchDescException launchDescException = new LaunchDescException(launchDesc, ResourceManager.getString("launch.error.missingjreversion"), null);
/*      */           
/*  596 */           throw new ExitException(launchDescException, 2);
/*      */         } 
/*      */ 
/*      */         
/*  600 */         boolean bool = JnlpxArgs.shouldRemoveArgumentFile();
/*      */ 
/*      */ 
/*      */         
/*  604 */         JnlpxArgs.setShouldRemoveArgumentFile("false");
/*  605 */         Properties properties = launchDesc.getResources().getResourceProperties();
/*  606 */         Process process = execProgram(jREInfo, arrayOfString, -1L, -1L, properties, null);
/*  607 */         EatInput.eatInput(process.getErrorStream());
/*  608 */         EatInput.eatInput(process.getInputStream());
/*  609 */         process.waitFor();
/*      */ 
/*      */         
/*  612 */         JnlpxArgs.setShouldRemoveArgumentFile(String.valueOf(bool));
/*      */ 
/*      */         
/*  615 */         localApplicationProperties.refresh();
/*  616 */         if (localApplicationProperties.isRebootNeeded()) {
/*  617 */           boolean bool1 = false;
/*  618 */           ExtensionInstallHandler extensionInstallHandler = ExtensionInstallHandler.getInstance();
/*      */           
/*  620 */           if (extensionInstallHandler != null && extensionInstallHandler.doPreRebootActions(this._downloadWindow.getFrame()))
/*      */           {
/*  622 */             bool1 = true;
/*      */           }
/*      */           
/*  625 */           localApplicationProperties.setLocallyInstalled(true);
/*  626 */           localApplicationProperties.setRebootNeeded(false);
/*  627 */           localApplicationProperties.store();
/*  628 */           if (bool1 && extensionInstallHandler.doReboot()) {
/*  629 */             throw new ExitException(null, 1);
/*      */           }
/*      */         } 
/*  632 */         if (!localApplicationProperties.isLocallyInstalled()) {
/*  633 */           this._downloadWindow.setVisible(true);
/*      */           
/*  635 */           throw new ExitException(new LaunchDescException(launchDesc, ResourceManager.getString("Launch.error.installfailed"), null), 2);
/*      */         }
/*      */       
/*      */       }
/*  639 */       catch (JNLPException jNLPException) {
/*  640 */         this._downloadWindow.setVisible(true);
/*  641 */         throw new ExitException(jNLPException, 2);
/*  642 */       } catch (IOException iOException) {
/*  643 */         this._downloadWindow.setVisible(true);
/*  644 */         throw new ExitException(iOException, 2);
/*  645 */       } catch (InterruptedException interruptedException) {
/*  646 */         this._downloadWindow.setVisible(true);
/*  647 */         throw new ExitException(interruptedException, 2);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void executeUninstallers(ArrayList paramArrayList) throws ExitException {
/*  655 */     for (byte b = 0; b < paramArrayList.size(); b++) {
/*  656 */       File file = paramArrayList.get(b);
/*      */       
/*      */       try {
/*  659 */         LaunchDesc launchDesc = LaunchDescFactory.buildDescriptor(file);
/*  660 */         LocalApplicationProperties localApplicationProperties = Cache.getLocalApplicationProperties(file.getPath(), launchDesc);
/*      */ 
/*      */ 
/*      */         
/*  664 */         Trace.println("uninstalling extension: " + file, TraceLevel.EXTENSIONS);
/*      */ 
/*      */         
/*  667 */         String[] arrayOfString = { "-silent", "-secure", "-installer", file.getAbsolutePath() };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  673 */         JREInfo jREInfo = LaunchSelection.selectJRE(launchDesc);
/*  674 */         if (jREInfo == null) {
/*      */           
/*  676 */           LaunchDescException launchDescException = new LaunchDescException(launchDesc, ResourceManager.getString("launch.error.missingjreversion"), null);
/*      */ 
/*      */           
/*  679 */           throw new ExitException(launchDescException, 2);
/*      */         } 
/*      */         
/*  682 */         Properties properties = launchDesc.getResources().getResourceProperties();
/*  683 */         Process process = execProgram(jREInfo, arrayOfString, -1L, -1L, properties, null);
/*  684 */         EatInput.eatInput(process.getErrorStream());
/*  685 */         EatInput.eatInput(process.getInputStream());
/*  686 */         process.waitFor();
/*      */         
/*  688 */         localApplicationProperties.refresh();
/*  689 */         if (localApplicationProperties.isRebootNeeded()) {
/*  690 */           boolean bool = false;
/*  691 */           ExtensionInstallHandler extensionInstallHandler = ExtensionInstallHandler.getInstance();
/*      */           
/*  693 */           if (extensionInstallHandler != null && extensionInstallHandler.doPreRebootActions(null)) {
/*  694 */             bool = true;
/*      */           }
/*  696 */           localApplicationProperties.setRebootNeeded(false);
/*  697 */           localApplicationProperties.setLocallyInstalled(false);
/*  698 */           localApplicationProperties.store();
/*  699 */           if (bool && extensionInstallHandler.doReboot()) {
/*  700 */             throw new ExitException(null, 1);
/*      */           }
/*      */         } 
/*  703 */       } catch (JNLPException jNLPException) {
/*  704 */         throw new ExitException(jNLPException, 2);
/*  705 */       } catch (IOException iOException) {
/*  706 */         throw new ExitException(iOException, 2);
/*  707 */       } catch (InterruptedException interruptedException) {
/*  708 */         throw new ExitException(interruptedException, 2);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static Process execProgram(JREInfo paramJREInfo, String[] paramArrayOfString, long paramLong1, long paramLong2, Properties paramProperties, String paramString) throws IOException {
/*  716 */     String str1 = null;
/*  717 */     String str2 = null;
/*  718 */     str2 = paramJREInfo.getPath();
/*  719 */     if (Config.isDebugMode() && Config.isDebugVMMode()) {
/*  720 */       str1 = paramJREInfo.getDebugJavaPath();
/*      */     } else {
/*  722 */       str1 = paramJREInfo.getPath();
/*      */     } 
/*  724 */     if (str1.length() == 0 || str2.length() == 0) {
/*  725 */       throw new IllegalArgumentException("must exist");
/*      */     }
/*      */     
/*  728 */     String[] arrayOfString1 = JnlpxArgs.getArgumentList(str2, paramLong1, paramLong2, paramProperties, paramString);
/*      */     
/*  730 */     int i = 1 + arrayOfString1.length + paramArrayOfString.length;
/*  731 */     String[] arrayOfString2 = new String[i];
/*  732 */     byte b1 = 0;
/*  733 */     arrayOfString2[b1++] = str1;
/*      */     byte b2;
/*  735 */     for (b2 = 0; b2 < arrayOfString1.length; ) { arrayOfString2[b1++] = arrayOfString1[b2]; b2++; }
/*      */     
/*  737 */     for (b2 = 0; b2 < paramArrayOfString.length; ) { arrayOfString2[b1++] = paramArrayOfString[b2]; b2++; }
/*      */ 
/*      */     
/*  740 */     arrayOfString2 = JPDA.JpdaSetup(arrayOfString2, paramJREInfo);
/*      */ 
/*      */     
/*  743 */     Trace.println("Launching new JRE version: " + paramJREInfo, TraceLevel.BASIC);
/*  744 */     for (b2 = 0; b2 < arrayOfString2.length; b2++) {
/*  745 */       Trace.println("cmd " + b2 + " : " + arrayOfString2[b2], TraceLevel.BASIC);
/*      */     }
/*      */     
/*  748 */     if (Globals.TCKHarnessRun) {
/*  749 */       Main.tckprintln("JVM Starting");
/*      */     }
/*  751 */     Trace.flush();
/*  752 */     return Runtime.getRuntime().exec(arrayOfString2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void continueLaunch(LocalApplicationProperties paramLocalApplicationProperties, boolean paramBoolean1, URL paramURL, LaunchDesc paramLaunchDesc, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) throws ExitException {
/*  761 */     AppPolicy appPolicy = AppPolicy.createInstance(paramLaunchDesc.getCanonicalHome().getHost());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  768 */       LaunchDownload.checkSignedResources(paramLaunchDesc);
/*      */ 
/*      */       
/*  771 */       LaunchDownload.checkSignedLaunchDesc(paramLaunchDesc);
/*  772 */     } catch (JNLPException jNLPException) {
/*  773 */       throw new ExitException(jNLPException, 2);
/*  774 */     } catch (IOException iOException) {
/*      */       
/*  776 */       throw new ExitException(iOException, 2);
/*      */     } 
/*      */ 
/*      */     
/*  780 */     JNLPClassLoader jNLPClassLoader = JNLPClassLoader.createClassLoader(paramLaunchDesc, appPolicy);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  786 */     Thread.currentThread().setContextClassLoader((ClassLoader)jNLPClassLoader);
/*      */ 
/*      */     
/*  789 */     System.setSecurityManager((SecurityManager)new JavaWebStartSecurity());
/*      */ 
/*      */     
/*      */     try {
/*  793 */       SwingUtilities.invokeAndWait(new Runnable(this, jNLPClassLoader) { private final JNLPClassLoader val$netLoader; private final Launcher this$0;
/*      */             public void run() {
/*  795 */               Thread.currentThread().setContextClassLoader((ClassLoader)this.val$netLoader);
/*      */               
/*      */               try {
/*  798 */                 UIManager.setLookAndFeel(UIManager.getLookAndFeel());
/*  799 */               } catch (UnsupportedLookAndFeelException unsupportedLookAndFeelException) {
/*  800 */                 unsupportedLookAndFeelException.printStackTrace();
/*  801 */                 Trace.ignoredException(unsupportedLookAndFeelException);
/*      */               } 
/*      */             } }
/*      */         );
/*  805 */     } catch (InterruptedException interruptedException) {
/*  806 */       Trace.ignoredException(interruptedException);
/*  807 */     } catch (InvocationTargetException invocationTargetException) {
/*  808 */       Trace.ignoredException(invocationTargetException);
/*      */     } 
/*      */ 
/*      */     
/*  812 */     SwingUtilities.invokeLater(new Runnable(this) { private final Launcher this$0;
/*      */           public void run() {
/*  814 */             JavawsConsoleController.showConsoleIfEnable();
/*      */           } }
/*      */       );
/*      */ 
/*      */ 
/*      */     
/*  820 */     String str = null;
/*  821 */     Class clazz = null;
/*      */     try {
/*  823 */       str = LaunchDownload.getMainClassName(paramLaunchDesc, true);
/*      */       
/*  825 */       Trace.println("Main-class: " + str, TraceLevel.BASIC);
/*      */       
/*  827 */       if (str == null) {
/*  828 */         throw new ClassNotFoundException(str);
/*      */       }
/*      */       
/*  831 */       clazz = jNLPClassLoader.loadClass(str);
/*  832 */       if (getClass().getPackage().equals(clazz.getPackage())) {
/*  833 */         throw new ClassNotFoundException(str);
/*      */       }
/*  835 */     } catch (ClassNotFoundException classNotFoundException) {
/*  836 */       throw new ExitException(classNotFoundException, 2);
/*  837 */     } catch (IOException iOException) {
/*  838 */       throw new ExitException(iOException, 2);
/*  839 */     } catch (JNLPException jNLPException) {
/*  840 */       throw new ExitException(jNLPException, 2);
/*  841 */     } catch (Exception exception) {
/*  842 */       throw new ExitException(exception, 2);
/*  843 */     } catch (Throwable throwable) {
/*  844 */       throwable.printStackTrace();
/*  845 */       throw new ExitException(new Exception(), 2);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  856 */     URL uRL = paramLaunchDesc.getCodebase();
/*  857 */     if (uRL == null) {
/*  858 */       uRL = URLUtil.getBase(paramURL);
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/*  863 */       BasicServiceImpl.initialize(uRL, paramBoolean1, BrowserSupport.isWebBrowserSupported());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  869 */       if (paramLaunchDesc.getLaunchType() == 4) {
/*  870 */         String str1 = paramLocalApplicationProperties.getInstallDirectory();
/*  871 */         if (str1 == null) {
/*  872 */           str1 = Cache.getNewExtensionInstallDirectory();
/*  873 */           paramLocalApplicationProperties.setInstallDirectory(str1);
/*      */         } 
/*      */         
/*  876 */         ExtensionInstallerServiceImpl.initialize(str1, paramLocalApplicationProperties, this._downloadWindow);
/*      */       }
/*      */     
/*      */     }
/*  880 */     catch (IOException iOException) {
/*  881 */       throw new ExitException(iOException, 2);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  887 */       DownloadWindow downloadWindow = this._downloadWindow;
/*  888 */       this._downloadWindow = null;
/*      */ 
/*      */       
/*  891 */       notifyLocalInstallHandler(paramLaunchDesc, paramLocalApplicationProperties, paramBoolean2, paramBoolean3, paramBoolean4, downloadWindow.getFrame());
/*      */ 
/*      */ 
/*      */       
/*  895 */       if (Globals.TCKHarnessRun) {
/*  896 */         Main.tckprintln("JNLP Launching");
/*      */       }
/*  898 */       executeMainClass(paramLaunchDesc, paramLocalApplicationProperties, clazz, downloadWindow);
/*  899 */     } catch (SecurityException securityException) {
/*      */       
/*  901 */       throw new ExitException(securityException, 2);
/*  902 */     } catch (IllegalAccessException illegalAccessException) {
/*  903 */       throw new ExitException(illegalAccessException, 2);
/*  904 */     } catch (IllegalArgumentException illegalArgumentException) {
/*  905 */       throw new ExitException(illegalArgumentException, 2);
/*  906 */     } catch (InstantiationException instantiationException) {
/*  907 */       throw new ExitException(instantiationException, 2);
/*  908 */     } catch (InvocationTargetException invocationTargetException) {
/*  909 */       Exception exception = invocationTargetException;
/*  910 */       Throwable throwable = invocationTargetException.getTargetException();
/*  911 */       if (throwable instanceof Exception) {
/*  912 */         exception = (Exception)invocationTargetException.getTargetException();
/*      */       } else {
/*  914 */         throwable.printStackTrace();
/*      */       } 
/*  916 */       throw new ExitException(exception, 2);
/*  917 */     } catch (NoSuchMethodException noSuchMethodException) {
/*  918 */       throw new ExitException(noSuchMethodException, 2);
/*  919 */     } catch (Exception exception) {
/*  920 */       Trace.ignoredException(exception);
/*      */     } 
/*  922 */     if (paramLaunchDesc.getLaunchType() == 4)
/*  923 */       throw new ExitException(null, 0); 
/*      */   }
/*      */   
/*      */   public Launcher(LaunchDesc paramLaunchDesc) {
/*  927 */     this._shownDownloadWindow = false;
/*      */     this._launchDesc = paramLaunchDesc;
/*      */     this._downloadWindow = new DownloadWindow();
/*      */     Trace.println("new Launcher: " + paramLaunchDesc.toString(), TraceLevel.BASIC);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private LaunchDesc downloadResources(LaunchDesc paramLaunchDesc, boolean paramBoolean1, boolean paramBoolean2, ArrayList paramArrayList, boolean paramBoolean3) throws ExitException {
/*  936 */     if (!this._shownDownloadWindow && !paramBoolean3) {
/*  937 */       this._shownDownloadWindow = true;
/*  938 */       this._downloadWindow.buildIntroScreen();
/*  939 */       this._downloadWindow.showLoadingProgressScreen();
/*  940 */       this._downloadWindow.setVisible(true);
/*  941 */       SplashScreen.hide();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  947 */       if (paramBoolean2) {
/*  948 */         LaunchDesc launchDesc = LaunchDownload.getUpdatedLaunchDesc(paramLaunchDesc);
/*  949 */         if (launchDesc != null)
/*      */         {
/*      */           
/*  952 */           return launchDesc;
/*      */         }
/*      */       } 
/*      */       
/*  956 */       LaunchDownload.downloadExtensions(paramLaunchDesc, (LaunchDownload.DownloadProgress)this._downloadWindow, 0, paramArrayList);
/*  957 */       if (paramBoolean1) {
/*  958 */         LaunchDownload.downloadJRE(paramLaunchDesc, (LaunchDownload.DownloadProgress)this._downloadWindow, paramArrayList);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  965 */       LaunchDownload.checkJNLPSecurity(paramLaunchDesc);
/*      */ 
/*      */ 
/*      */       
/*  969 */       LaunchDownload.downloadEagerorAll(paramLaunchDesc, false, (LaunchDownload.DownloadProgress)this._downloadWindow, false);
/*      */     }
/*  971 */     catch (SecurityException securityException) {
/*      */ 
/*      */       
/*  974 */       throw new ExitException(securityException, 2);
/*  975 */     } catch (JNLPException jNLPException) {
/*  976 */       throw new ExitException(jNLPException, 2);
/*  977 */     } catch (IOException iOException) {
/*  978 */       throw new ExitException(iOException, 2);
/*      */     } 
/*  980 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void notifyLocalInstallHandler(LaunchDesc paramLaunchDesc, LocalApplicationProperties paramLocalApplicationProperties, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, Frame paramFrame) {
/*  991 */     if (paramLocalApplicationProperties == null)
/*  992 */       return;  paramLocalApplicationProperties.setLastAccessed(new Date());
/*  993 */     paramLocalApplicationProperties.incrementLaunchCount();
/*      */     
/*  995 */     LocalInstallHandler localInstallHandler = LocalInstallHandler.getInstance();
/*      */     
/*  997 */     if (paramLaunchDesc.isApplicationDescriptor() && (paramLaunchDesc.getLocation() != null || paramLaunchDesc.getInformation().supportsOfflineOperation())) {
/*      */ 
/*      */       
/* 1000 */       if (localInstallHandler != null && localInstallHandler.isLocalInstallSupported()) {
/* 1001 */         AssociationDesc[] arrayOfAssociationDesc = paramLocalApplicationProperties.getAssociations();
/* 1002 */         if (arrayOfAssociationDesc != null && arrayOfAssociationDesc.length > 0) {
/* 1003 */           if (paramBoolean1) {
/* 1004 */             localInstallHandler.removeAssociations(paramLaunchDesc, paramLocalApplicationProperties);
/* 1005 */             localInstallHandler.createAssociations(paramLaunchDesc, paramLocalApplicationProperties, true, paramFrame);
/*      */           } 
/*      */         } else {
/* 1008 */           localInstallHandler.createAssociations(paramLaunchDesc, paramLocalApplicationProperties, paramBoolean3, paramFrame);
/*      */         } 
/*      */         
/* 1011 */         if (paramLocalApplicationProperties.isLocallyInstalled()) {
/* 1012 */           if (paramBoolean1 && !paramLocalApplicationProperties.isLocallyInstalledSystem()) {
/* 1013 */             localInstallHandler.uninstall(paramLaunchDesc, paramLocalApplicationProperties, true);
/* 1014 */             localInstallHandler.install(paramLaunchDesc, paramLocalApplicationProperties);
/*      */           } 
/*      */         } else {
/* 1017 */           localInstallHandler.installFromLaunch(paramLaunchDesc, paramLocalApplicationProperties, paramBoolean3, paramFrame);
/*      */         } 
/*      */       } 
/*      */       
/* 1021 */       if (paramBoolean1) {
/* 1022 */         String str1 = paramLaunchDesc.getInformation().getTitle();
/* 1023 */         String str2 = paramLaunchDesc.getCanonicalHome().toString();
/*      */         
/* 1025 */         String str3 = paramLocalApplicationProperties.getRegisteredTitle();
/* 1026 */         if (str3 != null && str3.length() != 0) {
/* 1027 */           Config.getInstance().addRemoveProgramsRemove(str3, Globals.isSystemCache());
/*      */         }
/*      */         
/* 1030 */         paramLocalApplicationProperties.setRegisteredTitle(str1);
/* 1031 */         Config.getInstance().addRemoveProgramsAdd(Config.getInstance().toExecArg(str2), str1, Globals.isSystemCache());
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1039 */       paramLocalApplicationProperties.store();
/* 1040 */     } catch (IOException iOException) {
/*      */       
/* 1042 */       Trace.println("Couldn't save LAP: " + iOException, TraceLevel.BASIC);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void executeMainClass(LaunchDesc paramLaunchDesc, LocalApplicationProperties paramLocalApplicationProperties, Class paramClass, DownloadWindow paramDownloadWindow) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
/* 1052 */     if (paramLaunchDesc.getLaunchType() == 2) {
/* 1053 */       executeApplet(paramLaunchDesc, paramClass, paramDownloadWindow);
/*      */     } else {
/* 1055 */       executeApplication(paramLaunchDesc, paramLocalApplicationProperties, paramClass, paramDownloadWindow);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void executeApplication(LaunchDesc paramLaunchDesc, LocalApplicationProperties paramLocalApplicationProperties, Class paramClass, DownloadWindow paramDownloadWindow) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
/* 1065 */     String[] arrayOfString = null;
/* 1066 */     if (paramLaunchDesc.getLaunchType() == 4) {
/* 1067 */       paramDownloadWindow.reset();
/*      */       
/* 1069 */       arrayOfString = new String[1];
/* 1070 */       arrayOfString[0] = paramLocalApplicationProperties.isLocallyInstalled() ? "uninstall" : "install";
/* 1071 */       paramLocalApplicationProperties.setLocallyInstalled(false);
/* 1072 */       paramLocalApplicationProperties.setRebootNeeded(false);
/*      */       try {
/* 1074 */         paramLocalApplicationProperties.store();
/* 1075 */       } catch (IOException iOException) {
/* 1076 */         Trace.ignoredException(iOException);
/*      */       } 
/*      */     } else {
/*      */       
/* 1080 */       paramDownloadWindow.disposeWindow();
/* 1081 */       SplashScreen.hide();
/*      */       
/* 1083 */       if (Globals.getApplicationArgs() != null) {
/*      */         
/* 1085 */         arrayOfString = Globals.getApplicationArgs();
/*      */       } else {
/*      */         
/* 1088 */         arrayOfString = paramLaunchDesc.getApplicationDescriptor().getArguments();
/*      */       } 
/*      */     } 
/*      */     
/* 1092 */     Object[] arrayOfObject = { arrayOfString };
/*      */ 
/*      */     
/* 1095 */     Class[] arrayOfClass = { (new String[0]).getClass() };
/* 1096 */     Method method = paramClass.getMethod("main", arrayOfClass);
/*      */     
/* 1098 */     if (!Modifier.isStatic(method.getModifiers())) {
/* 1099 */       throw new NoSuchMethodException(ResourceManager.getString("launch.error.nonstaticmainmethod"));
/*      */     }
/*      */     
/* 1102 */     method.setAccessible(true);
/*      */     
/* 1104 */     PerfLogger.setEndTime("Calling Application main");
/* 1105 */     PerfLogger.outputLog();
/*      */ 
/*      */     
/* 1108 */     method.invoke(null, arrayOfObject);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void executeApplet(LaunchDesc paramLaunchDesc, Class paramClass, DownloadWindow paramDownloadWindow) throws IllegalAccessException, InstantiationException {
/* 1115 */     AppletDesc appletDesc = paramLaunchDesc.getAppletDescriptor();
/* 1116 */     int i = appletDesc.getWidth();
/* 1117 */     int j = appletDesc.getHeight();
/*      */     
/* 1119 */     Applet applet = null;
/* 1120 */     applet = paramClass.newInstance();
/*      */ 
/*      */     
/* 1123 */     SplashScreen.hide();
/* 1124 */     if (paramDownloadWindow.getFrame() == null) {
/*      */       
/* 1126 */       paramDownloadWindow.buildIntroScreen();
/* 1127 */       paramDownloadWindow.showLaunchingApplication(paramLaunchDesc.getInformation().getTitle());
/*      */     } 
/* 1129 */     JFrame jFrame = paramDownloadWindow.getFrame();
/*      */     
/* 1131 */     boolean bool = BrowserSupport.isWebBrowserSupported();
/*      */ 
/*      */     
/* 1134 */     AppletContainerCallback appletContainerCallback = new AppletContainerCallback(this, jFrame) { private final JFrame val$mainFrame; private final Launcher this$0;
/*      */         
/*      */         public void showDocument(URL param1URL) {
/* 1137 */           BrowserSupport.showDocument(param1URL);
/*      */         }
/*      */ 
/*      */         
/*      */         public void relativeResize(Dimension param1Dimension) {
/* 1142 */           Dimension dimension = this.val$mainFrame.getSize();
/* 1143 */           dimension.width += param1Dimension.width;
/* 1144 */           dimension.height += param1Dimension.height;
/* 1145 */           this.val$mainFrame.setSize(dimension);
/*      */         } }
/*      */       ;
/*      */     
/* 1149 */     URL uRL1 = BasicServiceImpl.getInstance().getCodeBase();
/* 1150 */     URL uRL2 = appletDesc.getDocumentBase();
/*      */     
/* 1152 */     if (uRL2 == null) uRL2 = uRL1;
/*      */ 
/*      */     
/* 1155 */     AppletContainer appletContainer = new AppletContainer(appletContainerCallback, applet, appletDesc.getName(), uRL2, uRL1, i, j, appletDesc.getParameters());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1170 */     jFrame.removeWindowListener((WindowListener)paramDownloadWindow);
/* 1171 */     jFrame.addWindowListener(new WindowAdapter(this, appletContainer) { private final AppletContainer val$ac;
/*      */           private final Launcher this$0;
/*      */           
/*      */           public void windowClosing(WindowEvent param1WindowEvent) {
/* 1175 */             this.val$ac.stopApplet();
/*      */           } }
/*      */       );
/*      */     
/* 1179 */     paramDownloadWindow.clearWindow();
/*      */     
/* 1181 */     jFrame.setTitle(paramLaunchDesc.getInformation().getTitle());
/* 1182 */     Container container = jFrame.getContentPane();
/* 1183 */     container.setLayout(new BorderLayout());
/* 1184 */     container.add("Center", (Component)appletContainer);
/* 1185 */     jFrame.pack();
/* 1186 */     Dimension dimension = appletContainer.getPreferredFrameSize(jFrame);
/* 1187 */     jFrame.setSize(dimension);
/*      */     
/* 1189 */     jFrame.getRootPane().revalidate();
/* 1190 */     jFrame.getRootPane().repaint();
/* 1191 */     jFrame.setResizable(false);
/* 1192 */     if (!jFrame.isVisible()) {
/* 1193 */       SwingUtilities.invokeLater(new Runnable(this, jFrame) { private final JFrame val$mainFrame; private final Launcher this$0;
/*      */             public void run() {
/* 1195 */               this.val$mainFrame.setVisible(true);
/*      */             } }
/*      */         );
/*      */     }
/*      */ 
/*      */     
/* 1201 */     appletContainer.startApplet();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void handleJnlpFileException(LaunchDesc paramLaunchDesc, Exception paramException) throws ExitException {
/* 1207 */     DiskCacheEntry diskCacheEntry = null;
/*      */     try {
/* 1209 */       diskCacheEntry = DownloadProtocol.getCachedLaunchedFile(paramLaunchDesc.getCanonicalHome());
/* 1210 */       if (diskCacheEntry != null) {
/* 1211 */         Cache.removeEntry(diskCacheEntry);
/*      */       }
/* 1213 */     } catch (JNLPException jNLPException) {
/* 1214 */       Trace.ignoredException((Exception)jNLPException);
/*      */     } 
/* 1216 */     JFrame jFrame = (this._downloadWindow == null) ? null : this._downloadWindow.getFrame();
/*      */     
/* 1218 */     throw new ExitException(paramException, 2);
/*      */   }
/*      */   private class RapidUpdateCheck extends Thread { private LaunchDesc _ld;
/*      */     private LocalApplicationProperties _lap;
/*      */     private boolean _updateAvailable;
/*      */     private boolean _checkCompleted;
/*      */     private Object _signalObject;
/*      */     private final Launcher this$0;
/*      */     
/*      */     public RapidUpdateCheck(Launcher this$0) {
/* 1228 */       this.this$0 = this$0; this._signalObject = null;
/* 1229 */       this._ld = null;
/* 1230 */       this._signalObject = new Object();
/*      */     }
/*      */ 
/*      */     
/*      */     private boolean doUpdateCheck(LaunchDesc param1LaunchDesc, LocalApplicationProperties param1LocalApplicationProperties, int param1Int) {
/* 1235 */       this._ld = param1LaunchDesc;
/* 1236 */       this._lap = param1LocalApplicationProperties;
/* 1237 */       boolean bool = false;
/*      */       
/* 1239 */       synchronized (this._signalObject) {
/* 1240 */         this._updateAvailable = false;
/* 1241 */         this._checkCompleted = false;
/* 1242 */         start();
/*      */         do {
/* 1244 */           if (param1LaunchDesc.getInformation().supportsOfflineOperation()) {
/*      */             
/*      */             try {
/* 1247 */               this._signalObject.wait(param1Int);
/* 1248 */               bool = this._updateAvailable;
/* 1249 */             } catch (InterruptedException interruptedException) {
/* 1250 */               bool = false;
/*      */             } 
/*      */           } else {
/*      */ 
/*      */             
/*      */             try {
/*      */               
/* 1257 */               this._signalObject.wait(param1Int);
/* 1258 */               bool = (this._updateAvailable || !this._checkCompleted);
/* 1259 */             } catch (InterruptedException interruptedException) {
/* 1260 */               bool = true;
/*      */             } 
/*      */           } 
/* 1263 */         } while ((this._ld.isHttps() && !this._checkCompleted) || (this.this$0._ja != null && this.this$0._ja.isChallanging()));
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1269 */       return bool;
/*      */     }
/*      */ 
/*      */     
/*      */     public void run() {
/* 1274 */       boolean bool = false;
/*      */       try {
/* 1276 */         bool = LaunchDownload.isUpdateAvailable(this._ld);
/* 1277 */       } catch (FailedDownloadingResourceException failedDownloadingResourceException) {
/* 1278 */         if (this._ld.isHttps()) {
/* 1279 */           Throwable throwable = failedDownloadingResourceException.getWrappedException();
/* 1280 */           if (throwable != null && throwable instanceof javax.net.ssl.SSLHandshakeException)
/*      */           {
/*      */             
/* 1283 */             Main.systemExit(0);
/*      */           }
/*      */         } 
/* 1286 */         Trace.ignoredException((Exception)failedDownloadingResourceException);
/* 1287 */       } catch (JNLPException jNLPException) {
/*      */         
/* 1289 */         Trace.ignoredException((Exception)jNLPException);
/*      */       } 
/*      */       
/* 1292 */       synchronized (this._signalObject) {
/* 1293 */         this._updateAvailable = bool;
/* 1294 */         this._checkCompleted = true;
/* 1295 */         this._signalObject.notify();
/*      */       } 
/*      */       
/* 1298 */       if (this._updateAvailable) {
/*      */ 
/*      */         
/* 1301 */         this._lap.setForceUpdateCheck(true);
/*      */         try {
/* 1303 */           this._lap.store();
/* 1304 */         } catch (IOException iOException) {
/* 1305 */           Trace.ignoredException(iOException);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */ 
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\Launcher.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */