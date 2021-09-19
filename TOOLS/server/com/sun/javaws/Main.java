/*      */ package com.sun.javaws;
/*      */ import com.sun.deploy.config.Config;
/*      */ import com.sun.deploy.config.JREInfo;
/*      */ import com.sun.deploy.net.proxy.DeployProxySelector;
/*      */ import com.sun.deploy.net.proxy.NSPreferences;
/*      */ import com.sun.deploy.resources.ResourceManager;
/*      */ import com.sun.deploy.services.ServiceManager;
/*      */ import com.sun.deploy.util.ConsoleController;
/*      */ import com.sun.deploy.util.ConsoleHelper;
/*      */ import com.sun.deploy.util.ConsoleTraceListener;
/*      */ import com.sun.deploy.util.ConsoleWindow;
/*      */ import com.sun.deploy.util.DialogFactory;
/*      */ import com.sun.deploy.util.FileTraceListener;
/*      */ import com.sun.deploy.util.LoggerTraceListener;
/*      */ import com.sun.deploy.util.PerfLogger;
/*      */ import com.sun.deploy.util.SocketTraceListener;
/*      */ import com.sun.deploy.util.Trace;
/*      */ import com.sun.deploy.util.TraceLevel;
/*      */ import com.sun.deploy.util.TraceListener;
/*      */ import com.sun.javaws.cache.Cache;
/*      */ import com.sun.javaws.exceptions.CacheAccessException;
/*      */ import com.sun.javaws.exceptions.CouldNotLoadArgumentException;
/*      */ import com.sun.javaws.exceptions.FailedDownloadingResourceException;
/*      */ import com.sun.javaws.exceptions.JNLPException;
/*      */ import com.sun.javaws.exceptions.TooManyArgumentsException;
/*      */ import com.sun.javaws.jnl.LaunchDesc;
/*      */ import com.sun.javaws.jnl.LaunchDescFactory;
/*      */ import com.sun.javaws.security.AppContextUtil;
/*      */ import com.sun.javaws.util.JavawsConsoleController;
/*      */ import com.sun.javaws.util.JavawsDialogListener;
/*      */ import com.sun.jnlp.JNLPClassLoader;
/*      */ import java.awt.Toolkit;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStreamReader;
/*      */ import java.net.Authenticator;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.Socket;
/*      */ import java.net.URL;
/*      */ import java.security.Security;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Date;
/*      */ import java.util.Properties;
/*      */ import java.util.logging.Level;
/*      */ import javax.jnlp.ServiceManager;
/*      */ import javax.jnlp.ServiceManagerStub;
/*      */ 
/*      */ public class Main {
/*      */   private static boolean _isViewer = false;
/*      */   private static boolean _launchingAllowed = false;
/*   56 */   private static String[] _tempfile = new String[1]; private static ThreadGroup _systemTG; private static ThreadGroup _securityTG; private static ThreadGroup _launchTG;
/*   57 */   private static DataInputStream _tckStream = null; private static long t0; private static long t1; private static long t2;
/*      */   private static long t3;
/*      */   private static long t4;
/*      */   private static long t5;
/*      */   private static boolean _timeing = true;
/*      */   private static boolean uninstall = false;
/*      */   
/*      */   public static void main(String[] paramArrayOfString) {
/*   65 */     PerfLogger.setStartTime("JavaWebStart main started");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   71 */     Thread.currentThread().setContextClassLoader((ClassLoader)JNLPClassLoader.createClassLoader());
/*      */     
/*   73 */     if (_timeing) t0 = (new Date()).getTime();
/*      */ 
/*      */     
/*   76 */     Toolkit.getDefaultToolkit();
/*      */     
/*   78 */     if (_timeing) t1 = (new Date()).getTime();
/*      */ 
/*      */ 
/*      */     
/*   82 */     _launchingAllowed = Config.isConfigValid();
/*      */     
/*   84 */     if (_timeing) t2 = (new Date()).getTime();
/*      */ 
/*      */     
/*   87 */     JPDA.setup();
/*      */ 
/*      */ 
/*      */     
/*   91 */     paramArrayOfString = Globals.parseOptions(paramArrayOfString);
/*      */     
/*   93 */     if (_timeing) t3 = (new Date()).getTime();
/*      */ 
/*      */     
/*   96 */     initTrace();
/*      */     
/*   98 */     if (_timeing) t4 = (new Date()).getTime();
/*      */     
/*  100 */     updateCache();
/*      */ 
/*      */     
/*  103 */     paramArrayOfString = parseArgs(paramArrayOfString);
/*      */     
/*  105 */     if (paramArrayOfString.length > 0) {
/*  106 */       _tempfile[0] = paramArrayOfString[0];
/*      */     }
/*      */ 
/*      */     
/*  110 */     if (Cache.canWrite()) {
/*      */       
/*  112 */       setupBrowser();
/*      */ 
/*      */ 
/*      */       
/*  116 */       JnlpxArgs.verify();
/*      */ 
/*      */       
/*  119 */       initializeExecutionEnvironment();
/*      */ 
/*      */       
/*  122 */       if (uninstall) {
/*  123 */         uninstallCache((paramArrayOfString.length > 0) ? paramArrayOfString[0] : null);
/*      */       }
/*      */ 
/*      */       
/*  127 */       if (Globals.TCKHarnessRun) {
/*  128 */         tckprintln("Java Started");
/*      */       }
/*      */       
/*  131 */       if (paramArrayOfString.length == 0) {
/*  132 */         _isViewer = true;
/*      */       }
/*      */       
/*  135 */       if (!_isViewer) {
/*  136 */         launchApp(paramArrayOfString, true);
/*      */       }
/*      */       
/*  139 */       if (_isViewer) {
/*  140 */         JnlpxArgs.removeArgumentFile(paramArrayOfString);
/*      */         try {
/*  142 */           if (_timeing) {
/*  143 */             t5 = (new Date()).getTime();
/*  144 */             Trace.println("startup times: \n      toolkit: " + (t1 - t0) + "\n" + "       config: " + (t2 - t1) + "\n" + "         args: " + (t3 - t2) + "\n" + "        trace: " + (t4 - t3) + "\n" + "     the rest: " + (t5 - t4) + "\n" + "", TraceLevel.BASIC);
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  153 */           Trace.println("Launching Cache Viewer", TraceLevel.BASIC);
/*      */ 
/*      */           
/*  156 */           Trace.flush();
/*  157 */           CacheViewer.main(paramArrayOfString);
/*  158 */         } catch (Exception exception) {
/*  159 */           LaunchErrorDialog.show(null, exception, true);
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/*  164 */       LaunchErrorDialog.show(null, (Throwable)new CacheAccessException(Globals.isSystemCache()), true);
/*      */     } 
/*  166 */     Trace.flush();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void launchApp(String[] paramArrayOfString, boolean paramBoolean) {
/*  172 */     if (paramArrayOfString.length > 1) {
/*  173 */       JnlpxArgs.removeArgumentFile(paramArrayOfString);
/*  174 */       LaunchErrorDialog.show(null, (Throwable)new TooManyArgumentsException(paramArrayOfString), paramBoolean);
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  180 */     LaunchDesc launchDesc = null;
/*      */     try {
/*  182 */       launchDesc = LaunchDescFactory.buildDescriptor(paramArrayOfString[0]);
/*  183 */     } catch (IOException iOException) {
/*  184 */       FailedDownloadingResourceException failedDownloadingResourceException; CouldNotLoadArgumentException couldNotLoadArgumentException = null;
/*  185 */       JnlpxArgs.removeArgumentFile(paramArrayOfString);
/*      */       
/*  187 */       couldNotLoadArgumentException = new CouldNotLoadArgumentException(paramArrayOfString[0], iOException);
/*      */       
/*  189 */       if (Globals.isJavaVersionAtLeast14())
/*      */       {
/*  191 */         if (iOException instanceof javax.net.ssl.SSLException || (iOException.getMessage() != null && iOException.getMessage().toLowerCase().indexOf("https") != -1)) {
/*      */           try {
/*  193 */             failedDownloadingResourceException = new FailedDownloadingResourceException(new URL(paramArrayOfString[0]), null, iOException);
/*  194 */           } catch (MalformedURLException malformedURLException) {
/*  195 */             Trace.ignoredException(malformedURLException);
/*      */           } 
/*      */         }
/*      */       }
/*      */ 
/*      */       
/*  201 */       LaunchErrorDialog.show(null, (Throwable)failedDownloadingResourceException, paramBoolean);
/*      */       return;
/*  203 */     } catch (JNLPException jNLPException) {
/*  204 */       JnlpxArgs.removeArgumentFile(paramArrayOfString);
/*      */       
/*  206 */       LaunchErrorDialog.show(null, (Throwable)jNLPException, paramBoolean);
/*      */       
/*      */       return;
/*      */     } 
/*  210 */     Globals.setCodebase(launchDesc.getCodebase());
/*      */ 
/*      */ 
/*      */     
/*  214 */     if (launchDesc.getLaunchType() == 5) {
/*  215 */       JnlpxArgs.removeArgumentFile(paramArrayOfString);
/*  216 */       String str = launchDesc.getInternalCommand();
/*  217 */       if (str.equals("viewer")) {
/*  218 */         _isViewer = true;
/*  219 */       } else if (str.equals("player")) {
/*  220 */         _isViewer = true;
/*      */       } else {
/*  222 */         launchJavaControlPanel(str);
/*  223 */         systemExit(0);
/*      */       }
/*      */     
/*      */     }
/*  227 */     else if (_launchingAllowed) {
/*  228 */       (new Launcher(launchDesc)).launch(paramArrayOfString, paramBoolean);
/*      */     } else {
/*  230 */       LaunchErrorDialog.show(null, (Throwable)new LaunchDescException(launchDesc, ResourceManager.getString("enterprize.cfg.mandatory", Config.getEnterprizeString()), null), paramBoolean);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void importApp(String paramString) {
/*  238 */     String[] arrayOfString = new String[1];
/*  239 */     arrayOfString[0] = paramString;
/*  240 */     Globals.setImportMode(true);
/*  241 */     Globals.setSilentMode(true);
/*  242 */     launchApp(arrayOfString, false);
/*  243 */     Launcher.checkCacheMax();
/*      */   }
/*      */   
/*      */   public static void launchJavaControlPanel(String paramString) {
/*  247 */     String[] arrayOfString = new String[7];
/*  248 */     String str = System.getProperty("javaplugin.user.profile");
/*  249 */     if (str == null) str = "";
/*      */     
/*  251 */     arrayOfString[0] = Config.getInstance().toExecArg(JREInfo.getDefaultJavaPath());
/*  252 */     arrayOfString[1] = "-cp";
/*  253 */     arrayOfString[2] = Config.getInstance().toExecArg(Config.getJavaHome() + File.separator + "lib" + File.separator + "deploy.jar");
/*      */     
/*  255 */     arrayOfString[3] = Config.getInstance().toExecArg("-Djavaplugin.user.profile=" + str);
/*      */     
/*  257 */     arrayOfString[4] = "com.sun.deploy.panel.ControlPanel";
/*  258 */     arrayOfString[5] = "-tab";
/*  259 */     arrayOfString[6] = (paramString == null) ? "general" : paramString;
/*  260 */     Trace.println("Launching Control Panel: " + arrayOfString[0] + " " + arrayOfString[1] + " " + arrayOfString[2] + " " + arrayOfString[3] + " " + arrayOfString[4] + " " + arrayOfString[5] + " ", TraceLevel.BASIC);
/*      */     
/*      */     try {
/*  263 */       Runtime.getRuntime().exec(arrayOfString);
/*  264 */     } catch (IOException iOException) {
/*  265 */       Trace.ignoredException(iOException);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void uninstallCache(String paramString) {
/*  270 */     int i = -1;
/*      */     
/*      */     try {
/*  273 */       i = uninstall(paramString);
/*  274 */     } catch (Exception exception) {
/*  275 */       LaunchErrorDialog.show(null, exception, !Globals.isSilentMode());
/*      */     } 
/*      */     
/*  278 */     systemExit(i);
/*      */   }
/*      */ 
/*      */   
/*      */   private static String[] parseArgs(String[] paramArrayOfString) {
/*  283 */     boolean bool = false;
/*  284 */     ArrayList arrayList = new ArrayList();
/*      */     
/*  286 */     for (byte b1 = 0; b1 < paramArrayOfString.length; b1++) {
/*  287 */       if (!paramArrayOfString[b1].startsWith("-")) {
/*  288 */         arrayList.add(paramArrayOfString[b1]);
/*      */       
/*      */       }
/*  291 */       else if (paramArrayOfString[b1].equals("-Xclearcache")) {
/*      */         try {
/*  293 */           Cache.remove();
/*  294 */           long l = Cache.getCacheSize();
/*  295 */           if (l > 0L) {
/*  296 */             System.err.println("Could not clean all entries  in cache since they are in use");
/*      */             
/*  298 */             if (Globals.TCKHarnessRun) {
/*  299 */               tckprintln("Cache Clear Failed");
/*      */             }
/*  301 */             systemExit(-1);
/*      */           } 
/*  303 */         } catch (IOException iOException) {
/*  304 */           Trace.println("Clear cached failed: " + iOException.getMessage());
/*  305 */           if (Globals.TCKHarnessRun) {
/*  306 */             tckprintln("Cache Clear Failed");
/*      */           }
/*  308 */           systemExit(-1);
/*      */         } 
/*  310 */         if (Globals.TCKHarnessRun) {
/*  311 */           tckprintln("Cache Clear Success");
/*      */         
/*      */         }
/*      */       }
/*  315 */       else if (paramArrayOfString[b1].equals("-offline")) {
/*  316 */         JnlpxArgs.SetIsOffline();
/*  317 */         Globals.setOffline(true);
/*      */       
/*      */       }
/*  320 */       else if (!paramArrayOfString[b1].equals("-online")) {
/*      */ 
/*      */         
/*  323 */         if (!paramArrayOfString[b1].equals("-Xnosplash"))
/*      */         {
/*      */           
/*  326 */           if (paramArrayOfString[b1].equals("-installer")) {
/*  327 */             Globals.setInstallMode(true);
/*      */           
/*      */           }
/*  330 */           else if (paramArrayOfString[b1].equals("-uninstall")) {
/*  331 */             uninstall = true;
/*  332 */             Globals.setInstallMode(true);
/*      */ 
/*      */           
/*      */           }
/*  336 */           else if (paramArrayOfString[b1].equals("-updateVersions")) {
/*  337 */             systemExit(0);
/*      */           
/*      */           }
/*  340 */           else if (paramArrayOfString[b1].equals("-import")) {
/*  341 */             Globals.setImportMode(true);
/*  342 */             bool = true;
/*      */           
/*      */           }
/*  345 */           else if (paramArrayOfString[b1].equals("-silent")) {
/*  346 */             Globals.setSilentMode(true);
/*      */           
/*      */           }
/*  349 */           else if (paramArrayOfString[b1].equals("-shortcut")) {
/*  350 */             Globals.setCreateShortcut(true);
/*      */           }
/*  352 */           else if (paramArrayOfString[b1].equals("-association")) {
/*  353 */             Globals.setCreateAssoc(true);
/*      */           
/*      */           }
/*  356 */           else if (paramArrayOfString[b1].equals("-codebase")) {
/*  357 */             if (b1 + 1 < paramArrayOfString.length) {
/*  358 */               String str = paramArrayOfString[++b1];
/*      */               try {
/*  360 */                 new URL(str);
/*  361 */               } catch (MalformedURLException malformedURLException) {
/*  362 */                 LaunchErrorDialog.show(null, malformedURLException, true);
/*      */               } 
/*  364 */               Globals.setCodebaseOverride(str);
/*      */             } 
/*  366 */             bool = true;
/*      */           
/*      */           }
/*  369 */           else if (paramArrayOfString[b1].equals("-system")) {
/*  370 */             Globals.setSystemCache(true);
/*  371 */             bool = true;
/*      */           
/*      */           }
/*  374 */           else if (paramArrayOfString[b1].equals("-secure")) {
/*  375 */             Globals.setSecureMode(true);
/*      */           
/*      */           }
/*  378 */           else if (paramArrayOfString[b1].equals("-open") || paramArrayOfString[b1].equals("-print")) {
/*      */             
/*  380 */             if (b1 + 1 < paramArrayOfString.length) {
/*  381 */               String[] arrayOfString1 = new String[2];
/*  382 */               arrayOfString1[0] = paramArrayOfString[b1++];
/*  383 */               arrayOfString1[1] = paramArrayOfString[b1];
/*  384 */               Globals.setApplicationArgs(arrayOfString1);
/*      */             } 
/*  386 */             bool = true;
/*      */           
/*      */           }
/*  389 */           else if (paramArrayOfString[b1].equals("-viewer")) {
/*  390 */             _isViewer = true;
/*      */           } else {
/*      */             
/*  393 */             Trace.println("unsupported option: " + paramArrayOfString[b1], TraceLevel.BASIC);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*  398 */     String[] arrayOfString = new String[arrayList.size()];
/*  399 */     for (byte b2 = 0; b2 < arrayOfString.length; b2++) {
/*  400 */       arrayOfString[b2] = arrayList.get(b2);
/*      */     }
/*      */     
/*  403 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void initTrace() {
/*  409 */     Trace.redirectStdioStderr();
/*      */     
/*  411 */     Trace.resetTraceLevel();
/*      */     
/*  413 */     Trace.setInitialTraceLevel();
/*      */     
/*  415 */     if (Globals.TraceBasic) Trace.setBasicTrace(true); 
/*  416 */     if (Globals.TraceNetwork) Trace.setNetTrace(true); 
/*  417 */     if (Globals.TraceCache) Trace.setCacheTrace(true); 
/*  418 */     if (Globals.TraceSecurity) Trace.setSecurityTrace(true); 
/*  419 */     if (Globals.TraceExtensions) Trace.setExtTrace(true); 
/*  420 */     if (Globals.TraceTemp) Trace.setTempTrace(true);
/*      */ 
/*      */ 
/*      */     
/*  424 */     if (Config.getProperty("deployment.console.startup.mode").equals("SHOW") && !Globals.isHeadless()) {
/*      */       
/*  426 */       JavawsConsoleController javawsConsoleController = JavawsConsoleController.getInstance();
/*      */ 
/*      */       
/*  429 */       ConsoleTraceListener consoleTraceListener = new ConsoleTraceListener((ConsoleController)javawsConsoleController);
/*      */ 
/*      */       
/*  432 */       ConsoleWindow consoleWindow = ConsoleWindow.create((ConsoleController)javawsConsoleController);
/*  433 */       javawsConsoleController.setConsole(consoleWindow);
/*  434 */       if (consoleTraceListener != null) {
/*  435 */         consoleTraceListener.setConsole(consoleWindow);
/*  436 */         Trace.addTraceListener((TraceListener)consoleTraceListener);
/*  437 */         consoleTraceListener.print(ConsoleHelper.displayVersion() + "\n");
/*  438 */         consoleTraceListener.print(ConsoleHelper.displayHelp());
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  444 */     SocketTraceListener socketTraceListener = initSocketTrace();
/*      */     
/*  446 */     if (socketTraceListener != null) {
/*  447 */       Trace.addTraceListener((TraceListener)socketTraceListener);
/*      */     }
/*      */ 
/*      */     
/*  451 */     FileTraceListener fileTraceListener = initFileTrace();
/*      */     
/*  453 */     if (fileTraceListener != null) {
/*  454 */       Trace.addTraceListener((TraceListener)fileTraceListener);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  459 */     if (Globals.isJavaVersionAtLeast14() && Config.getBooleanProperty("deployment.log")) {
/*      */       
/*  461 */       String str = null;
/*      */       
/*      */       try {
/*  464 */         str = Config.getProperty("deployment.javaws.logFileName");
/*  465 */         File file = new File(Config.getLogDirectory());
/*  466 */         if (str != null && str != "")
/*      */         {
/*      */ 
/*      */           
/*  470 */           if (str.compareToIgnoreCase("TEMP") != 0) {
/*      */ 
/*      */             
/*  473 */             File file1 = new File(str);
/*  474 */             if (file1.isDirectory()) {
/*  475 */               str = "";
/*      */             } else {
/*  477 */               file = file1.getParentFile();
/*  478 */               if (file != null) {
/*  479 */                 file.mkdirs();
/*      */               }
/*      */             } 
/*      */           } else {
/*  483 */             str = "";
/*      */           } 
/*      */         }
/*      */ 
/*      */         
/*  488 */         if (str == "") {
/*      */           
/*  490 */           file.mkdirs();
/*      */           
/*  492 */           str = Config.getLogDirectory() + File.separator + "javaws.log";
/*      */         } 
/*      */         
/*  495 */         LoggerTraceListener loggerTraceListener = new LoggerTraceListener("com.sun.deploy", str);
/*      */ 
/*      */ 
/*      */         
/*  499 */         if (loggerTraceListener != null) {
/*  500 */           loggerTraceListener.getLogger().setLevel(Level.ALL);
/*  501 */           JavawsConsoleController.getInstance().setLogger(loggerTraceListener.getLogger());
/*  502 */           Trace.addTraceListener((TraceListener)loggerTraceListener);
/*      */         } 
/*  504 */       } catch (Exception exception) {
/*  505 */         Trace.println("can not create log file in directory: " + Config.getLogDirectory(), TraceLevel.BASIC);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static FileTraceListener initFileTrace() {
/*  515 */     if (Config.getBooleanProperty("deployment.trace")) {
/*  516 */       File file = null;
/*  517 */       String str1 = Config.getProperty("deployment.user.logdir");
/*  518 */       String str2 = Config.getProperty("deployment.javaws.traceFileName");
/*      */       
/*      */       try {
/*  521 */         if (str2 != null && str2 != "" && str2.compareToIgnoreCase("TEMP") != 0) {
/*      */ 
/*      */           
/*  524 */           file = new File(str2);
/*  525 */           if (!file.isDirectory()) {
/*  526 */             int i = str2.lastIndexOf(File.separator);
/*  527 */             if (i != -1) {
/*  528 */               str1 = str2.substring(0, i);
/*      */             }
/*      */           } else {
/*  531 */             file = null;
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/*  536 */         File file1 = new File(str1);
/*  537 */         file1.mkdirs();
/*      */         
/*  539 */         if (file == null) {
/*  540 */           file = File.createTempFile("javaws", ".trace", file1);
/*      */         }
/*      */         
/*  543 */         return new FileTraceListener(file, true);
/*      */       }
/*  545 */       catch (Exception exception) {
/*  546 */         Trace.println("cannot create trace file in Directory: " + str1, TraceLevel.BASIC);
/*      */       } 
/*      */     } 
/*      */     
/*  550 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   private static SocketTraceListener initSocketTrace() {
/*  555 */     if (Globals.LogToHost != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  561 */       String str1 = Globals.LogToHost;
/*  562 */       String str2 = null;
/*  563 */       int i = -1;
/*      */       
/*  565 */       boolean bool = false;
/*  566 */       int j = 0;
/*  567 */       if (str1.charAt(0) == '[' && (j = str1.indexOf('\001', 93)) != -1) {
/*  568 */         bool = true;
/*      */       } else {
/*  570 */         j = str1.indexOf(":");
/*      */       } 
/*  572 */       str2 = str1.substring(bool, j);
/*  573 */       if (str2 == null)
/*      */       {
/*  575 */         return null;
/*      */       }
/*      */       try {
/*  578 */         String str = str1.substring(str1.lastIndexOf(':') + 1);
/*  579 */         i = Integer.parseInt(str);
/*  580 */       } catch (NumberFormatException numberFormatException) {
/*  581 */         i = -1;
/*      */       } 
/*      */       
/*  584 */       if (i < 0)
/*      */       {
/*  586 */         return null;
/*      */       }
/*      */       
/*  589 */       SocketTraceListener socketTraceListener = new SocketTraceListener(str2, i);
/*      */ 
/*      */       
/*  592 */       if (socketTraceListener != null) {
/*  593 */         Socket socket = socketTraceListener.getSocket();
/*      */         
/*  595 */         if (Globals.TCKResponse && socket != null) {
/*      */           try {
/*  597 */             _tckStream = new DataInputStream(socket.getInputStream());
/*      */           }
/*  599 */           catch (IOException iOException) {
/*  600 */             Trace.ignoredException(iOException);
/*      */           } 
/*      */         }
/*      */       } 
/*      */       
/*  605 */       return socketTraceListener;
/*      */     } 
/*      */     
/*  608 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static int uninstall(String paramString) {
/*  614 */     if (paramString == null) {
/*  615 */       Trace.println("Uninstall all!", TraceLevel.BASIC);
/*  616 */       uninstallAll();
/*  617 */       if (Globals.TCKHarnessRun) {
/*  618 */         tckprintln("Cache Clear Success");
/*      */       }
/*      */     } else {
/*  621 */       Trace.println("Uninstall: " + paramString, TraceLevel.BASIC);
/*      */       
/*  623 */       LaunchDesc launchDesc = null;
/*      */       try {
/*  625 */         launchDesc = LaunchDescFactory.buildDescriptor(paramString);
/*  626 */       } catch (IOException iOException) {
/*  627 */         Trace.ignoredException(iOException);
/*  628 */       } catch (JNLPException jNLPException) {
/*  629 */         Trace.ignoredException((Exception)jNLPException);
/*      */       } 
/*  631 */       if (launchDesc != null) {
/*  632 */         LocalApplicationProperties localApplicationProperties = null;
/*  633 */         if (launchDesc.isInstaller() || launchDesc.isLibrary()) {
/*      */ 
/*      */ 
/*      */           
/*  637 */           localApplicationProperties = Cache.getLocalApplicationProperties(paramString, launchDesc);
/*      */         } else {
/*  639 */           localApplicationProperties = Cache.getLocalApplicationProperties(launchDesc.getCanonicalHome(), launchDesc);
/*      */         } 
/*      */ 
/*      */         
/*  643 */         if (localApplicationProperties != null) {
/*  644 */           Cache.remove(paramString, localApplicationProperties, launchDesc);
/*  645 */           Cache.clean();
/*  646 */           if (Globals.TCKHarnessRun) {
/*  647 */             tckprintln("Cache Clear Success");
/*      */           }
/*      */           
/*  650 */           return 0;
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  656 */       Trace.println("Error uninstalling!", TraceLevel.BASIC);
/*      */       
/*  658 */       if (Globals.TCKHarnessRun) {
/*  659 */         tckprintln("Cache Clear Failed");
/*      */       }
/*      */       
/*  662 */       if (!Globals.isSilentMode()) {
/*  663 */         SplashScreen.hide();
/*  664 */         DialogFactory.showErrorDialog(null, ResourceManager.getMessage("uninstall.failedMessage"), ResourceManager.getMessage("uninstall.failedMessageTitle"));
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  672 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void uninstallAll() {
/*  679 */     Cache.remove();
/*      */   }
/*      */ 
/*      */   
/*      */   private static void setupBrowser() {
/*  684 */     if (Config.getBooleanProperty("deployment.capture.mime.types")) {
/*      */       
/*  686 */       setupNS6();
/*  687 */       setupOpera();
/*      */       
/*  689 */       Config.setBooleanProperty("deployment.capture.mime.types", false);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void setupOpera() {
/*  697 */     OperaSupport operaSupport = BrowserSupport.getInstance().getOperaSupport();
/*      */     
/*  699 */     if (operaSupport != null && 
/*  700 */       operaSupport.isInstalled()) {
/*  701 */       operaSupport.enableJnlp(new File(JREInfo.getDefaultJavaPath()), Config.getBooleanProperty("deployment.update.mime.types"));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void setupNS6() {
/*  713 */     String str1 = null;
/*      */     
/*  715 */     str1 = BrowserSupport.getInstance().getNS6MailCapInfo();
/*      */     
/*  717 */     String str2 = "user_pref(\"browser.helperApps.neverAsk.openFile\", \"application%2Fx-java-jnlp-file\");\n";
/*      */ 
/*      */ 
/*      */     
/*  721 */     String str3 = System.getProperty("user.home");
/*      */     
/*  723 */     File file1 = new File(str3 + "/.mozilla/appreg");
/*      */     
/*  725 */     File file2 = null;
/*      */     try {
/*  727 */       file2 = NSPreferences.getNS6PrefsFile(file1);
/*      */     }
/*  729 */     catch (IOException iOException) {
/*      */       
/*  731 */       Trace.println("cannot determine NS6 prefs.js location", TraceLevel.BASIC);
/*      */     } 
/*      */     
/*  734 */     if (file2 == null) {
/*      */       return;
/*      */     }
/*  737 */     FileInputStream fileInputStream = null;
/*      */     try {
/*      */       boolean bool2;
/*  740 */       String str4 = null;
/*  741 */       fileInputStream = new FileInputStream(file2);
/*      */       
/*  743 */       BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
/*      */ 
/*      */       
/*  746 */       String str5 = "";
/*  747 */       boolean bool1 = true;
/*      */       
/*  749 */       if (str1 == null) {
/*  750 */         bool2 = false;
/*      */       } else {
/*  752 */         bool2 = true;
/*      */       } 
/*      */       
/*      */       while (true) {
/*      */         try {
/*  757 */           str4 = bufferedReader.readLine();
/*      */           
/*  759 */           if (str4 == null) {
/*  760 */             fileInputStream.close();
/*      */             break;
/*      */           } 
/*  763 */           str5 = str5 + str4 + "\n";
/*  764 */           if (str4.indexOf("x-java-jnlp-file") != -1)
/*      */           {
/*  766 */             bool1 = false;
/*      */           }
/*  768 */           if (str1 != null && str4.indexOf(".mime.types") != -1) {
/*  769 */             bool2 = false;
/*      */           }
/*      */         }
/*  772 */         catch (IOException iOException) {
/*  773 */           Trace.ignoredException(iOException);
/*      */         } 
/*      */       } 
/*      */       
/*  777 */       if (!bool1 && !bool2) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/*  782 */       if (bool1) str5 = str5 + str2;
/*      */       
/*  784 */       if (str1 != null && bool2) {
/*  785 */         str5 = str5 + str1;
/*      */       }
/*      */       
/*  788 */       FileOutputStream fileOutputStream = new FileOutputStream(file2);
/*      */       try {
/*  790 */         fileOutputStream.write(str5.getBytes());
/*  791 */         fileOutputStream.close();
/*  792 */       } catch (IOException iOException) {
/*  793 */         Trace.ignoredException(iOException);
/*      */       }
/*      */     
/*  796 */     } catch (FileNotFoundException fileNotFoundException) {
/*      */       
/*  798 */       Trace.ignoredException(fileNotFoundException);
/*  799 */       String str = "";
/*      */ 
/*      */       
/*  802 */       if (str1 != null) str = str + str1;
/*      */ 
/*      */       
/*  805 */       str = str + str2;
/*      */       
/*      */       try {
/*  808 */         FileOutputStream fileOutputStream = new FileOutputStream(file2);
/*      */         
/*  810 */         fileOutputStream.write(str.getBytes());
/*  811 */         fileOutputStream.close();
/*  812 */       } catch (IOException iOException) {
/*  813 */         Trace.ignoredException(iOException);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void updateCache() {
/*  822 */     if (Config.getProperty("deployment.javaws.cachedir") != null) {
/*  823 */       Cache.updateCache();
/*  824 */       Config.setProperty("deployment.javaws.cachedir", null);
/*  825 */       Config.storeIfDirty();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void initializeExecutionEnvironment() {
/*  835 */     boolean bool = (Config.getOSName().indexOf("Windows") != -1) ? true : false;
/*  836 */     boolean bool1 = Globals.isJavaVersionAtLeast15();
/*      */ 
/*      */     
/*  839 */     if (bool) {
/*      */       
/*  841 */       if (bool1) {
/*  842 */         ServiceManager.setService(33024);
/*      */       } else {
/*  844 */         ServiceManager.setService(16640);
/*      */       
/*      */       }
/*      */     
/*      */     }
/*  849 */     else if (bool1) {
/*  850 */       ServiceManager.setService(36864);
/*      */     } else {
/*  852 */       ServiceManager.setService(20480);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  857 */     Properties properties = System.getProperties();
/*      */ 
/*      */     
/*  860 */     properties.put("http.auth.serializeRequests", "true");
/*      */ 
/*      */     
/*  863 */     if (Globals.isJavaVersionAtLeast14()) {
/*  864 */       String str = (String)properties.get("java.protocol.handler.pkgs");
/*  865 */       if (str != null) {
/*  866 */         properties.put("java.protocol.handler.pkgs", str + "|com.sun.deploy.net.protocol");
/*      */       } else {
/*  868 */         properties.put("java.protocol.handler.pkgs", "com.sun.deploy.net.protocol");
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  873 */     properties.setProperty("javawebstart.version", Globals.getComponentName());
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  879 */       DeployProxySelector.reset();
/*      */ 
/*      */       
/*  882 */       DeployCookieSelector.reset();
/*      */     }
/*  884 */     catch (Throwable throwable) {
/*      */ 
/*      */       
/*  887 */       StaticProxyManager.reset();
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  896 */     if (Config.getBooleanProperty("deployment.security.authenticator")) {
/*      */       
/*  898 */       JAuthenticator jAuthenticator = JAuthenticator.getInstance((Frame)null);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  904 */       Authenticator.setDefault((Authenticator)jAuthenticator);
/*      */     } 
/*      */ 
/*      */     
/*  908 */     ServiceManager.setServiceManagerStub((ServiceManagerStub)new JnlpLookupStub());
/*      */ 
/*      */ 
/*      */     
/*  912 */     addToSecurityProperty("package.access", "com.sun.javaws");
/*  913 */     addToSecurityProperty("package.access", "com.sun.deploy");
/*  914 */     addToSecurityProperty("package.definition", "com.sun.javaws");
/*  915 */     addToSecurityProperty("package.definition", "com.sun.deploy");
/*  916 */     addToSecurityProperty("package.definition", "com.sun.jnlp");
/*      */ 
/*      */     
/*  919 */     addToSecurityProperty("package.access", "org.mozilla.jss");
/*  920 */     addToSecurityProperty("package.definition", "org.mozilla.jss");
/*      */ 
/*      */     
/*  923 */     DialogFactory.addDialogListener((DialogListener)new JavawsDialogListener());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  932 */     if (properties.get("https.protocols") == null && !Config.getBooleanProperty("deployment.security.TLSv1"))
/*      */     {
/*  934 */       properties.put("https.protocols", "SSLv3,SSLv2Hello");
/*      */     }
/*      */   }
/*      */   
/*      */   private static void addToSecurityProperty(String paramString1, String paramString2) {
/*  939 */     String str = Security.getProperty(paramString1);
/*      */     
/*  941 */     Trace.println("property " + paramString1 + " value " + str, TraceLevel.SECURITY);
/*      */     
/*  943 */     if (str != null) {
/*  944 */       str = str + "," + paramString2;
/*      */     } else {
/*      */       
/*  947 */       str = paramString2;
/*      */     } 
/*  949 */     Security.setProperty(paramString1, str);
/*      */     
/*  951 */     Trace.println("property " + paramString1 + " new value " + str, TraceLevel.SECURITY);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void systemExit(int paramInt) {
/*  957 */     JnlpxArgs.removeArgumentFile(_tempfile);
/*  958 */     SplashScreen.hide();
/*  959 */     Trace.flush();
/*  960 */     System.exit(paramInt);
/*      */   }
/*      */   
/*      */   static boolean isViewer() {
/*  964 */     return _isViewer;
/*      */   }
/*      */   
/*      */   public static final ThreadGroup getLaunchThreadGroup() {
/*  968 */     initializeThreadGroups();
/*  969 */     return _launchTG;
/*      */   }
/*      */   
/*      */   public static final ThreadGroup getSecurityThreadGroup() {
/*  973 */     initializeThreadGroups();
/*  974 */     return _securityTG;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void initializeThreadGroups() {
/*  979 */     if (_securityTG == null) {
/*  980 */       _systemTG = Thread.currentThread().getThreadGroup();
/*  981 */       while (_systemTG.getParent() != null) {
/*  982 */         _systemTG = _systemTG.getParent();
/*      */       }
/*      */       
/*  985 */       _securityTG = new ThreadGroup(_systemTG, "javawsSecurityThreadGroup");
/*      */ 
/*      */       
/*  988 */       (new Thread(_securityTG, new Runnable()
/*      */           {
/*      */             public void run() {
/*  991 */               AppContextUtil.createSecurityAppContext();
/*      */             }
/*      */           })).start();
/*      */       
/*  995 */       _launchTG = new ThreadGroup(_systemTG, "javawsApplicationThreadGroup");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static synchronized void tckprintln(String paramString) {
/* 1008 */     long l = System.currentTimeMillis();
/* 1009 */     Trace.println("##TCKHarnesRun##:" + l + ":" + Runtime.getRuntime().hashCode() + ":" + Thread.currentThread() + ":" + paramString);
/*      */ 
/*      */     
/* 1012 */     if (_tckStream != null)
/*      */       try {
/* 1014 */         while (_tckStream.readLong() < l);
/* 1015 */       } catch (IOException iOException) {
/* 1016 */         System.err.println("Warning:Exceptions occurred, while logging to logSocket");
/*      */         
/* 1018 */         iOException.printStackTrace(System.err);
/*      */       }  
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\Main.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */