/*     */ package com.sun.javaws;
/*     */ 
/*     */ import com.sun.deploy.association.Association;
/*     */ import com.sun.deploy.association.AssociationAlreadyRegisteredException;
/*     */ import com.sun.deploy.association.AssociationNotRegisteredException;
/*     */ import com.sun.deploy.association.AssociationService;
/*     */ import com.sun.deploy.association.RegisterFailedException;
/*     */ import com.sun.deploy.config.Config;
/*     */ import com.sun.deploy.resources.ResourceManager;
/*     */ import com.sun.deploy.util.DialogFactory;
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.deploy.util.TraceLevel;
/*     */ import com.sun.deploy.util.WinRegistry;
/*     */ import com.sun.javaws.cache.Cache;
/*     */ import com.sun.javaws.jnl.InformationDesc;
/*     */ import com.sun.javaws.jnl.LaunchDesc;
/*     */ import com.sun.javaws.jnl.RContentDesc;
/*     */ import com.sun.javaws.jnl.ShortcutDesc;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.StringTokenizer;
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
/*     */ public class WinInstallHandler
/*     */   extends LocalInstallHandler
/*     */ {
/*     */   private static final String INSTALLED_DESKTOP_SHORTCUT_KEY = "windows.installedDesktopShortcut";
/*     */   private static final String INSTALLED_START_MENU_KEY = "windows.installedStartMenuShortcut";
/*     */   private static final String UNINSTALLED_START_MENU_KEY = "windows.uninstalledStartMenuShortcut";
/*     */   private static final String RCONTENT_START_MENU_KEY = "windows.RContent.shortcuts";
/*     */   public static final int TYPE_DESKTOP = 1;
/*     */   public static final int TYPE_START_MENU = 2;
/*     */   private static final String REG_SHORTCUT_PATH = "Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders";
/*     */   private static final String REG_DESKTOP_PATH_KEY = "Desktop";
/*     */   private static final String REG_START_MENU_PATH_KEY = "Programs";
/*     */   private static final String SHORTCUT_EXTENSION = ".lnk";
/*     */   private static final int MAX_PATH = 200;
/*     */   private boolean _loadedPaths = false;
/*     */   private String _desktopPath;
/*     */   private String _startMenuPath;
/*     */   private static boolean useSystem;
/*     */   
/*     */   static {
/*  97 */     NativeLibrary.getInstance().load();
/*  98 */     String str = System.getProperty("os.name");
/*  99 */     if (str.indexOf("2000") != -1 || str.indexOf("XP") != -1) {
/*     */       
/* 101 */       useSystem = false;
/*     */     } else {
/*     */       
/* 104 */       useSystem = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getDefaultIconPath() {
/* 109 */     return Config.getInstance().getSystemJavawsPath();
/*     */   }
/*     */   
/*     */   public String getAssociationOpenCommand(String paramString) {
/* 113 */     return "\"" + Config.getJavawsCommand() + "\"" + " \"-open\" \"%1\" " + "\"" + paramString + "\"";
/*     */   }
/*     */   public String getAssociationPrintCommand(String paramString) {
/* 116 */     return "\"" + Config.getJavawsCommand() + "\"" + " \"-print\" \"%1\" " + "\"" + paramString + "\"";
/*     */   }
/*     */   
/*     */   public void registerAssociationInternal(Association paramAssociation) throws AssociationAlreadyRegisteredException, RegisterFailedException {
/* 120 */     AssociationService associationService = new AssociationService();
/* 121 */     if (Globals.isSystemCache() || useSystem) {
/*     */ 
/*     */       
/* 124 */       associationService.registerSystemAssociation(paramAssociation);
/*     */     }
/*     */     else {
/*     */       
/* 128 */       associationService.registerUserAssociation(paramAssociation);
/*     */     } 
/*     */   }
/*     */   public void unregisterAssociationInternal(Association paramAssociation) throws AssociationNotRegisteredException, RegisterFailedException {
/* 132 */     AssociationService associationService = new AssociationService();
/* 133 */     if (Globals.isSystemCache() || useSystem) {
/*     */ 
/*     */       
/* 136 */       associationService.unregisterSystemAssociation(paramAssociation);
/*     */     }
/*     */     else {
/*     */       
/* 140 */       associationService.unregisterUserAssociation(paramAssociation);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLocalInstallSupported() {
/* 151 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isAssociationSupported() {
/* 155 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void associationCompleted() {}
/*     */ 
/*     */   
/*     */   public void uninstall(LaunchDesc paramLaunchDesc, LocalApplicationProperties paramLocalApplicationProperties, boolean paramBoolean) {
/* 164 */     if (paramLocalApplicationProperties == null) {
/*     */       
/* 166 */       Trace.println("No LAP for uninstall, bailing!", TraceLevel.TEMP);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 172 */     String str2 = null;
/* 173 */     boolean bool = false;
/*     */     String str1;
/* 175 */     if ((str1 = paramLocalApplicationProperties.get("windows.installedStartMenuShortcut")) != null) {
/* 176 */       if (!uninstallShortcut(str1)) {
/* 177 */         bool = true;
/*     */       } else {
/* 179 */         paramLocalApplicationProperties.put("windows.installedStartMenuShortcut", null);
/*     */       } 
/* 181 */       str2 = str1;
/*     */     } 
/*     */     
/* 184 */     if ((str1 = paramLocalApplicationProperties.get("windows.uninstalledStartMenuShortcut")) != null) {
/* 185 */       if (!uninstallShortcut(str1)) {
/* 186 */         bool = true;
/*     */       } else {
/* 188 */         paramLocalApplicationProperties.put("windows.uninstalledStartMenuShortcut", null);
/*     */       } 
/* 190 */       str2 = str1;
/*     */     } 
/*     */     
/* 193 */     String str3 = paramLocalApplicationProperties.get("windows.RContent.shortcuts");
/* 194 */     if (str3 != null) {
/* 195 */       StringTokenizer stringTokenizer = new StringTokenizer(str3, File.pathSeparator);
/* 196 */       while (stringTokenizer.hasMoreElements()) {
/* 197 */         str1 = stringTokenizer.nextToken();
/* 198 */         if (str1 != null) {
/* 199 */           if (!uninstallShortcut(str1)) {
/* 200 */             bool = true;
/*     */           }
/* 202 */           str2 = str1;
/*     */         } 
/*     */       } 
/* 205 */       paramLocalApplicationProperties.put("windows.RContent.shortcuts", null);
/*     */     } 
/*     */     
/* 208 */     if (str2 != null) {
/* 209 */       checkEmpty(str2);
/*     */     }
/*     */     
/* 212 */     if (paramBoolean && (
/* 213 */       str1 = paramLocalApplicationProperties.get("windows.installedDesktopShortcut")) != null) {
/* 214 */       if (!uninstallShortcut(str1)) {
/* 215 */         bool = true;
/*     */       } else {
/* 217 */         paramLocalApplicationProperties.put("windows.installedDesktopShortcut", null);
/*     */       } 
/*     */     }
/*     */     
/* 221 */     if (bool) {
/* 222 */       Trace.println("uninstall shortcut failed", TraceLevel.TEMP);
/*     */     }
/*     */     
/* 225 */     paramLocalApplicationProperties.setLocallyInstalled(false);
/* 226 */     save(paramLocalApplicationProperties);
/*     */   }
/*     */   
/*     */   private void checkEmpty(String paramString) {
/*     */     try {
/* 231 */       File file = (new File(paramString)).getParentFile();
/* 232 */       if (file != null && file.isDirectory() && (file.list()).length == 0)
/*     */       {
/* 234 */         file.delete();
/*     */       }
/* 236 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasValidTitle(LaunchDesc paramLaunchDesc) {
/* 244 */     if (paramLaunchDesc == null) {
/* 245 */       return false;
/*     */     }
/* 247 */     InformationDesc informationDesc = paramLaunchDesc.getInformation();
/*     */     
/* 249 */     if (informationDesc == null || informationDesc.getTitle().trim() == null) {
/*     */       
/* 251 */       Trace.println("Invalid: No title!", TraceLevel.TEMP);
/*     */       
/* 253 */       return false;
/*     */     } 
/* 255 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void install(LaunchDesc paramLaunchDesc, LocalApplicationProperties paramLocalApplicationProperties) {
/* 260 */     if (!hasValidTitle(paramLaunchDesc)) {
/*     */       return;
/*     */     }
/*     */     
/* 264 */     if (isApplicationInstalled(paramLaunchDesc) && 
/* 265 */       !shouldInstallOverExisting(paramLaunchDesc)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 270 */     String str = null;
/*     */     
/*     */     try {
/* 273 */       str = Cache.getCachedLaunchedFile(paramLaunchDesc.getCanonicalHome()).getAbsolutePath();
/*     */     }
/* 275 */     catch (IOException iOException) {
/* 276 */       Trace.ignoredException(iOException);
/*     */     } 
/*     */     
/* 279 */     if (str == null) {
/* 280 */       installFailed(paramLaunchDesc);
/*     */       return;
/*     */     } 
/* 283 */     ShortcutDesc shortcutDesc = paramLaunchDesc.getInformation().getShortcut();
/*     */     
/* 285 */     boolean bool1 = (shortcutDesc == null) ? true : shortcutDesc.getDesktop();
/* 286 */     if (bool1 && 
/* 287 */       !handleInstall(paramLaunchDesc, paramLocalApplicationProperties, str, 1)) {
/* 288 */       installFailed(paramLaunchDesc);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 293 */     boolean bool2 = (shortcutDesc == null) ? true : shortcutDesc.getMenu();
/* 294 */     if (bool2 && 
/* 295 */       !handleInstall(paramLaunchDesc, paramLocalApplicationProperties, str, 2)) {
/* 296 */       uninstall(paramLaunchDesc, paramLocalApplicationProperties, bool1);
/* 297 */       installFailed(paramLaunchDesc);
/*     */       
/*     */       return;
/*     */     } 
/* 301 */     if (bool2 || bool1) {
/* 302 */       paramLocalApplicationProperties.setLocallyInstalled(true);
/* 303 */       save(paramLocalApplicationProperties);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void installFailed(LaunchDesc paramLaunchDesc) {
/* 312 */     Runnable runnable = new Runnable(this, paramLaunchDesc) {
/*     */         public void run() {
/* 314 */           DialogFactory.showErrorDialog(ResourceManager.getString("install.installFailed", this.this$0.getInstallName(this.val$desc)), ResourceManager.getString("install.installFailedTitle"));
/*     */         }
/*     */         
/*     */         private final LaunchDesc val$desc;
/*     */         private final WinInstallHandler this$0;
/*     */       };
/* 320 */     invokeRunnable(runnable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void uninstallFailed(LaunchDesc paramLaunchDesc) {
/* 327 */     Runnable runnable = new Runnable(this, paramLaunchDesc) {
/*     */         public void run() {
/* 329 */           DialogFactory.showErrorDialog(ResourceManager.getString("install.uninstallFailed", this.this$0.getInstallName(this.val$desc)), ResourceManager.getString("install.uninstallFailedTitle"));
/*     */         }
/*     */         private final LaunchDesc val$desc;
/*     */         private final WinInstallHandler this$0;
/*     */       };
/* 334 */     invokeRunnable(runnable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean handleInstall(LaunchDesc paramLaunchDesc, LocalApplicationProperties paramLocalApplicationProperties, String paramString, int paramInt) {
/* 345 */     InformationDesc informationDesc = paramLaunchDesc.getInformation();
/* 346 */     ShortcutDesc shortcutDesc = informationDesc.getShortcut();
/* 347 */     String str1 = null;
/* 348 */     String str2 = null;
/* 349 */     String str3 = IcoEncoder.getIconPath(paramLaunchDesc);
/* 350 */     String str4 = Config.getInstance().getSystemJavawsPath();
/* 351 */     String str5 = informationDesc.getDescription(1);
/* 352 */     boolean bool1 = true;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 357 */     if (str3 == null) {
/* 358 */       str3 = getDefaultIconPath();
/*     */     }
/*     */     
/* 361 */     boolean bool2 = (!informationDesc.supportsOfflineOperation() || shortcutDesc == null || shortcutDesc.getOnline()) ? true : false;
/*     */ 
/*     */     
/* 364 */     String str6 = bool2 ? "" : "-offline ";
/* 365 */     String str7 = str6 + "\"" + paramString + "\"";
/* 366 */     int i = 0;
/*     */     
/* 368 */     if (paramInt == 1) {
/* 369 */       str1 = getDesktopPath(paramLaunchDesc);
/* 370 */       str2 = getDesktopName(paramLaunchDesc);
/*     */ 
/*     */       
/* 373 */       i = installWrapper(str1, str2, str5, str4, str7, (String)null, str3);
/*     */       
/* 375 */       if (i == 0) {
/* 376 */         paramLocalApplicationProperties.put("windows.installedDesktopShortcut", str1);
/* 377 */         Trace.println("Installed desktop shortcut for: " + str2 + ".", TraceLevel.TEMP);
/*     */       } else {
/*     */         
/* 380 */         bool1 = false;
/* 381 */         Trace.println("Installed desktop shortcut for: " + str2 + " failed (" + i + ")!!!", TraceLevel.TEMP);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 386 */       File file = new File(getSubMenuPath(paramLaunchDesc));
/* 387 */       if (file.exists() || file.mkdirs()) {
/* 388 */         str1 = getStartMenuPath(paramLaunchDesc);
/* 389 */         str2 = getStartMenuName(paramLaunchDesc);
/*     */ 
/*     */         
/* 392 */         i = installWrapper(str1, str2, str5, str4, str7, (String)null, str3);
/*     */         
/* 394 */         if (i == 0) {
/* 395 */           paramLocalApplicationProperties.put("windows.installedStartMenuShortcut", str1);
/* 396 */           Trace.println("Installed menu shortcut for: " + str2 + ".", TraceLevel.TEMP);
/*     */         } else {
/*     */           
/* 399 */           bool1 = false;
/* 400 */           Trace.println("Installed menu shortcut for: " + str2 + " failed (" + i + ")!!!", TraceLevel.TEMP);
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 406 */         String str = getSubMenuDir(paramLaunchDesc);
/* 407 */         if ((str == null || !str.equals("Startup")) && addUninstallShortcut()) {
/*     */           
/* 409 */           str7 = "-uninstall \"" + paramString + "\"";
/* 410 */           str1 = getUninstallPath(paramLaunchDesc);
/* 411 */           str2 = ResourceManager.getString("install.startMenuUninstallShortcutName", str2);
/*     */ 
/*     */ 
/*     */           
/* 415 */           i = installWrapper(str1, str2, str5, str4, str7, (String)null, str3);
/*     */           
/* 417 */           if (i == 0) {
/* 418 */             paramLocalApplicationProperties.put("windows.uninstalledStartMenuShortcut", str1);
/* 419 */             Trace.println("Installed menu shortcut for: " + str2 + ".", TraceLevel.TEMP);
/*     */           } else {
/*     */             
/* 422 */             bool1 = false;
/* 423 */             Trace.println("Installed menu shortcut for: " + str2 + " failed (" + i + ")!!!", TraceLevel.TEMP);
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 429 */         RContentDesc[] arrayOfRContentDesc = informationDesc.getRelatedContent();
/* 430 */         StringBuffer stringBuffer = new StringBuffer(200 * arrayOfRContentDesc.length);
/*     */         
/* 432 */         if (arrayOfRContentDesc != null) for (byte b = 0; b < arrayOfRContentDesc.length; b++) {
/* 433 */             str2 = arrayOfRContentDesc[b].getTitle().trim();
/* 434 */             if (str2 == null || str2.length() == 0) {
/* 435 */               str2 = getStartMenuName(paramLaunchDesc) + " #" + b;
/*     */             }
/* 437 */             str2 = getName(str2);
/* 438 */             URL uRL = arrayOfRContentDesc[b].getHref();
/* 439 */             if (!uRL.toString().endsWith("jnlp")) {
/* 440 */               str5 = arrayOfRContentDesc[b].getDescription();
/* 441 */               URL uRL1 = arrayOfRContentDesc[b].getIcon();
/* 442 */               String str8 = null;
/* 443 */               if (uRL1 != null) {
/* 444 */                 str8 = IcoEncoder.getIconPath(uRL1, null);
/*     */               }
/* 446 */               if (str8 == null) str8 = str3; 
/* 447 */               str1 = getRCPath(paramLaunchDesc, str2);
/* 448 */               File file1 = Cache.getCachedFile(uRL);
/*     */               
/* 450 */               str4 = (new WinBrowserSupport()).getDefaultHandler(uRL);
/*     */               
/* 452 */               if (file1 != null) {
/*     */ 
/*     */                 
/* 455 */                 str7 = "\"file:" + file1.getAbsolutePath() + "\"";
/*     */ 
/*     */                 
/* 458 */                 i = installWrapper(str1, str2, str5, str4, str7, (String)null, str8);
/*     */                 
/* 460 */                 if (i == 0) {
/* 461 */                   stringBuffer.append(str1);
/* 462 */                   stringBuffer.append(File.pathSeparator);
/* 463 */                   Trace.println("Installed menu shortcut for: " + str2 + ".", TraceLevel.TEMP);
/*     */                 } else {
/*     */                   
/* 466 */                   bool1 = false;
/* 467 */                   Trace.println("Installed menu shortcut for: " + str2 + " failed (" + i + ")!!!", TraceLevel.TEMP);
/*     */                 }
/*     */               
/*     */               }
/*     */               else {
/*     */                 
/* 473 */                 str7 = uRL.toString();
/*     */ 
/*     */                 
/* 476 */                 i = installWrapper(str1, str2, str5, str4, str7, (String)null, str8);
/*     */                 
/* 478 */                 if (i == 0) {
/* 479 */                   stringBuffer.append(str1);
/* 480 */                   stringBuffer.append(File.pathSeparator);
/* 481 */                   Trace.println("Installed menu shortcut for: " + str2 + ".", TraceLevel.TEMP);
/*     */                 } else {
/*     */                   
/* 484 */                   bool1 = false;
/* 485 */                   Trace.println("Installed menu shortcut for: " + str2 + " failed (" + i + ")!!!", TraceLevel.TEMP);
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */ 
/*     */ 
/*     */         
/* 493 */         if (stringBuffer.length() > 0) {
/* 494 */           paramLocalApplicationProperties.put("windows.RContent.shortcuts", stringBuffer.toString());
/*     */         } else {
/* 496 */           paramLocalApplicationProperties.put("windows.RContent.shortcuts", null);
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 502 */         bool1 = false;
/* 503 */         Trace.println("Installed menu shortcut for: " + str2 + " failed (can't create directory \"" + file.getAbsolutePath() + "\")!!!", TraceLevel.TEMP);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 510 */     return bool1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isApplicationInstalled(LaunchDesc paramLaunchDesc) {
/* 518 */     boolean bool1 = false;
/* 519 */     boolean bool2 = false;
/* 520 */     String str = null;
/*     */     
/* 522 */     str = getDesktopPath(paramLaunchDesc);
/* 523 */     Trace.println("getDesktopPath(" + str + ").exists() = " + ((str == null) ? "N/A" : ("" + (new File(str)).exists())), TraceLevel.TEMP);
/* 524 */     bool1 = (str == null) ? true : (new File(str)).exists();
/*     */     
/* 526 */     str = getStartMenuPath(paramLaunchDesc);
/* 527 */     Trace.println("startMenuInstalled(" + str + ").exists() = " + ((str == null) ? "N/A" : ("" + (new File(str)).exists())), TraceLevel.TEMP);
/* 528 */     bool2 = (str == null) ? true : (new File(str)).exists();
/*     */ 
/*     */ 
/*     */     
/* 532 */     return (bool1 && bool2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getInstallName(LaunchDesc paramLaunchDesc) {
/* 539 */     String str = paramLaunchDesc.getInformation().getTitle().trim();
/* 540 */     return getName(str);
/*     */   }
/*     */   
/*     */   private String getName(String paramString) {
/* 544 */     if (paramString.length() > 32) {
/* 545 */       paramString = paramString.substring(0, 32);
/*     */     }
/* 547 */     return paramString;
/*     */   }
/*     */   
/*     */   private String getDesktopName(LaunchDesc paramLaunchDesc) {
/* 551 */     return ResourceManager.getString("install.desktopShortcutName", getInstallName(paramLaunchDesc));
/*     */   }
/*     */ 
/*     */   
/*     */   private String getStartMenuName(LaunchDesc paramLaunchDesc) {
/* 556 */     return ResourceManager.getString("install.startMenuShortcutName", getInstallName(paramLaunchDesc));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getDesktopPath(LaunchDesc paramLaunchDesc) {
/* 565 */     String str = getDesktopPath();
/* 566 */     if (str != null) {
/* 567 */       String str1 = getDesktopName(paramLaunchDesc);
/* 568 */       if (str1 != null) {
/* 569 */         str = str + str1;
/*     */       }
/* 571 */       if (str.length() > 192)
/*     */       {
/* 573 */         str = str.substring(0, 192);
/*     */       }
/* 575 */       str = str + ".lnk";
/*     */     } 
/* 577 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getStartMenuPath(LaunchDesc paramLaunchDesc) {
/* 584 */     String str = getSubMenuPath(paramLaunchDesc);
/* 585 */     if (str != null) {
/* 586 */       String str1 = getStartMenuName(paramLaunchDesc);
/* 587 */       if (str1 != null) {
/* 588 */         str = str + str1;
/*     */       }
/* 590 */       if (str.length() > 192)
/*     */       {
/* 592 */         str = str.substring(0, 192);
/*     */       }
/* 594 */       str = str + ".lnk";
/*     */     } 
/* 596 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getRCPath(LaunchDesc paramLaunchDesc, String paramString) {
/* 603 */     String str = getSubMenuPath(paramLaunchDesc);
/* 604 */     if (str != null) {
/* 605 */       str = str + paramString;
/* 606 */       if (str.length() > 192)
/*     */       {
/* 608 */         str = str.substring(0, 192);
/*     */       }
/* 610 */       str = str + ".lnk";
/*     */     } 
/* 612 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getUninstallPath(LaunchDesc paramLaunchDesc) {
/* 619 */     String str = getSubMenuPath(paramLaunchDesc);
/* 620 */     if (str != null) {
/* 621 */       String str1 = "uninstall  " + getStartMenuName(paramLaunchDesc);
/* 622 */       str = str + str1;
/* 623 */       if (str.length() > 192)
/*     */       {
/* 625 */         str = str.substring(0, 192);
/*     */       }
/* 627 */       str = str + ".lnk";
/*     */     } 
/* 629 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getSubMenuPath(LaunchDesc paramLaunchDesc) {
/* 636 */     String str = getStartMenuPath();
/* 637 */     if (str != null) {
/* 638 */       String str1 = getSubMenuDir(paramLaunchDesc);
/* 639 */       if (str1 != null) {
/* 640 */         str = str + str1 + File.separator;
/*     */       }
/*     */     } 
/* 643 */     return str;
/*     */   }
/*     */   
/*     */   private String getSubMenuDir(LaunchDesc paramLaunchDesc) {
/* 647 */     String str = getStartMenuName(paramLaunchDesc);
/* 648 */     ShortcutDesc shortcutDesc = paramLaunchDesc.getInformation().getShortcut();
/* 649 */     if (shortcutDesc != null) {
/* 650 */       String str1 = shortcutDesc.getSubmenu();
/* 651 */       if (str1 != null) {
/* 652 */         str = str1;
/*     */       }
/*     */     } 
/* 655 */     if (str != null && 
/* 656 */       str.equalsIgnoreCase("startup")) {
/* 657 */       str = "Startup";
/*     */     }
/*     */     
/* 660 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getDesktopPath() {
/* 667 */     loadPathsIfNecessary();
/* 668 */     return this._desktopPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getStartMenuPath() {
/* 675 */     loadPathsIfNecessary();
/* 676 */     return this._startMenuPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadPathsIfNecessary() {
/* 684 */     int i = -2147483647;
/* 685 */     String str = "";
/*     */     
/* 687 */     if (Globals.isSystemCache()) {
/* 688 */       i = -2147483646;
/* 689 */       str = "Common ";
/*     */     } 
/*     */     
/* 692 */     if (!this._loadedPaths) {
/* 693 */       this._desktopPath = WinRegistry.getString(i, "Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders", str + "Desktop");
/*     */ 
/*     */       
/* 696 */       if (this._desktopPath != null && this._desktopPath.length() > 0 && this._desktopPath.charAt(this._desktopPath.length() - 1) != '\\')
/*     */       {
/* 698 */         this._desktopPath += '\\';
/*     */       }
/* 700 */       this._startMenuPath = WinRegistry.getString(i, "Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders", str + "Programs");
/*     */ 
/*     */       
/* 703 */       if (this._startMenuPath != null && this._startMenuPath.length() > 0 && this._startMenuPath.charAt(this._startMenuPath.length() - 1) != '\\')
/*     */       {
/* 705 */         this._startMenuPath += '\\';
/*     */       }
/* 707 */       this._loadedPaths = true;
/*     */       
/* 709 */       Trace.println("Start path: " + this._startMenuPath + " desktop " + this._desktopPath, TraceLevel.TEMP);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean uninstallShortcut(String paramString) {
/* 720 */     File file = new File(paramString);
/*     */     
/* 722 */     if (file.exists()) {
/* 723 */       return file.delete();
/*     */     }
/* 725 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int installWrapper(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7) {
/* 735 */     Trace.println("installshortcut with args:", TraceLevel.TEMP);
/* 736 */     Trace.println("    path: " + paramString1, TraceLevel.TEMP);
/* 737 */     Trace.println("    name: " + paramString2, TraceLevel.TEMP);
/* 738 */     Trace.println("    desc: " + paramString3, TraceLevel.TEMP);
/* 739 */     Trace.println("    appP: " + paramString4, TraceLevel.TEMP);
/* 740 */     Trace.println("    args: " + paramString5, TraceLevel.TEMP);
/* 741 */     Trace.println("    dir : " + paramString6, TraceLevel.TEMP);
/* 742 */     Trace.println("    icon: " + paramString7, TraceLevel.TEMP);
/* 743 */     Trace.flush();
/*     */     
/* 745 */     return Config.getInstance().installShortcut(paramString1, paramString2, paramString3, paramString4, paramString5, paramString6, paramString7);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\WinInstallHandler.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */