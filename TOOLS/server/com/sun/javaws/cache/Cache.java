/*     */ package com.sun.javaws.cache;
/*     */ 
/*     */ import com.sun.deploy.config.Config;
/*     */ import com.sun.deploy.config.JREInfo;
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.deploy.util.TraceLevel;
/*     */ import com.sun.javaws.Globals;
/*     */ import com.sun.javaws.Launcher;
/*     */ import com.sun.javaws.LocalApplicationProperties;
/*     */ import com.sun.javaws.LocalInstallHandler;
/*     */ import com.sun.javaws.SplashScreen;
/*     */ import com.sun.javaws.exceptions.ExitException;
/*     */ import com.sun.javaws.jnl.IconDesc;
/*     */ import com.sun.javaws.jnl.InformationDesc;
/*     */ import com.sun.javaws.jnl.LaunchDesc;
/*     */ import com.sun.javaws.jnl.LaunchDescFactory;
/*     */ import com.sun.javaws.jnl.RContentDesc;
/*     */ import com.sun.javaws.util.VersionID;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FilenameFilter;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Properties;
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
/*     */ public class Cache
/*     */ {
/*     */   public static final char RESOURCE_TYPE = 'R';
/*     */   public static final char APPLICATION_TYPE = 'A';
/*     */   public static final char EXTENSION_TYPE = 'E';
/*     */   public static final char MUFFIN_TYPE = 'P';
/*     */   public static final char MUFFIN_TAG_INDEX = '\000';
/*     */   public static final char MUFFIN_MAXSIZE_INDEX = '\001';
/*  55 */   private static DiskCache _activeCache = null;
/*  56 */   private static DiskCache _readOnlyCache = null;
/*  57 */   private static DiskCache _muffincache = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String LAST_ACCESSED_FILE_NAME = "lastAccessed";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String INDIRECT_EXTENSION = ".ind";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static HashMap _loadedProperties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  79 */     initialize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initialize() {
/*  88 */     DiskCache diskCache1 = new DiskCache(getUserBaseDir());
/*  89 */     DiskCache diskCache2 = null;
/*  90 */     File file1 = getSysBaseDir();
/*  91 */     if (file1 != null) {
/*  92 */       diskCache2 = new DiskCache(file1);
/*     */     }
/*  94 */     File file2 = getMuffinCacheBaseDir();
/*  95 */     _muffincache = new DiskCache(file2);
/*  96 */     _loadedProperties = new HashMap();
/*     */     
/*  98 */     if (diskCache2 != null && Globals.isSystemCache()) {
/*  99 */       _readOnlyCache = null;
/* 100 */       _activeCache = diskCache2;
/*     */     } else {
/* 102 */       _readOnlyCache = diskCache2;
/* 103 */       _activeCache = diskCache1;
/* 104 */       if (Globals.isSystemCache()) {
/* 105 */         Globals.setSystemCache(false);
/* 106 */         Trace.println("There is no system cache configured, \"-system\" option ignored");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 112 */   private static final char[] cacheTypes = new char[] { 'D', 'X', 'V', 'I', 'R', 'A', 'E', 'P' };
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
/*     */   public static boolean canWrite() {
/* 124 */     return _activeCache.canWrite();
/*     */   }
/*     */   
/*     */   public static void updateCache() {
/* 128 */     String str1 = Config.getProperty("deployment.javaws.cachedir");
/* 129 */     String str2 = Config.getProperty("deployment.user.cachedir") + File.separator + "javaws";
/*     */     
/* 131 */     File file1 = new File(str1);
/* 132 */     File file2 = new File(str2);
/*     */ 
/*     */ 
/*     */     
/* 136 */     Iterator iterator = _activeCache.getOrphans();
/* 137 */     while (iterator.hasNext()) {
/* 138 */       DiskCacheEntry diskCacheEntry = iterator.next();
/*     */     }
/*     */     
/* 141 */     iterator = _activeCache.getJnlpCacheEntries();
/* 142 */     ArrayList arrayList = new ArrayList();
/* 143 */     LocalInstallHandler localInstallHandler = LocalInstallHandler.getInstance();
/* 144 */     while (iterator.hasNext()) {
/* 145 */       DiskCacheEntry diskCacheEntry = iterator.next();
/* 146 */       LaunchDesc launchDesc = null;
/* 147 */       LocalApplicationProperties localApplicationProperties = null;
/*     */       try {
/* 149 */         launchDesc = LaunchDescFactory.buildDescriptor(diskCacheEntry.getFile());
/* 150 */         localApplicationProperties = getLocalApplicationProperties(diskCacheEntry, launchDesc);
/* 151 */         if (localApplicationProperties != null && localApplicationProperties.isLocallyInstalled()) {
/* 152 */           localInstallHandler.uninstall(launchDesc, localApplicationProperties, true);
/* 153 */           arrayList.add(diskCacheEntry);
/*     */         } 
/* 155 */       } catch (Exception exception) {
/* 156 */         Trace.ignoredException(exception);
/*     */       } 
/*     */     } 
/*     */     
/* 160 */     boolean bool = (str1.startsWith(str2) || str2.startsWith(str1)) ? true : false;
/*     */     
/* 162 */     if (!bool && file1.exists() && file1.isDirectory()) {
/* 163 */       copy(file1, file2, new FilenameFilter(file1) { private final File val$oldFile;
/*     */             public boolean accept(File param1File, String param1String) {
/* 165 */               if (param1File.equals(this.val$oldFile) || param1File.getParentFile().equals(this.val$oldFile))
/*     */               {
/*     */                 
/* 168 */                 return !param1String.equals("splashes");
/*     */               }
/* 170 */               if (param1String.length() == 0) {
/* 171 */                 return false;
/*     */               }
/* 173 */               char c = param1String.charAt(0);
/* 174 */               for (byte b = 0; b < Cache.cacheTypes.length; b++) {
/* 175 */                 if (c == Cache.cacheTypes[b]) {
/* 176 */                   return true;
/*     */                 }
/*     */               } 
/* 179 */               return false;
/*     */             } }
/*     */         );
/*     */     }
/* 183 */     Config.setProperty("deployment.javaws.cachedir", null);
/* 184 */     Config.storeIfDirty();
/* 185 */     synchronized (Cache.class) {
/* 186 */       initialize();
/*     */     } 
/*     */ 
/*     */     
/* 190 */     iterator = arrayList.iterator();
/* 191 */     while (iterator.hasNext()) {
/*     */       try {
/* 193 */         LaunchDesc launchDesc = null;
/* 194 */         LocalApplicationProperties localApplicationProperties = null;
/* 195 */         DiskCacheEntry diskCacheEntry = iterator.next();
/* 196 */         launchDesc = LaunchDescFactory.buildDescriptor(diskCacheEntry.getFile());
/* 197 */         localApplicationProperties = getLocalApplicationProperties(diskCacheEntry, launchDesc);
/* 198 */         localInstallHandler.doInstall(launchDesc, localApplicationProperties);
/* 199 */       } catch (Exception exception) {
/* 200 */         Trace.ignoredException(exception);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void copy(File paramFile1, File paramFile2, FilenameFilter paramFilenameFilter) {
/* 207 */     if (paramFile1.isDirectory()) {
/* 208 */       paramFile2.mkdirs();
/* 209 */       File[] arrayOfFile = paramFile1.listFiles(paramFilenameFilter);
/* 210 */       for (byte b = 0; b < arrayOfFile.length; b++) {
/* 211 */         copy(arrayOfFile[b], new File(paramFile2.getPath() + File.separator + arrayOfFile[b].getName()), paramFilenameFilter);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 216 */       byte[] arrayOfByte = new byte[1024];
/* 217 */       FileOutputStream fileOutputStream = null;
/* 218 */       FileInputStream fileInputStream = null;
/*     */       try {
/* 220 */         fileOutputStream = new FileOutputStream(paramFile2);
/* 221 */         fileInputStream = new FileInputStream(paramFile1);
/*     */         while (true) {
/* 223 */           int i = fileInputStream.read(arrayOfByte);
/* 224 */           if (i == -1)
/* 225 */             break;  fileOutputStream.write(arrayOfByte, 0, i);
/*     */         } 
/* 227 */       } catch (Exception exception) {
/* 228 */         Trace.ignoredException(exception);
/*     */       } finally {
/*     */         try {
/* 231 */           if (fileOutputStream != null) fileOutputStream.close(); 
/* 232 */         } catch (Exception exception) {}
/*     */         try {
/* 234 */           if (fileInputStream != null) fileInputStream.close(); 
/* 235 */         } catch (Exception exception) {}
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void saveRemovedApp(URL paramURL, String paramString) {
/* 241 */     Properties properties = getRemovedApps();
/* 242 */     properties.setProperty(paramURL.toString(), paramString);
/* 243 */     setRemovedApps(properties);
/*     */   }
/*     */   
/*     */   public static void setRemovedApps(Properties paramProperties) {
/*     */     try {
/* 248 */       FileOutputStream fileOutputStream = new FileOutputStream(getRemovePath());
/* 249 */       paramProperties.store(fileOutputStream, "Removed JNLP Applications");
/* 250 */     } catch (IOException iOException) {}
/*     */   }
/*     */   
/*     */   public static Properties getRemovedApps() {
/* 254 */     Properties properties = new Properties();
/*     */     try {
/* 256 */       FileInputStream fileInputStream = new FileInputStream(getRemovePath());
/* 257 */       properties.load(fileInputStream);
/* 258 */     } catch (IOException iOException) {}
/* 259 */     return properties;
/*     */   }
/*     */   
/*     */   public static String getRemovePath() {
/* 263 */     return Config.getJavawsCacheDir() + File.separator + "removed.apps";
/*     */   }
/*     */   
/*     */   private static File getMuffinCacheBaseDir() {
/* 267 */     String str = Config.getJavawsCacheDir() + File.separator + "muffins";
/* 268 */     File file = new File(str);
/* 269 */     if (!file.exists()) {
/* 270 */       file.mkdirs();
/*     */     }
/* 272 */     Trace.println("Muffin Cache = " + file, TraceLevel.CACHE);
/* 273 */     return file;
/*     */   }
/*     */   
/*     */   private static File getUserBaseDir() {
/* 277 */     String str = Config.getJavawsCacheDir();
/* 278 */     File file = new File(str);
/* 279 */     if (!file.exists()) {
/* 280 */       file.mkdirs();
/*     */     }
/* 282 */     Trace.println("User cache dir = " + file, TraceLevel.CACHE);
/* 283 */     return file;
/*     */   }
/*     */   
/*     */   private static File getSysBaseDir() {
/* 287 */     String str = Config.getSystemCacheDirectory();
/* 288 */     if (str == null || str.length() == 0) {
/* 289 */       return null;
/*     */     }
/* 291 */     File file = new File(str + File.separator + "javaws");
/* 292 */     if (!file.exists()) {
/* 293 */       file.mkdirs();
/*     */     }
/* 295 */     Trace.println("System cache dir = " + file, TraceLevel.CACHE);
/* 296 */     return file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void remove() {
/* 303 */     Iterator iterator = _activeCache.getJnlpCacheEntries();
/* 304 */     while (iterator.hasNext()) {
/* 305 */       DiskCacheEntry diskCacheEntry = iterator.next();
/* 306 */       LaunchDesc launchDesc = null;
/*     */       try {
/* 308 */         launchDesc = LaunchDescFactory.buildDescriptor(diskCacheEntry.getFile());
/* 309 */       } catch (Exception exception) {
/* 310 */         Trace.ignoredException(exception);
/*     */       } 
/* 312 */       if (launchDesc != null) {
/* 313 */         LocalApplicationProperties localApplicationProperties = getLocalApplicationProperties(diskCacheEntry, launchDesc);
/*     */         
/* 315 */         remove(diskCacheEntry, localApplicationProperties, launchDesc);
/*     */       } 
/*     */     } 
/* 318 */     uninstallActiveCache();
/* 319 */     uninstallMuffinCache();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void remove(String paramString, LocalApplicationProperties paramLocalApplicationProperties, LaunchDesc paramLaunchDesc) {
/*     */     try {
/* 325 */       DiskCacheEntry diskCacheEntry = getCacheEntryFromFile(new File(paramString));
/* 326 */       remove(diskCacheEntry, paramLocalApplicationProperties, paramLaunchDesc);
/* 327 */     } catch (Exception exception) {
/* 328 */       Trace.ignoredException(exception);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void remove(DiskCacheEntry paramDiskCacheEntry, LocalApplicationProperties paramLocalApplicationProperties, LaunchDesc paramLaunchDesc) {
/* 337 */     InformationDesc informationDesc = paramLaunchDesc.getInformation();
/* 338 */     LocalInstallHandler localInstallHandler = LocalInstallHandler.getInstance();
/*     */ 
/*     */     
/* 341 */     if (paramLaunchDesc.isApplicationDescriptor() && paramLaunchDesc.getLocation() != null) {
/* 342 */       saveRemovedApp(paramLaunchDesc.getLocation(), informationDesc.getTitle());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 347 */     paramLocalApplicationProperties.refresh();
/* 348 */     if (paramLocalApplicationProperties.isLocallyInstalled()) {
/* 349 */       if (paramLaunchDesc.isApplicationDescriptor()) {
/* 350 */         if (localInstallHandler != null) {
/* 351 */           localInstallHandler.uninstall(paramLaunchDesc, paramLocalApplicationProperties, true);
/*     */         }
/* 353 */       } else if (paramLaunchDesc.isInstaller()) {
/* 354 */         ArrayList arrayList = new ArrayList();
/* 355 */         arrayList.add(paramDiskCacheEntry.getFile());
/*     */         try {
/* 357 */           String str1 = paramLocalApplicationProperties.getInstallDirectory();
/* 358 */           Launcher.executeUninstallers(arrayList);
/* 359 */           JREInfo.removeJREsIn(str1);
/* 360 */           deleteFile(new File(str1));
/* 361 */         } catch (ExitException exitException) {
/* 362 */           Trace.ignoredException((Exception)exitException);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 368 */     String str = paramLocalApplicationProperties.getRegisteredTitle();
/* 369 */     Config.getInstance().addRemoveProgramsRemove(str, Globals.isSystemCache());
/*     */ 
/*     */ 
/*     */     
/* 373 */     localInstallHandler.removeAssociations(paramLaunchDesc, paramLocalApplicationProperties);
/*     */ 
/*     */     
/* 376 */     SplashScreen.removeCustomSplash(paramLaunchDesc);
/*     */ 
/*     */     
/* 379 */     if (informationDesc != null) {
/* 380 */       IconDesc[] arrayOfIconDesc = informationDesc.getIcons();
/* 381 */       if (arrayOfIconDesc != null) {
/* 382 */         for (byte b = 0; b < arrayOfIconDesc.length; b++) {
/* 383 */           URL uRL1 = arrayOfIconDesc[b].getLocation();
/* 384 */           String str1 = arrayOfIconDesc[b].getVersion();
/* 385 */           removeEntries('R', uRL1, str1);
/*     */         } 
/*     */       }
/* 388 */       RContentDesc[] arrayOfRContentDesc = informationDesc.getRelatedContent();
/* 389 */       if (arrayOfRContentDesc != null) for (byte b = 0; b < arrayOfRContentDesc.length; b++) {
/* 390 */           URL uRL1 = arrayOfRContentDesc[b].getIcon();
/* 391 */           if (uRL1 != null) {
/* 392 */             removeEntries('R', uRL1, null);
/*     */           }
/*     */         } 
/*     */     
/*     */     } 
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
/* 411 */     URL uRL = paramLaunchDesc.getCanonicalHome();
/*     */     
/* 413 */     if (uRL != null) {
/* 414 */       removeEntries('A', uRL, null);
/* 415 */       removeEntries('E', uRL, null);
/*     */     } 
/* 417 */     if (paramDiskCacheEntry != null) {
/* 418 */       removeEntry(paramDiskCacheEntry);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void deleteFile(File paramFile) {
/* 424 */     if (paramFile.isDirectory()) {
/* 425 */       File[] arrayOfFile = paramFile.listFiles();
/* 426 */       if (arrayOfFile != null) {
/* 427 */         for (byte b = 0; b < arrayOfFile.length; b++) {
/* 428 */           deleteFile(arrayOfFile[b]);
/*     */         }
/*     */       }
/*     */     } 
/* 432 */     paramFile.delete();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void removeEntries(char paramChar, URL paramURL, String paramString) {
/* 438 */     if (paramURL == null)
/*     */       return;  try {
/* 440 */       DiskCacheEntry[] arrayOfDiskCacheEntry = getCacheEntries(paramChar, paramURL, paramString, true);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 445 */       for (byte b = 0; b < arrayOfDiskCacheEntry.length; b++) {
/* 446 */         removeEntry(arrayOfDiskCacheEntry[b]);
/*     */       }
/*     */     }
/* 449 */     catch (IOException iOException) {
/* 450 */       Trace.ignoredException(iOException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static File getCachedLaunchedFile(URL paramURL) throws IOException {
/* 459 */     DiskCacheEntry diskCacheEntry = getCacheEntry('A', paramURL, null);
/* 460 */     return (diskCacheEntry == null) ? null : diskCacheEntry.getFile();
/*     */   }
/*     */   
/*     */   public static File getCachedFile(URL paramURL) {
/* 464 */     File file = null;
/* 465 */     if (paramURL.getProtocol().equals("jar")) {
/* 466 */       String str = paramURL.getPath();
/* 467 */       int i = str.indexOf("!/");
/* 468 */       if (i > 0)
/* 469 */         try { String str1 = str.substring(i + 2);
/* 470 */           URL uRL = new URL(str.substring(0, i));
/*     */           
/* 472 */           File file1 = createNativeLibDir(uRL, null);
/* 473 */           return new File(file1, str1); }
/* 474 */         catch (MalformedURLException malformedURLException)
/* 475 */         { Trace.ignoredException(malformedURLException); }
/* 476 */         catch (IOException iOException)
/* 477 */         { Trace.ignoredException(iOException); }
/*     */          
/* 479 */       return null;
/* 480 */     }  if (paramURL.toString().endsWith(".jnlp")) {
/*     */       try {
/* 482 */         file = getCachedLaunchedFile(paramURL);
/* 483 */       } catch (IOException iOException) {
/* 484 */         Trace.ignoredException(iOException);
/*     */       } 
/*     */     }
/* 487 */     return file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LocalApplicationProperties getLocalApplicationProperties(DiskCacheEntry paramDiskCacheEntry, LaunchDesc paramLaunchDesc) {
/* 496 */     return getLocalApplicationProperties(paramDiskCacheEntry.getLocation(), paramDiskCacheEntry.getVersionId(), paramLaunchDesc, (paramDiskCacheEntry.getType() == 'A'));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LocalApplicationProperties getLocalApplicationProperties(String paramString, LaunchDesc paramLaunchDesc) {
/* 506 */     DiskCacheEntry diskCacheEntry = getCacheEntryFromFile(new File(paramString));
/* 507 */     if (diskCacheEntry == null) {
/* 508 */       return null;
/*     */     }
/* 510 */     return getLocalApplicationProperties(diskCacheEntry.getLocation(), diskCacheEntry.getVersionId(), paramLaunchDesc, (diskCacheEntry.getType() == 'A'));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LocalApplicationProperties getLocalApplicationProperties(URL paramURL, LaunchDesc paramLaunchDesc) {
/* 521 */     return getLocalApplicationProperties(paramURL, null, paramLaunchDesc, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LocalApplicationProperties getLocalApplicationProperties(URL paramURL, String paramString, LaunchDesc paramLaunchDesc, boolean paramBoolean) {
/*     */     LocalApplicationProperties localApplicationProperties;
/* 532 */     if (paramURL == null) {
/* 533 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 538 */     String str = paramURL.toString().intern() + "?" + paramString;
/*     */ 
/*     */     
/* 541 */     synchronized (_loadedProperties) {
/* 542 */       localApplicationProperties = (LocalApplicationProperties)_loadedProperties.get(str);
/* 543 */       if (localApplicationProperties == null) {
/* 544 */         localApplicationProperties = new DefaultLocalApplicationProperties(paramURL, paramString, paramLaunchDesc, paramBoolean);
/*     */         
/* 546 */         _loadedProperties.put(str, localApplicationProperties);
/*     */       } else {
/* 548 */         localApplicationProperties.refreshIfNecessary();
/*     */       } 
/*     */     } 
/* 551 */     return localApplicationProperties;
/*     */   }
/*     */   
/*     */   public static LaunchDesc getLaunchDesc(URL paramURL, String paramString) {
/*     */     try {
/* 556 */       DiskCacheEntry diskCacheEntry = getCacheEntry('A', paramURL, paramString);
/*     */       
/* 558 */       if (diskCacheEntry != null) {
/*     */         try {
/* 560 */           return LaunchDescFactory.buildDescriptor(diskCacheEntry.getFile());
/* 561 */         } catch (Exception exception) {
/* 562 */           return null;
/*     */         } 
/*     */       }
/* 565 */     } catch (IOException iOException) {
/*     */       
/* 567 */       Trace.ignoredException(iOException);
/*     */     } 
/* 569 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getNewExtensionInstallDirectory() throws IOException {
/* 574 */     String str1 = getUserBaseDir().getAbsolutePath() + File.separator + "ext";
/*     */     
/* 576 */     String str2 = null;
/* 577 */     byte b = 0;
/*     */     do {
/* 579 */       str2 = str1 + File.separator + "E" + (new Date()).getTime() + File.separator;
/*     */ 
/*     */       
/* 582 */       File file = new File(str2);
/* 583 */       if (!file.mkdirs()) {
/* 584 */         str2 = null;
/*     */       }
/* 586 */       Thread.yield();
/* 587 */     } while (str2 == null && ++b < 50);
/* 588 */     if (str2 == null) {
/* 589 */       throw new IOException("Unable to create temp. dir for extension");
/*     */     }
/* 591 */     return str2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String createUniqueIndirectFile() throws IOException {
/* 599 */     String str = getUserBaseDir().getAbsolutePath() + File.separator + "indirect";
/*     */     
/* 601 */     File file1 = new File(str);
/*     */     
/* 603 */     file1.mkdirs();
/* 604 */     File file2 = File.createTempFile("indirect", ".ind", file1);
/*     */     
/* 606 */     return file2.getAbsolutePath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeEntry(DiskCacheEntry paramDiskCacheEntry) {
/* 617 */     _activeCache.removeEntry(paramDiskCacheEntry);
/*     */   }
/*     */   
/*     */   public static long getLastAccessed(boolean paramBoolean) {
/* 621 */     if (paramBoolean) {
/* 622 */       if (_readOnlyCache == null) return 0L; 
/* 623 */       return _readOnlyCache.getLastUpdate();
/*     */     } 
/* 625 */     return _activeCache.getLastUpdate();
/*     */   }
/*     */   
/*     */   public static long getLastAccessed() {
/* 629 */     return _activeCache.getLastUpdate();
/*     */   }
/*     */   
/*     */   public static void setLastAccessed() {
/* 633 */     _activeCache.recordLastUpdate();
/*     */   }
/*     */   
/*     */   public static String[] getBaseDirsForHost(URL paramURL) {
/*     */     String[] arrayOfString;
/* 638 */     if (_readOnlyCache == null) {
/* 639 */       arrayOfString = new String[1];
/* 640 */       arrayOfString[0] = _activeCache.getBaseDirForHost(paramURL);
/*     */     } else {
/* 642 */       arrayOfString = new String[2];
/* 643 */       arrayOfString[0] = _readOnlyCache.getBaseDirForHost(paramURL);
/* 644 */       arrayOfString[0] = _activeCache.getBaseDirForHost(paramURL);
/*     */     } 
/* 646 */     return arrayOfString;
/*     */   }
/*     */   
/*     */   public static long getCacheSize() throws IOException {
/* 650 */     return _activeCache.getCacheSize();
/*     */   }
/*     */   
/*     */   public static void clean() {
/* 654 */     _activeCache.cleanResources();
/*     */   }
/*     */   
/*     */   public static long getOrphanSize(boolean paramBoolean) {
/* 658 */     if (paramBoolean) {
/* 659 */       if (_readOnlyCache == null) return 0L; 
/* 660 */       return _readOnlyCache.getOrphanSize();
/*     */     } 
/* 662 */     return _activeCache.getOrphanSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void cleanResources() {
/* 667 */     _activeCache.cleanResources();
/*     */   }
/*     */   
/*     */   public static long getCacheSize(boolean paramBoolean) {
/*     */     try {
/* 672 */       if (paramBoolean) {
/* 673 */         if (_readOnlyCache == null) return -1L; 
/* 674 */         return _readOnlyCache.getCacheSize();
/*     */       } 
/* 676 */       return _activeCache.getCacheSize();
/*     */     }
/* 678 */     catch (Exception exception) {
/* 679 */       Trace.ignoredException(exception);
/*     */       
/* 681 */       return 0L;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String[] getCacheVersions(char paramChar, URL paramURL) throws IOException {
/* 686 */     String[] arrayOfString1 = _activeCache.getCacheVersions(paramChar, paramURL);
/* 687 */     String[] arrayOfString2 = new String[0];
/* 688 */     String[] arrayOfString3 = arrayOfString1;
/* 689 */     if (_readOnlyCache != null) {
/* 690 */       arrayOfString2 = _readOnlyCache.getCacheVersions(paramChar, paramURL);
/* 691 */       if (arrayOfString2.length > 0) {
/* 692 */         arrayOfString3 = new String[arrayOfString1.length + arrayOfString2.length];
/* 693 */         System.arraycopy(arrayOfString1, 0, arrayOfString3, 0, arrayOfString1.length);
/* 694 */         System.arraycopy(arrayOfString2, 0, arrayOfString3, arrayOfString1.length, arrayOfString2.length);
/*     */       } 
/*     */     } 
/*     */     
/* 698 */     if (arrayOfString3.length > 1) {
/* 699 */       Arrays.sort(arrayOfString3, new Comparator() {
/*     */             public int compare(Object param1Object1, Object param1Object2) {
/* 701 */               VersionID versionID1 = new VersionID((String)param1Object1);
/* 702 */               VersionID versionID2 = new VersionID((String)param1Object2);
/* 703 */               return versionID1.isGreaterThan(versionID2) ? -1 : 1;
/*     */             }
/*     */           });
/*     */     }
/* 707 */     return arrayOfString3;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static DiskCacheEntry[] getCacheEntries(char paramChar, URL paramURL, String paramString, boolean paramBoolean) throws IOException {
/* 713 */     DiskCacheEntry[] arrayOfDiskCacheEntry1 = _activeCache.getCacheEntries(paramChar, paramURL, paramString, paramBoolean);
/* 714 */     DiskCacheEntry[] arrayOfDiskCacheEntry2 = new DiskCacheEntry[0];
/* 715 */     if (_readOnlyCache != null) {
/* 716 */       arrayOfDiskCacheEntry2 = _readOnlyCache.getCacheEntries(paramChar, paramURL, paramString, paramBoolean);
/*     */     }
/*     */     
/* 719 */     if (arrayOfDiskCacheEntry2.length == 0) {
/* 720 */       return arrayOfDiskCacheEntry1;
/*     */     }
/* 722 */     int i = arrayOfDiskCacheEntry2.length + arrayOfDiskCacheEntry1.length;
/*     */     
/* 724 */     DiskCacheEntry[] arrayOfDiskCacheEntry3 = new DiskCacheEntry[i];
/* 725 */     byte b1 = 0;
/* 726 */     for (b1 = 0; b1 < arrayOfDiskCacheEntry2.length; b1++) {
/* 727 */       arrayOfDiskCacheEntry3[b1] = arrayOfDiskCacheEntry2[b1];
/*     */     }
/* 729 */     for (byte b2 = 0; b2 < arrayOfDiskCacheEntry1.length; b2++) {
/* 730 */       arrayOfDiskCacheEntry3[b1++] = arrayOfDiskCacheEntry1[b2];
/*     */     }
/* 732 */     return arrayOfDiskCacheEntry3;
/*     */   }
/*     */   
/*     */   public static DiskCacheEntry getMuffinCacheEntryFromFile(File paramFile) {
/* 736 */     return _muffincache.getCacheEntryFromFile(paramFile);
/*     */   }
/*     */   
/*     */   public static DiskCacheEntry getCacheEntryFromFile(File paramFile) {
/* 740 */     DiskCacheEntry diskCacheEntry = _activeCache.getCacheEntryFromFile(paramFile);
/* 741 */     if (_readOnlyCache != null) {
/* 742 */       DiskCacheEntry diskCacheEntry1 = _readOnlyCache.getCacheEntryFromFile(paramFile);
/* 743 */       if (diskCacheEntry1 != null && diskCacheEntry1.newerThan(diskCacheEntry)) {
/* 744 */         return diskCacheEntry1;
/*     */       }
/*     */     } 
/* 747 */     return diskCacheEntry;
/*     */   }
/*     */ 
/*     */   
/*     */   public static File getTempCacheFile(URL paramURL, String paramString) throws IOException {
/* 752 */     return _activeCache.getTempCacheFile(paramURL, paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public static DiskCacheEntry getCacheEntry(char paramChar, URL paramURL, String paramString) throws IOException {
/* 757 */     DiskCacheEntry diskCacheEntry = _activeCache.getCacheEntry(paramChar, paramURL, paramString);
/*     */     
/* 759 */     if (_readOnlyCache != null) {
/* 760 */       DiskCacheEntry diskCacheEntry1 = _readOnlyCache.getCacheEntry(paramChar, paramURL, paramString);
/*     */       
/* 762 */       if (diskCacheEntry1 != null && diskCacheEntry1.newerThan(diskCacheEntry)) {
/* 763 */         return diskCacheEntry1;
/*     */       }
/*     */     } 
/* 766 */     return diskCacheEntry;
/*     */   }
/*     */ 
/*     */   
/*     */   public static File createNativeLibDir(URL paramURL, String paramString) throws IOException {
/* 771 */     return _activeCache.createNativeLibDir(paramURL, paramString);
/*     */   }
/*     */   
/*     */   public static Iterator getJnlpCacheEntries(boolean paramBoolean) {
/* 775 */     if (paramBoolean) {
/* 776 */       if (_readOnlyCache == null) {
/* 777 */         return (new ArrayList()).iterator();
/*     */       }
/* 779 */       return _readOnlyCache.getJnlpCacheEntries();
/*     */     } 
/* 781 */     return _activeCache.getJnlpCacheEntries();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static File putMappedImage(URL paramURL, String paramString, File paramFile) throws IOException {
/* 787 */     return _activeCache.putMappedImage(paramURL, paramString, paramFile);
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] getLapData(char paramChar, URL paramURL, String paramString, boolean paramBoolean) throws IOException {
/* 792 */     if (paramBoolean) {
/* 793 */       return (_readOnlyCache == null) ? null : _readOnlyCache.getLapData(paramChar, paramURL, paramString);
/*     */     }
/*     */     
/* 796 */     return _activeCache.getLapData(paramChar, paramURL, paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void putLapData(char paramChar, URL paramURL, String paramString, byte[] paramArrayOfbyte) throws IOException {
/* 801 */     _activeCache.putLapData(paramChar, paramURL, paramString, paramArrayOfbyte);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void insertEntry(char paramChar, URL paramURL, String paramString, File paramFile, long paramLong) throws IOException {
/* 806 */     _activeCache.insertEntry(paramChar, paramURL, paramString, paramFile, paramLong);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void putCanonicalLaunchDesc(URL paramURL, LaunchDesc paramLaunchDesc) throws IOException {
/* 813 */     if (paramLaunchDesc.isApplicationDescriptor()) {
/* 814 */       File file = getTempCacheFile(paramURL, null);
/* 815 */       FileOutputStream fileOutputStream = new FileOutputStream(file);
/*     */       try {
/* 817 */         fileOutputStream.write(paramLaunchDesc.getSource().getBytes());
/*     */       } finally {
/* 819 */         fileOutputStream.close();
/*     */       } 
/* 821 */       insertEntry('A', paramURL, null, file, (new Date()).getTime());
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void uninstallActiveCache() {
/* 826 */     _activeCache.uninstallCache();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getMuffinSize(URL paramURL) throws IOException {
/* 837 */     return _muffincache.getMuffinSize(paramURL);
/*     */   }
/*     */   
/*     */   public static long[] getMuffinAttributes(URL paramURL) throws IOException {
/* 841 */     return _muffincache.getMuffinAttributes(paramURL);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void putMuffinAttributes(URL paramURL, int paramInt, long paramLong) throws IOException {
/* 846 */     _muffincache.putMuffinAttributes(paramURL, paramInt, paramLong);
/*     */   }
/*     */   
/*     */   public static URL[] getAccessibleMuffins(URL paramURL) throws IOException {
/* 850 */     return _muffincache.getAccessibleMuffins(paramURL);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void insertMuffinEntry(URL paramURL, File paramFile, int paramInt, long paramLong) throws IOException {
/* 855 */     _muffincache.insertMuffinEntry(paramURL, paramFile, paramInt, paramLong);
/*     */   }
/*     */   
/*     */   public static File getMuffinFileForURL(URL paramURL) {
/* 859 */     return _muffincache.getMuffinFileForURL(paramURL);
/*     */   }
/*     */ 
/*     */   
/*     */   public static DiskCacheEntry getMuffinEntry(char paramChar, URL paramURL) throws IOException {
/* 864 */     return _muffincache.getMuffinEntry(paramChar, paramURL);
/*     */   }
/*     */   
/*     */   public static boolean isMainMuffinFile(File paramFile) throws IOException {
/* 868 */     return _muffincache.isMainMuffinFile(paramFile);
/*     */   }
/*     */   
/*     */   public static void removeMuffinEntry(DiskCacheEntry paramDiskCacheEntry) {
/* 872 */     _muffincache.removeMuffinEntry(paramDiskCacheEntry);
/*     */   }
/*     */   
/*     */   public static void uninstallMuffinCache() {
/* 876 */     _muffincache.uninstallCache();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\cache\Cache.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */