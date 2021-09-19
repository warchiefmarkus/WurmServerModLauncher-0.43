/*     */ package com.sun.javaws;
/*     */ 
/*     */ import com.sun.deploy.association.Action;
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
/*     */ import com.sun.javaws.cache.Cache;
/*     */ import com.sun.javaws.jnl.AssociationDesc;
/*     */ import com.sun.javaws.jnl.LaunchDesc;
/*     */ import com.sun.javaws.jnl.RContentDesc;
/*     */ import com.sun.javaws.jnl.ShortcutDesc;
/*     */ import com.sun.javaws.ui.DesktopIntegration;
/*     */ import java.awt.Frame;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.StringTokenizer;
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
/*     */ public abstract class LocalInstallHandler
/*     */ {
/*     */   private static LocalInstallHandler _installHandler;
/*     */   
/*     */   public static synchronized LocalInstallHandler getInstance() {
/*  55 */     if (_installHandler == null) {
/*  56 */       _installHandler = LocalInstallHandlerFactory.newInstance();
/*     */     }
/*  58 */     return _installHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract void install(LaunchDesc paramLaunchDesc, LocalApplicationProperties paramLocalApplicationProperties);
/*     */ 
/*     */   
/*     */   public abstract void uninstall(LaunchDesc paramLaunchDesc, LocalApplicationProperties paramLocalApplicationProperties, boolean paramBoolean);
/*     */ 
/*     */   
/*     */   public abstract boolean isLocalInstallSupported();
/*     */ 
/*     */   
/*     */   public abstract boolean isAssociationSupported();
/*     */ 
/*     */   
/*     */   public abstract void associationCompleted();
/*     */ 
/*     */   
/*     */   public abstract String getAssociationOpenCommand(String paramString);
/*     */   
/*     */   public abstract String getAssociationPrintCommand(String paramString);
/*     */   
/*     */   public abstract void registerAssociationInternal(Association paramAssociation) throws AssociationAlreadyRegisteredException, RegisterFailedException;
/*     */   
/*     */   public abstract void unregisterAssociationInternal(Association paramAssociation) throws AssociationNotRegisteredException, RegisterFailedException;
/*     */   
/*     */   public abstract String getDefaultIconPath();
/*     */   
/*     */   private String getJnlpLocation(LaunchDesc paramLaunchDesc) {
/*     */     String str;
/*  89 */     File file = null;
/*     */     try {
/*  91 */       file = Cache.getCachedLaunchedFile(paramLaunchDesc.getCanonicalHome());
/*  92 */     } catch (IOException iOException) {}
/*     */     
/*  94 */     if (file != null) {
/*  95 */       str = file.getAbsolutePath();
/*     */     } else {
/*  97 */       str = paramLaunchDesc.getLocation().toString();
/*     */     } 
/*  99 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean promptUserAssociation(LaunchDesc paramLaunchDesc, Association paramAssociation, boolean paramBoolean1, String paramString, boolean paramBoolean2, Frame paramFrame) {
/* 107 */     if (paramBoolean2) return true;
/*     */ 
/*     */     
/* 110 */     String str1 = "";
/* 111 */     String str2 = paramAssociation.getMimeType();
/* 112 */     ArrayList arrayList = (ArrayList)paramAssociation.getFileExtList();
/* 113 */     String str3 = "";
/*     */     
/* 115 */     if (arrayList != null) {
/* 116 */       Iterator iterator = arrayList.iterator();
/* 117 */       while (iterator.hasNext()) {
/* 118 */         str3 = str3 + iterator.next();
/* 119 */         if (iterator.hasNext()) {
/* 120 */           str3 = str3 + ", ";
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 125 */     if (paramBoolean1) {
/*     */       
/* 127 */       str1 = ResourceManager.getString("javaws.association.dialog.existAsk") + "\n\n";
/* 128 */       if (str3 != "") {
/* 129 */         str1 = str1 + ResourceManager.getString("javaws.association.dialog.ext", str3) + "\n";
/*     */       }
/* 131 */       if (str2 != null) {
/* 132 */         str1 = str1 + ResourceManager.getString("javaws.association.dialog.mime", str2) + "\n";
/*     */       }
/* 134 */       if (paramString == null) {
/* 135 */         str1 = str1 + "\n" + ResourceManager.getString("javaws.association.dialog.exist");
/*     */       } else {
/* 137 */         str1 = str1 + "\n" + ResourceManager.getString("javaws.association.dialog.exist.command", paramString);
/*     */       } 
/* 139 */       str1 = str1 + "\n" + ResourceManager.getString("javaws.association.dialog.askReplace", paramLaunchDesc.getInformation().getTitle());
/*     */     } else {
/*     */       
/* 142 */       str1 = ResourceManager.getString("javaws.association.dialog.ask", paramLaunchDesc.getInformation().getTitle()) + "\n";
/* 143 */       if (str3 != "") {
/* 144 */         str1 = str1 + ResourceManager.getString("javaws.association.dialog.ext", str3) + "\n";
/*     */       }
/* 146 */       if (str2 != null) {
/* 147 */         str1 = str1 + ResourceManager.getString("javaws.association.dialog.mime", str2) + "\n";
/*     */       }
/*     */     } 
/* 150 */     int i = 1;
/* 151 */     if (!paramBoolean2) {
/* 152 */       i = DialogFactory.showConfirmDialog(paramFrame, str1, ResourceManager.getString("javaws.association.dialog.title"));
/*     */     }
/*     */     
/* 155 */     if (i == 0) return true; 
/* 156 */     return false;
/*     */   }
/*     */   
/*     */   private String getOpenActionCommand(Association paramAssociation) {
/* 160 */     Action action = paramAssociation.getActionByVerb("open");
/* 161 */     String str = null;
/* 162 */     if (action != null) {
/* 163 */       str = action.getCommand();
/*     */     }
/* 165 */     return str;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean registerAssociation(LaunchDesc paramLaunchDesc, String paramString1, String paramString2, boolean paramBoolean, Frame paramFrame) {
/* 170 */     AssociationService associationService = new AssociationService();
/* 171 */     Association association1 = new Association();
/* 172 */     boolean bool = false;
/* 173 */     Association association2 = null;
/* 174 */     String str1 = "";
/* 175 */     String str2 = null;
/*     */     
/* 177 */     if (paramString1 != null) {
/* 178 */       StringTokenizer stringTokenizer = new StringTokenizer(paramString1);
/*     */ 
/*     */       
/* 181 */       while (stringTokenizer.hasMoreTokens()) {
/* 182 */         String str = "." + stringTokenizer.nextToken();
/* 183 */         Trace.println("associate with ext: " + str, TraceLevel.BASIC);
/* 184 */         if (str1 == "") {
/* 185 */           str1 = str + " file";
/*     */         }
/* 187 */         association2 = associationService.getFileExtensionAssociation(str);
/* 188 */         if (association2 != null) {
/* 189 */           Trace.println("associate with ext: " + str + " already EXIST", TraceLevel.BASIC);
/*     */           
/* 191 */           if (str2 == null) {
/* 192 */             str2 = getOpenActionCommand(association2);
/*     */           }
/* 194 */           bool = true;
/*     */         } 
/* 196 */         association1.addFileExtension(str);
/*     */       } 
/*     */     } 
/* 199 */     if (paramString2 != null) {
/* 200 */       Trace.println("associate with mime: " + paramString2, TraceLevel.BASIC);
/* 201 */       association2 = associationService.getMimeTypeAssociation(paramString2);
/* 202 */       if (association2 != null) {
/* 203 */         Trace.println("associate with mime: " + paramString2 + " already EXIST", TraceLevel.BASIC);
/*     */         
/* 205 */         if (str2 == null) {
/* 206 */           str2 = getOpenActionCommand(association2);
/*     */         }
/* 208 */         bool = true;
/*     */       } 
/* 210 */       association1.setMimeType(paramString2);
/*     */     } 
/* 212 */     association1.setName(paramLaunchDesc.getInformation().getTitle());
/* 213 */     association1.setDescription(str1);
/* 214 */     String str3 = IcoEncoder.getIconPath(paramLaunchDesc);
/*     */     
/* 216 */     if (str3 == null) {
/* 217 */       str3 = getDefaultIconPath();
/*     */     }
/*     */     
/* 220 */     association1.setIconFileName(str3);
/*     */ 
/*     */     
/* 223 */     String str4 = getJnlpLocation(paramLaunchDesc);
/*     */     
/* 225 */     String str5 = getAssociationOpenCommand(str4);
/*     */     
/* 227 */     String str6 = getAssociationPrintCommand(str4);
/*     */     
/* 229 */     Trace.println("register OPEN using: " + str5, TraceLevel.BASIC);
/* 230 */     Action action = new Action("open", str5, "open the file");
/* 231 */     association1.addAction(action);
/*     */     
/* 233 */     if (str6 != null) {
/* 234 */       Trace.println("register PRINT using: " + str6, TraceLevel.BASIC);
/* 235 */       action = new Action("print", str6, "print the file");
/* 236 */       association1.addAction(action);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 244 */       if (!Globals.createAssoc())
/* 245 */         switch (Config.getAssociationValue()) {
/*     */           case 0:
/* 247 */             return false;
/*     */ 
/*     */           
/*     */           case 1:
/* 251 */             if (bool) return false;
/*     */             
/*     */             break;
/*     */           
/*     */           case 2:
/* 256 */             if (!promptUserAssociation(paramLaunchDesc, association1, bool, str2, paramBoolean, paramFrame))
/*     */             {
/* 258 */               return false;
/*     */             }
/*     */             break;
/*     */           
/*     */           case 3:
/* 263 */             if (bool && 
/* 264 */               !promptUserAssociation(paramLaunchDesc, association1, bool, str2, paramBoolean, paramFrame))
/*     */             {
/* 266 */               return false;
/*     */             }
/*     */             break;
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           default:
/* 274 */             if (!promptUserAssociation(paramLaunchDesc, association1, bool, str2, paramBoolean, paramFrame))
/*     */             {
/* 276 */               return false;
/*     */             }
/*     */             break;
/*     */         }  
/* 280 */       registerAssociationInternal(association1);
/* 281 */     } catch (AssociationAlreadyRegisteredException associationAlreadyRegisteredException) {
/*     */ 
/*     */       
/*     */       try {
/* 285 */         unregisterAssociationInternal(association1);
/* 286 */         registerAssociationInternal(association1);
/* 287 */       } catch (AssociationNotRegisteredException associationNotRegisteredException) {
/* 288 */         Trace.ignoredException((Exception)associationNotRegisteredException);
/* 289 */         return false;
/* 290 */       } catch (AssociationAlreadyRegisteredException associationAlreadyRegisteredException1) {
/* 291 */         Trace.ignoredException((Exception)associationAlreadyRegisteredException1);
/* 292 */         return false;
/* 293 */       } catch (RegisterFailedException registerFailedException) {
/* 294 */         Trace.ignoredException((Exception)registerFailedException);
/* 295 */         return false;
/*     */       } 
/* 297 */     } catch (RegisterFailedException registerFailedException) {
/* 298 */       Trace.ignoredException((Exception)registerFailedException);
/* 299 */       return false;
/*     */     } 
/* 301 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private void unregisterAssociation(LaunchDesc paramLaunchDesc, String paramString1, String paramString2) {
/* 306 */     AssociationService associationService = new AssociationService();
/* 307 */     Association association = null;
/* 308 */     if (paramString2 != null) {
/* 309 */       StringTokenizer stringTokenizer = new StringTokenizer(paramString2);
/*     */       
/* 311 */       while (stringTokenizer.hasMoreTokens()) {
/* 312 */         String str = "." + stringTokenizer.nextToken();
/*     */         
/* 314 */         association = associationService.getFileExtensionAssociation(str);
/*     */         
/* 316 */         if (association != null) {
/* 317 */           association.setName(paramLaunchDesc.getInformation().getTitle());
/*     */           
/* 319 */           Trace.println("remove association with ext: " + str, TraceLevel.BASIC);
/*     */           
/*     */           try {
/* 322 */             unregisterAssociationInternal(association);
/* 323 */           } catch (AssociationNotRegisteredException associationNotRegisteredException) {
/* 324 */             Trace.ignoredException((Exception)associationNotRegisteredException);
/* 325 */           } catch (RegisterFailedException registerFailedException) {
/* 326 */             Trace.ignoredException((Exception)registerFailedException);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 332 */     if (paramString1 != null) {
/* 333 */       association = associationService.getMimeTypeAssociation(paramString1);
/*     */       
/* 335 */       if (association != null) {
/* 336 */         association.setName(paramLaunchDesc.getInformation().getTitle());
/*     */         
/* 338 */         Trace.println("remove association with mime: " + paramString1, TraceLevel.BASIC);
/*     */         
/*     */         try {
/* 341 */           unregisterAssociationInternal(association);
/* 342 */         } catch (AssociationNotRegisteredException associationNotRegisteredException) {
/* 343 */           Trace.ignoredException((Exception)associationNotRegisteredException);
/* 344 */         } catch (RegisterFailedException registerFailedException) {
/* 345 */           Trace.ignoredException((Exception)registerFailedException);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAssociations(LaunchDesc paramLaunchDesc, LocalApplicationProperties paramLocalApplicationProperties) {
/* 354 */     if (isAssociationSupported()) {
/* 355 */       AssociationDesc[] arrayOfAssociationDesc = paramLocalApplicationProperties.getAssociations();
/*     */ 
/*     */       
/* 358 */       if (arrayOfAssociationDesc != null) {
/* 359 */         for (byte b = 0; b < arrayOfAssociationDesc.length; b++) {
/* 360 */           String str1 = arrayOfAssociationDesc[b].getExtensions();
/* 361 */           String str2 = arrayOfAssociationDesc[b].getMimeType();
/*     */           
/* 363 */           unregisterAssociation(paramLaunchDesc, str2, str1);
/*     */         } 
/* 365 */         paramLocalApplicationProperties.setAssociations(null);
/* 366 */         associationCompleted();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void createAssociations(LaunchDesc paramLaunchDesc, LocalApplicationProperties paramLocalApplicationProperties, boolean paramBoolean, Frame paramFrame) {
/* 375 */     if (Config.getAssociationValue() == 0)
/* 376 */       return;  if (isAssociationSupported()) {
/* 377 */       AssociationDesc[] arrayOfAssociationDesc = paramLaunchDesc.getInformation().getAssociations();
/*     */ 
/*     */ 
/*     */       
/* 381 */       for (byte b = 0; b < arrayOfAssociationDesc.length; b++) {
/* 382 */         String str1 = arrayOfAssociationDesc[b].getExtensions();
/* 383 */         String str2 = arrayOfAssociationDesc[b].getMimeType();
/* 384 */         if (registerAssociation(paramLaunchDesc, str1, str2, paramBoolean, paramFrame)) {
/*     */           
/* 386 */           paramLocalApplicationProperties.addAssociation(arrayOfAssociationDesc[b]);
/* 387 */           save(paramLocalApplicationProperties);
/*     */         } 
/*     */       } 
/*     */       
/* 391 */       associationCompleted();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void installFromLaunch(LaunchDesc paramLaunchDesc, LocalApplicationProperties paramLocalApplicationProperties, boolean paramBoolean, Frame paramFrame) {
/* 404 */     ShortcutDesc shortcutDesc = paramLaunchDesc.getInformation().getShortcut();
/* 405 */     if (shortcutDesc != null && 
/* 406 */       !shortcutDesc.getDesktop() && !shortcutDesc.getMenu()) {
/*     */       return;
/*     */     }
/*     */     
/* 410 */     if (paramBoolean && 
/* 411 */       Globals.createShortcut()) {
/* 412 */       doInstall(paramLaunchDesc, paramLocalApplicationProperties);
/*     */       
/*     */       return;
/*     */     } 
/* 416 */     switch (Config.getShortcutValue()) {
/*     */       case 0:
/*     */         return;
/*     */       
/*     */       case 1:
/* 421 */         doInstall(paramLaunchDesc, paramLocalApplicationProperties);
/*     */         return;
/*     */       
/*     */       case 4:
/* 425 */         if (shortcutDesc != null) {
/* 426 */           doInstall(paramLaunchDesc, paramLocalApplicationProperties);
/*     */         }
/*     */         return;
/*     */       
/*     */       case 3:
/* 431 */         if (shortcutDesc == null) {
/*     */           return;
/*     */         }
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 438 */     if (paramLocalApplicationProperties.getAskedForInstall()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 443 */     if (paramBoolean) {
/*     */       return;
/*     */     }
/*     */     
/* 447 */     showDialog(paramLaunchDesc, paramLocalApplicationProperties, paramFrame);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void showDialog(LaunchDesc paramLaunchDesc, LocalApplicationProperties paramLocalApplicationProperties, Frame paramFrame) {
/* 454 */     int i = DesktopIntegration.showDTIDialog(paramFrame, paramLaunchDesc);
/*     */     
/* 456 */     switch (i) {
/*     */       case 1:
/* 458 */         doInstall(paramLaunchDesc, paramLocalApplicationProperties);
/*     */         return;
/*     */       case 0:
/* 461 */         paramLocalApplicationProperties.setAskedForInstall(true);
/*     */         return;
/*     */     } 
/* 464 */     paramLocalApplicationProperties.setAskedForInstall(false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void doInstall(LaunchDesc paramLaunchDesc, LocalApplicationProperties paramLocalApplicationProperties) {
/* 470 */     install(paramLaunchDesc, paramLocalApplicationProperties);
/* 471 */     paramLocalApplicationProperties.setAskedForInstall(true);
/* 472 */     RContentDesc[] arrayOfRContentDesc = paramLaunchDesc.getInformation().getRelatedContent();
/* 473 */     if (arrayOfRContentDesc != null) {
/* 474 */       for (byte b = 0; b < arrayOfRContentDesc.length; b++) {
/* 475 */         URL uRL = arrayOfRContentDesc[b].getHref();
/* 476 */         if (!"jar".equals(uRL.getProtocol()) && uRL.toString().endsWith(".jnlp")) {
/*     */           
/*     */           try {
/* 479 */             Main.importApp(uRL.toString());
/* 480 */           } catch (Exception exception) {
/* 481 */             Trace.ignoredException(exception);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean shouldInstallOverExisting(LaunchDesc paramLaunchDesc) {
/* 494 */     int[] arrayOfInt = { 1 };
/* 495 */     Runnable runnable = new Runnable(arrayOfInt, paramLaunchDesc) {
/*     */         public void run() {
/* 497 */           this.val$result[0] = DialogFactory.showConfirmDialog(ResourceManager.getString("install.alreadyInstalled", this.val$ld.getInformation().getTitle()), ResourceManager.getString("install.alreadyInstalledTitle"));
/*     */         }
/*     */ 
/*     */         
/*     */         private final int[] val$result;
/*     */         private final LaunchDesc val$ld;
/*     */       };
/* 504 */     if (!Globals.isSilentMode()) {
/* 505 */       invokeRunnable(runnable);
/*     */     }
/* 507 */     return (arrayOfInt[0] == 0);
/*     */   }
/*     */   
/*     */   public static void invokeRunnable(Runnable paramRunnable) {
/* 511 */     if (SwingUtilities.isEventDispatchThread()) {
/* 512 */       paramRunnable.run();
/*     */     } else {
/*     */ 
/*     */       
/* 516 */       try { SwingUtilities.invokeAndWait(paramRunnable); }
/* 517 */       catch (InterruptedException interruptedException) {  }
/* 518 */       catch (InvocationTargetException invocationTargetException) {}
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void save(LocalApplicationProperties paramLocalApplicationProperties) {
/*     */     try {
/* 524 */       paramLocalApplicationProperties.store();
/* 525 */     } catch (IOException iOException) {
/* 526 */       Trace.ignoredException(iOException);
/*     */     } 
/*     */   }
/*     */   public boolean addUninstallShortcut() {
/* 530 */     if (Config.getBooleanProperty("deployment.javaws.uninstall.shortcut") && !Globals.isSystemCache())
/*     */     {
/* 532 */       return true;
/*     */     }
/* 534 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\LocalInstallHandler.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */