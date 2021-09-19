/*      */ package com.sun.javaws;
/*      */ 
/*      */ import com.sun.deploy.config.Config;
/*      */ import com.sun.deploy.config.JREInfo;
/*      */ import com.sun.deploy.resources.ResourceManager;
/*      */ import com.sun.deploy.util.Trace;
/*      */ import com.sun.deploy.util.TraceLevel;
/*      */ import com.sun.javaws.cache.Cache;
/*      */ import com.sun.javaws.cache.DiskCacheEntry;
/*      */ import com.sun.javaws.cache.DownloadProtocol;
/*      */ import com.sun.javaws.exceptions.ErrorCodeResponseException;
/*      */ import com.sun.javaws.exceptions.FailedDownloadingResourceException;
/*      */ import com.sun.javaws.exceptions.JNLPException;
/*      */ import com.sun.javaws.exceptions.LaunchDescException;
/*      */ import com.sun.javaws.exceptions.MissingFieldException;
/*      */ import com.sun.javaws.exceptions.MultipleHostsException;
/*      */ import com.sun.javaws.exceptions.NativeLibViolationException;
/*      */ import com.sun.javaws.exceptions.UnsignedAccessViolationException;
/*      */ import com.sun.javaws.jnl.AppletDesc;
/*      */ import com.sun.javaws.jnl.ApplicationDesc;
/*      */ import com.sun.javaws.jnl.ExtensionDesc;
/*      */ import com.sun.javaws.jnl.IconDesc;
/*      */ import com.sun.javaws.jnl.InstallerDesc;
/*      */ import com.sun.javaws.jnl.JARDesc;
/*      */ import com.sun.javaws.jnl.JREDesc;
/*      */ import com.sun.javaws.jnl.LaunchDesc;
/*      */ import com.sun.javaws.jnl.LaunchDescFactory;
/*      */ import com.sun.javaws.jnl.PackageDesc;
/*      */ import com.sun.javaws.jnl.PropertyDesc;
/*      */ import com.sun.javaws.jnl.ResourceVisitor;
/*      */ import com.sun.javaws.jnl.ResourcesDesc;
/*      */ import com.sun.javaws.security.AppPolicy;
/*      */ import com.sun.javaws.security.SigningInfo;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.IOException;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.security.CodeSource;
/*      */ import java.security.cert.Certificate;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashSet;
/*      */ import java.util.jar.JarEntry;
/*      */ import java.util.jar.JarFile;
/*      */ import java.util.jar.Manifest;
/*      */ 
/*      */ public class LaunchDownload {
/*      */   private static boolean updateAvailable = false;
/*   52 */   private static int numThread = 0;
/*      */   
/*   54 */   private static Object syncObj = new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String SIGNED_JNLP_ENTRY = "JNLP-INF/APPLICATION.JNLP";
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
/*      */   private static boolean compareByteArray(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/*   76 */     if (paramArrayOfbyte1.length == paramArrayOfbyte2.length) {
/*   77 */       for (byte b = 0; b < paramArrayOfbyte1.length; b++) {
/*   78 */         if (paramArrayOfbyte1[b] != paramArrayOfbyte2[b]) {
/*   79 */           return false;
/*      */         }
/*      */       } 
/*   82 */       return true;
/*      */     } 
/*   84 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean updateLaunchDescInCache(LaunchDesc paramLaunchDesc) {
/*   90 */     URL uRL = paramLaunchDesc.getLocation();
/*   91 */     if (uRL == null) {
/*   92 */       uRL = paramLaunchDesc.getCanonicalHome();
/*      */     }
/*      */     
/*   95 */     if (uRL != null)
/*   96 */       try { DiskCacheEntry diskCacheEntry = DownloadProtocol.getCachedLaunchedFile(uRL);
/*   97 */         if (diskCacheEntry != null) {
/*   98 */           File file = diskCacheEntry.getFile();
/*   99 */           byte[] arrayOfByte1 = LaunchDescFactory.readBytes(new FileInputStream(file), file.length());
/*      */           
/*  101 */           byte[] arrayOfByte2 = paramLaunchDesc.getSource().getBytes();
/*  102 */           if (arrayOfByte1 != null && arrayOfByte2 != null && 
/*  103 */             compareByteArray(arrayOfByte1, arrayOfByte2)) {
/*  104 */             return false;
/*      */           }
/*      */         } 
/*      */         
/*  108 */         Cache.putCanonicalLaunchDesc(uRL, paramLaunchDesc);
/*  109 */         return true; }
/*  110 */       catch (JNLPException jNLPException)
/*  111 */       { Trace.ignoredException((Exception)jNLPException); }
/*  112 */       catch (IOException iOException)
/*  113 */       { Trace.ignoredException(iOException); }
/*      */        
/*  115 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static LaunchDesc getUpdatedLaunchDesc(LaunchDesc paramLaunchDesc) throws JNLPException, IOException {
/*  123 */     if (paramLaunchDesc.getLocation() == null) return null;
/*      */     
/*  125 */     boolean bool = DownloadProtocol.isLaunchFileUpdateAvailable(paramLaunchDesc.getLocation());
/*  126 */     if (!bool) return null;
/*      */ 
/*      */     
/*  129 */     Trace.println("Downloading updated JNLP descriptor from: " + paramLaunchDesc.getLocation(), TraceLevel.BASIC);
/*      */     
/*  131 */     DiskCacheEntry diskCacheEntry = DownloadProtocol.getLaunchFile(paramLaunchDesc.getLocation(), false);
/*      */     try {
/*  133 */       return LaunchDescFactory.buildDescriptor(diskCacheEntry.getFile());
/*  134 */     } catch (JNLPException jNLPException) {
/*  135 */       Cache.removeEntry(diskCacheEntry);
/*  136 */       throw jNLPException;
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
/*      */   public static boolean isInCache(LaunchDesc paramLaunchDesc) {
/*  149 */     ResourcesDesc resourcesDesc = paramLaunchDesc.getResources();
/*  150 */     if (resourcesDesc == null) return true;
/*      */ 
/*      */     
/*      */     try {
/*  154 */       if (paramLaunchDesc.getLocation() != null) {
/*  155 */         DiskCacheEntry diskCacheEntry = DownloadProtocol.getCachedLaunchedFile(paramLaunchDesc.getLocation());
/*  156 */         if (diskCacheEntry == null) return false;
/*      */       
/*      */       } 
/*      */       
/*  160 */       boolean bool = getCachedExtensions(paramLaunchDesc);
/*      */ 
/*      */       
/*  163 */       if (!bool) return false;
/*      */ 
/*      */       
/*  166 */       JARDesc[] arrayOfJARDesc = resourcesDesc.getEagerOrAllJarDescs(false);
/*  167 */       for (byte b = 0; b < arrayOfJARDesc.length; b++) {
/*  168 */         boolean bool1 = arrayOfJARDesc[b].isJavaFile() ? false : true;
/*  169 */         if (!DownloadProtocol.isInCache(arrayOfJARDesc[b].getLocation(), arrayOfJARDesc[b].getVersion(), bool1))
/*      */         {
/*      */           
/*  172 */           return false;
/*      */         }
/*      */       } 
/*  175 */     } catch (JNLPException jNLPException) {
/*  176 */       Trace.ignoredException((Exception)jNLPException);
/*      */       
/*  178 */       return false;
/*  179 */     } catch (IOException iOException) {
/*  180 */       Trace.ignoredException(iOException);
/*      */       
/*  182 */       return false;
/*      */     } 
/*  184 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void updateCheck(URL paramURL, String paramString, int paramInt, boolean paramBoolean) {
/*  189 */     synchronized (syncObj) {
/*  190 */       numThread++;
/*      */     } 
/*  192 */     (new Thread(new Runnable(paramURL, paramString, paramInt, paramBoolean) { private final URL val$url; private final String val$version; private final int val$type;
/*      */           private final boolean val$lazy;
/*      */           
/*      */           public void run() {
/*      */             try {
/*  197 */               boolean bool = DownloadProtocol.isUpdateAvailable(this.val$url, this.val$version, this.val$type);
/*  198 */               if (bool && this.val$lazy) {
/*      */ 
/*      */                 
/*  201 */                 File file = DownloadProtocol.getCachedVersion(this.val$url, this.val$version, this.val$type).getFile();
/*  202 */                 if (file != null) {
/*  203 */                   file.delete();
/*      */                 }
/*      */               } 
/*      */               
/*  207 */               synchronized (LaunchDownload.syncObj) {
/*  208 */                 if (bool && !LaunchDownload.updateAvailable) {
/*  209 */                   LaunchDownload.updateAvailable = true;
/*      */                 }
/*      */               } 
/*  212 */             } catch (JNLPException jNLPException) {
/*  213 */               Trace.ignoredException((Exception)jNLPException);
/*      */             } finally {
/*  215 */               synchronized (LaunchDownload.syncObj) {
/*      */                 LaunchDownload.numThread--;
/*      */               } 
/*      */             } 
/*      */           } }
/*      */       )).start();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isUpdateAvailable(LaunchDesc paramLaunchDesc) throws JNLPException {
/*  229 */     URL uRL = paramLaunchDesc.getLocation();
/*  230 */     if (uRL != null) {
/*  231 */       boolean bool = DownloadProtocol.isLaunchFileUpdateAvailable(uRL);
/*  232 */       if (bool) return true;
/*      */     
/*      */     } 
/*  235 */     ResourcesDesc resourcesDesc = paramLaunchDesc.getResources();
/*  236 */     if (resourcesDesc == null) return false;
/*      */ 
/*      */ 
/*      */     
/*  240 */     ExtensionDesc[] arrayOfExtensionDesc = resourcesDesc.getExtensionDescs();
/*      */     
/*  242 */     for (byte b1 = 0; b1 < arrayOfExtensionDesc.length; b1++) {
/*  243 */       URL uRL1 = arrayOfExtensionDesc[b1].getLocation();
/*  244 */       if (uRL1 != null)
/*      */       {
/*  246 */         updateCheck(uRL1, arrayOfExtensionDesc[b1].getVersion(), 4, false);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  254 */     JARDesc[] arrayOfJARDesc = resourcesDesc.getEagerOrAllJarDescs(true);
/*  255 */     for (byte b2 = 0; b2 < arrayOfJARDesc.length; b2++) {
/*  256 */       URL uRL1 = arrayOfJARDesc[b2].getLocation();
/*  257 */       String str = arrayOfJARDesc[b2].getVersion();
/*  258 */       boolean bool = arrayOfJARDesc[b2].isJavaFile() ? false : true;
/*  259 */       if (DownloadProtocol.isInCache(uRL1, str, bool))
/*      */       {
/*  261 */         updateCheck(uRL1, str, bool, arrayOfJARDesc[b2].isLazyDownload());
/*      */       }
/*      */     } 
/*      */     
/*  265 */     IconDesc[] arrayOfIconDesc = paramLaunchDesc.getInformation().getIcons();
/*  266 */     if (arrayOfIconDesc != null) for (byte b = 0; b < arrayOfIconDesc.length; b++) {
/*  267 */         URL uRL1 = arrayOfIconDesc[b].getLocation();
/*  268 */         String str = arrayOfIconDesc[b].getVersion();
/*  269 */         byte b3 = 2;
/*  270 */         if (DownloadProtocol.isInCache(uRL1, str, b3))
/*      */         {
/*  272 */           updateCheck(uRL1, str, b3, false);
/*      */         }
/*      */       } 
/*      */ 
/*      */     
/*  277 */     while (numThread > 0) {
/*  278 */       synchronized (syncObj) {
/*  279 */         if (updateAvailable)
/*      */           break; 
/*      */       } 
/*      */     } 
/*  283 */     return updateAvailable;
/*      */   }
/*      */   
/*      */   private static class DownloadCallbackHelper implements DownloadProtocol.DownloadDelegate {
/*      */     LaunchDownload.DownloadProgress _downloadProgress;
/*      */     long _totalSize;
/*      */     long _downloadedSoFar;
/*      */     long _currentTotal;
/*      */     
/*      */     public DownloadCallbackHelper(LaunchDownload.DownloadProgress param1DownloadProgress, long param1Long) {
/*  293 */       this._downloadProgress = param1DownloadProgress;
/*  294 */       this._totalSize = param1Long;
/*  295 */       this._downloadedSoFar = 0L;
/*      */     }
/*      */     
/*      */     public void downloading(URL param1URL, String param1String, int param1Int1, int param1Int2, boolean param1Boolean) {
/*  299 */       int i = -1;
/*  300 */       if (this._totalSize != -1L) {
/*      */ 
/*      */ 
/*      */         
/*  304 */         double d1 = param1Boolean ? 0.8D : 0.9D;
/*  305 */         double d2 = this._downloadedSoFar + d1 * param1Int1;
/*      */         
/*  307 */         i = getPercent(d2);
/*  308 */         this._currentTotal = param1Int2;
/*      */       } 
/*  310 */       if (this._downloadProgress != null) {
/*  311 */         this._downloadProgress.progress(param1URL, param1String, this._downloadedSoFar + param1Int1, this._totalSize, i);
/*      */       }
/*      */     }
/*      */     
/*      */     public void patching(URL param1URL, String param1String, int param1Int) {
/*  316 */       int i = -1;
/*  317 */       if (this._totalSize != -1L) {
/*  318 */         double d = this._downloadedSoFar + this._currentTotal * (0.8D + param1Int / 1000.0D);
/*      */ 
/*      */         
/*  321 */         i = getPercent(d);
/*      */       } 
/*  323 */       if (this._downloadProgress != null) {
/*  324 */         this._downloadProgress.patching(param1URL, param1String, param1Int, i);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public void validating(URL param1URL, int param1Int1, int param1Int2) {
/*  330 */       int i = -1;
/*  331 */       if (this._totalSize != -1L && param1Int2 != 0) {
/*  332 */         double d = this._downloadedSoFar + 0.9D * this._currentTotal + 0.1D * this._currentTotal * param1Int1 / param1Int2;
/*      */         
/*  334 */         i = getPercent(d);
/*      */       } 
/*  336 */       if (this._downloadProgress != null) {
/*  337 */         this._downloadProgress.validating(param1URL, null, param1Int1, param1Int2, i);
/*      */       }
/*      */ 
/*      */       
/*  341 */       if (param1Int1 == param1Int2) {
/*  342 */         this._downloadedSoFar += this._currentTotal;
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public void downloadFailed(URL param1URL, String param1String) {
/*  348 */       if (this._downloadProgress != null) {
/*  349 */         this._downloadProgress.downloadFailed(param1URL, param1String);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     private int getPercent(double param1Double) {
/*  355 */       if (param1Double > this._totalSize) {
/*      */         
/*  357 */         this._totalSize = -1L;
/*  358 */         return -1;
/*      */       } 
/*  360 */       double d = param1Double * 100.0D / this._totalSize;
/*  361 */       return (int)(d + 0.5D);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static File[] getNativeDirectories(LaunchDesc paramLaunchDesc) {
/*  368 */     ResourcesDesc resourcesDesc = paramLaunchDesc.getResources();
/*  369 */     if (resourcesDesc == null) return new File[0];
/*      */     
/*  371 */     JARDesc[] arrayOfJARDesc = resourcesDesc.getEagerOrAllJarDescs(true);
/*  372 */     ArrayList arrayList = new ArrayList();
/*  373 */     for (byte b = 0; b < arrayOfJARDesc.length; b++) {
/*  374 */       if (arrayOfJARDesc[b].isNativeLib()) {
/*  375 */         URL uRL = arrayOfJARDesc[b].getLocation();
/*  376 */         String str = arrayOfJARDesc[b].getVersion();
/*  377 */         DiskCacheEntry diskCacheEntry = DownloadProtocol.getCachedVersion(uRL, str, 1);
/*      */ 
/*      */         
/*  380 */         if (diskCacheEntry != null) {
/*  381 */           arrayList.add(diskCacheEntry.getDirectory());
/*      */         }
/*      */       } 
/*      */     } 
/*  385 */     File[] arrayOfFile = new File[arrayList.size()];
/*  386 */     return arrayList.<File>toArray(arrayOfFile);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void downloadExtensions(LaunchDesc paramLaunchDesc, DownloadProgress paramDownloadProgress, int paramInt, ArrayList paramArrayList) throws IOException, JNLPException {
/*  395 */     downloadExtensionsHelper(paramLaunchDesc, paramDownloadProgress, paramInt, false, paramArrayList);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean getCachedExtensions(LaunchDesc paramLaunchDesc) throws IOException, JNLPException {
/*  405 */     return downloadExtensionsHelper(paramLaunchDesc, null, 0, true, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean downloadExtensionsHelper(LaunchDesc paramLaunchDesc, DownloadProgress paramDownloadProgress, int paramInt, boolean paramBoolean, ArrayList paramArrayList) throws IOException, JNLPException {
/*  412 */     ResourcesDesc resourcesDesc = paramLaunchDesc.getResources();
/*  413 */     if (resourcesDesc == null) return true;
/*      */     
/*  415 */     String str = JREInfo.getKnownPlatforms();
/*      */ 
/*      */     
/*  418 */     ArrayList arrayList = new ArrayList();
/*      */     
/*  420 */     resourcesDesc.visit(new ResourceVisitor(arrayList) { private final ArrayList val$list;
/*      */           public void visitJARDesc(JARDesc param1JARDesc) {}
/*      */           public void visitPropertyDesc(PropertyDesc param1PropertyDesc) {}
/*      */           public void visitPackageDesc(PackageDesc param1PackageDesc) {}
/*      */           
/*      */           public void visitExtensionDesc(ExtensionDesc param1ExtensionDesc) {
/*  426 */             this.val$list.add(param1ExtensionDesc);
/*      */           }
/*      */           public void visitJREDesc(JREDesc param1JREDesc) {} }
/*      */       );
/*  430 */     paramInt += arrayList.size();
/*  431 */     for (byte b = 0; b < arrayList.size(); b++) {
/*  432 */       ExtensionDesc extensionDesc = arrayList.get(b);
/*      */       
/*  434 */       String str1 = extensionDesc.getName();
/*  435 */       if (str1 == null) {
/*      */         
/*  437 */         str1 = extensionDesc.getLocation().toString();
/*  438 */         int i = str1.lastIndexOf('/');
/*  439 */         if (i > 0) str1 = str1.substring(i + 1, str1.length());
/*      */       
/*      */       } 
/*      */       
/*  443 */       paramInt--;
/*  444 */       if (paramDownloadProgress != null) paramDownloadProgress.extensionDownload(str1, paramInt);
/*      */ 
/*      */       
/*  447 */       DiskCacheEntry diskCacheEntry = null;
/*  448 */       if (!paramBoolean) {
/*  449 */         diskCacheEntry = DownloadProtocol.getExtension(extensionDesc.getLocation(), extensionDesc.getVersion(), str, false);
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/*  455 */         diskCacheEntry = DownloadProtocol.getCachedExtension(extensionDesc.getLocation(), extensionDesc.getVersion(), str);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  461 */         if (diskCacheEntry == null) return false;
/*      */       
/*      */       } 
/*      */       
/*  465 */       Trace.println("Downloaded extension: " + extensionDesc.getLocation() + ": " + diskCacheEntry.getFile(), TraceLevel.NETWORK);
/*      */ 
/*      */ 
/*      */       
/*  469 */       LaunchDesc launchDesc = LaunchDescFactory.buildDescriptor(diskCacheEntry.getFile());
/*  470 */       boolean bool = false;
/*  471 */       if (launchDesc.getLaunchType() == 3) {
/*      */         
/*  473 */         bool = true;
/*  474 */       } else if (launchDesc.getLaunchType() == 4) {
/*  475 */         extensionDesc.setInstaller(true);
/*      */         
/*  477 */         LocalApplicationProperties localApplicationProperties = Cache.getLocalApplicationProperties(diskCacheEntry.getLocation(), diskCacheEntry.getVersionId(), paramLaunchDesc, false);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  482 */         bool = !localApplicationProperties.isLocallyInstalled() ? true : false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  491 */         if (paramArrayList != null && (isUpdateAvailable(launchDesc) || bool)) {
/*  492 */           paramArrayList.add(diskCacheEntry.getFile());
/*      */         }
/*      */         
/*  495 */         if (paramBoolean && bool)
/*      */         {
/*      */           
/*  498 */           return false;
/*      */         }
/*      */       } else {
/*      */         
/*  502 */         throw new MissingFieldException(launchDesc.getSource(), "<component-desc>|<installer-desc>");
/*      */       } 
/*      */       
/*  505 */       if (bool) {
/*  506 */         extensionDesc.setExtensionDesc(launchDesc);
/*      */         
/*  508 */         boolean bool1 = downloadExtensionsHelper(launchDesc, paramDownloadProgress, paramInt, paramBoolean, paramArrayList);
/*  509 */         if (!bool1) return false; 
/*      */       } 
/*      */     } 
/*  512 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void downloadJRE(LaunchDesc paramLaunchDesc, DownloadProgress paramDownloadProgress, ArrayList paramArrayList) throws JNLPException, IOException {
/*  518 */     JREDesc jREDesc = paramLaunchDesc.getResources().getSelectedJRE();
/*  519 */     String str1 = jREDesc.getVersion();
/*  520 */     URL uRL = jREDesc.getHref();
/*      */ 
/*      */     
/*  523 */     boolean bool = (uRL == null) ? true : false;
/*  524 */     if (uRL == null) {
/*  525 */       String str = Config.getProperty("deployment.javaws.installURL");
/*  526 */       if (str != null) {
/*      */         try {
/*  528 */           uRL = new URL(str);
/*  529 */         } catch (MalformedURLException malformedURLException) {}
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  535 */     if (paramDownloadProgress != null) paramDownloadProgress.jreDownload(str1, uRL);
/*      */ 
/*      */     
/*  538 */     String str2 = JREInfo.getKnownPlatforms();
/*  539 */     DiskCacheEntry diskCacheEntry = null;
/*      */     try {
/*  541 */       diskCacheEntry = DownloadProtocol.getJRE(uRL, str1, bool, str2);
/*  542 */     } catch (ErrorCodeResponseException errorCodeResponseException) {
/*      */       
/*  544 */       errorCodeResponseException.setJreDownload(true);
/*  545 */       throw errorCodeResponseException;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  550 */     LaunchDesc launchDesc = LaunchDescFactory.buildDescriptor(diskCacheEntry.getFile());
/*  551 */     if (launchDesc.getLaunchType() != 4) {
/*  552 */       throw new MissingFieldException(launchDesc.getSource(), "<installer-desc>");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  557 */     if (paramArrayList != null) paramArrayList.add(diskCacheEntry.getFile());
/*      */ 
/*      */     
/*  560 */     jREDesc.setExtensionDesc(launchDesc);
/*      */ 
/*      */     
/*  563 */     downloadExtensionsHelper(launchDesc, paramDownloadProgress, 0, false, paramArrayList);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void downloadResource(LaunchDesc paramLaunchDesc, URL paramURL, String paramString, DownloadProgress paramDownloadProgress, boolean paramBoolean) throws IOException, JNLPException {
/*  572 */     ResourcesDesc resourcesDesc = paramLaunchDesc.getResources();
/*  573 */     if (resourcesDesc == null)
/*  574 */       return;  JARDesc[] arrayOfJARDesc = resourcesDesc.getResource(paramURL, paramString);
/*  575 */     downloadJarFiles(arrayOfJARDesc, paramDownloadProgress, paramBoolean);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void downloadParts(LaunchDesc paramLaunchDesc, String[] paramArrayOfString, DownloadProgress paramDownloadProgress, boolean paramBoolean) throws IOException, JNLPException {
/*  582 */     ResourcesDesc resourcesDesc = paramLaunchDesc.getResources();
/*  583 */     if (resourcesDesc == null)
/*  584 */       return;  JARDesc[] arrayOfJARDesc = resourcesDesc.getPartJars(paramArrayOfString);
/*  585 */     downloadJarFiles(arrayOfJARDesc, paramDownloadProgress, paramBoolean);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void downloadExtensionPart(LaunchDesc paramLaunchDesc, URL paramURL, String paramString, String[] paramArrayOfString, DownloadProgress paramDownloadProgress, boolean paramBoolean) throws IOException, JNLPException {
/*  595 */     ResourcesDesc resourcesDesc = paramLaunchDesc.getResources();
/*  596 */     if (resourcesDesc == null)
/*  597 */       return;  JARDesc[] arrayOfJARDesc = resourcesDesc.getExtensionPart(paramURL, paramString, paramArrayOfString);
/*  598 */     downloadJarFiles(arrayOfJARDesc, paramDownloadProgress, paramBoolean);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void downloadEagerorAll(LaunchDesc paramLaunchDesc, boolean paramBoolean1, DownloadProgress paramDownloadProgress, boolean paramBoolean2) throws IOException, JNLPException {
/*  604 */     ResourcesDesc resourcesDesc = paramLaunchDesc.getResources();
/*  605 */     if (resourcesDesc == null)
/*  606 */       return;  JARDesc[] arrayOfJARDesc = resourcesDesc.getEagerOrAllJarDescs(paramBoolean1);
/*      */ 
/*      */     
/*  609 */     if (!paramBoolean1) {
/*  610 */       JARDesc[] arrayOfJARDesc1 = resourcesDesc.getEagerOrAllJarDescs(true);
/*      */       
/*  612 */       if (arrayOfJARDesc1.length != arrayOfJARDesc.length) {
/*  613 */         HashSet hashSet = new HashSet(Arrays.asList((Object[])arrayOfJARDesc));
/*  614 */         byte b1 = 0;
/*  615 */         for (byte b2 = 0; b2 < arrayOfJARDesc1.length; b2++) {
/*  616 */           URL uRL = arrayOfJARDesc1[b2].getLocation();
/*  617 */           String str = arrayOfJARDesc1[b2].getVersion();
/*  618 */           boolean bool = arrayOfJARDesc1[b2].isJavaFile() ? false : true;
/*  619 */           if (!hashSet.contains(arrayOfJARDesc1[b2]) && DownloadProtocol.isInCache(uRL, str, bool)) {
/*  620 */             b1++;
/*      */           } else {
/*  622 */             arrayOfJARDesc1[b2] = null;
/*      */           } 
/*      */         } 
/*      */         
/*  626 */         if (b1 > 0) {
/*  627 */           JARDesc[] arrayOfJARDesc2 = new JARDesc[arrayOfJARDesc.length + b1];
/*  628 */           System.arraycopy(arrayOfJARDesc, 0, arrayOfJARDesc2, 0, arrayOfJARDesc.length);
/*  629 */           int i = arrayOfJARDesc.length;
/*  630 */           for (byte b = 0; b < arrayOfJARDesc1.length; b++) {
/*  631 */             if (arrayOfJARDesc1[b] != null) arrayOfJARDesc2[i++] = arrayOfJARDesc1[b]; 
/*      */           } 
/*  633 */           arrayOfJARDesc = arrayOfJARDesc2;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  638 */     downloadJarFiles(arrayOfJARDesc, paramDownloadProgress, paramBoolean2);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void downloadJarFiles(JARDesc[] paramArrayOfJARDesc, DownloadProgress paramDownloadProgress, boolean paramBoolean) throws JNLPException, IOException {
/*  654 */     if (paramArrayOfJARDesc == null) {
/*      */       return;
/*      */     }
/*  657 */     Trace.println("Contacting server for JAR file sizes", TraceLevel.NETWORK);
/*      */ 
/*      */ 
/*      */     
/*  661 */     long l = 0L;
/*  662 */     for (byte b1 = 0; b1 < paramArrayOfJARDesc.length && l != -1L; b1++) {
/*      */       try {
/*  664 */         JARDesc jARDesc = paramArrayOfJARDesc[b1];
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  669 */         boolean bool = jARDesc.isNativeLib() ? true : false;
/*      */         
/*  671 */         long l1 = jARDesc.getSize();
/*  672 */         if (l1 == 0L)
/*      */         {
/*  674 */           l1 = DownloadProtocol.getDownloadSize(paramArrayOfJARDesc[b1].getLocation(), paramArrayOfJARDesc[b1].getVersion(), bool);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  679 */         Trace.println("Size of " + paramArrayOfJARDesc[b1].getLocation() + ": " + l1, TraceLevel.NETWORK);
/*      */         
/*  681 */         if (l1 == -1L) {
/*      */           
/*  683 */           l = -1L;
/*      */         } else {
/*  685 */           l += l1;
/*      */         } 
/*  687 */       } catch (JNLPException jNLPException) {
/*  688 */         if (paramDownloadProgress != null) paramDownloadProgress.downloadFailed(paramArrayOfJARDesc[b1].getLocation(), paramArrayOfJARDesc[b1].getVersion()); 
/*  689 */         throw jNLPException;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  695 */     Trace.println("Total size to download: " + l, TraceLevel.NETWORK);
/*      */ 
/*      */ 
/*      */     
/*  699 */     if (l == 0L) {
/*      */       return;
/*      */     }
/*  702 */     DownloadCallbackHelper downloadCallbackHelper = new DownloadCallbackHelper(paramDownloadProgress, l);
/*      */ 
/*      */     
/*  705 */     for (byte b2 = 0; b2 < paramArrayOfJARDesc.length; b2++) {
/*  706 */       JARDesc jARDesc = paramArrayOfJARDesc[b2];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  713 */         boolean bool = jARDesc.isNativeLib() ? true : false;
/*      */         
/*  715 */         DiskCacheEntry diskCacheEntry = DownloadProtocol.getResource(paramArrayOfJARDesc[b2].getLocation(), paramArrayOfJARDesc[b2].getVersion(), bool, paramBoolean, downloadCallbackHelper);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  721 */         Trace.println("Downloaded " + paramArrayOfJARDesc[b2].getLocation() + ": " + diskCacheEntry, TraceLevel.NETWORK);
/*      */ 
/*      */ 
/*      */         
/*  725 */         if (diskCacheEntry == null) {
/*  726 */           throw new FailedDownloadingResourceException(null, paramArrayOfJARDesc[b2].getLocation(), paramArrayOfJARDesc[b2].getVersion(), null);
/*      */         
/*      */         }
/*      */       
/*      */       }
/*  731 */       catch (JNLPException jNLPException) {
/*  732 */         if (paramDownloadProgress != null) paramDownloadProgress.downloadFailed(jARDesc.getLocation(), jARDesc.getVersion()); 
/*  733 */         throw jNLPException;
/*      */       } 
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
/*      */ 
/*      */   
/*      */   static void checkJNLPSecurity(LaunchDesc paramLaunchDesc) throws MultipleHostsException, NativeLibViolationException {
/*  749 */     boolean[] arrayOfBoolean1 = new boolean[1];
/*  750 */     boolean[] arrayOfBoolean2 = new boolean[1];
/*  751 */     ResourcesDesc resourcesDesc = paramLaunchDesc.getResources();
/*  752 */     if (resourcesDesc == null)
/*  753 */       return;  JARDesc jARDesc = paramLaunchDesc.getResources().getMainJar(true);
/*  754 */     if (jARDesc == null)
/*  755 */       return;  checkJNLPSecurityHelper(paramLaunchDesc, jARDesc.getLocation().getHost(), arrayOfBoolean2, arrayOfBoolean1);
/*  756 */     if (arrayOfBoolean2[0]) throw new MultipleHostsException(); 
/*  757 */     if (arrayOfBoolean1[0]) throw new NativeLibViolationException();
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void checkJNLPSecurityHelper(LaunchDesc paramLaunchDesc, String paramString, boolean[] paramArrayOfboolean1, boolean[] paramArrayOfboolean2) {
/*  765 */     if (paramLaunchDesc.getSecurityModel() != 0)
/*      */       return; 
/*  767 */     ResourcesDesc resourcesDesc = paramLaunchDesc.getResources();
/*  768 */     if (resourcesDesc == null) {
/*      */       return;
/*      */     }
/*  771 */     resourcesDesc.visit(new ResourceVisitor(paramArrayOfboolean1, paramString, paramArrayOfboolean2) { private final boolean[] val$hostViolation; private final String val$host; private final boolean[] val$nativeLibViolation;
/*      */           
/*      */           public void visitJARDesc(JARDesc param1JARDesc) {
/*  774 */             String str = param1JARDesc.getLocation().getHost();
/*  775 */             this.val$hostViolation[0] = (this.val$hostViolation[0] || !this.val$host.equals(str));
/*  776 */             this.val$nativeLibViolation[0] = (this.val$nativeLibViolation[0] || param1JARDesc.isNativeLib());
/*      */           }
/*      */           
/*      */           public void visitExtensionDesc(ExtensionDesc param1ExtensionDesc) {
/*  780 */             if (!this.val$hostViolation[0] && !this.val$nativeLibViolation[0]) {
/*      */               
/*  782 */               LaunchDesc launchDesc = param1ExtensionDesc.getExtensionDesc();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  788 */               if (launchDesc != null && launchDesc.getSecurityModel() == 0) {
/*  789 */                 String str = param1ExtensionDesc.getLocation().getHost();
/*  790 */                 this.val$hostViolation[0] = (this.val$hostViolation[0] || !this.val$host.equals(str));
/*      */ 
/*      */                 
/*  793 */                 if (!this.val$hostViolation[0]) {
/*  794 */                   LaunchDownload.checkJNLPSecurityHelper(launchDesc, this.val$host, this.val$hostViolation, this.val$nativeLibViolation);
/*      */                 }
/*      */               } 
/*      */             } 
/*      */           }
/*      */           
/*      */           public void visitPropertyDesc(PropertyDesc param1PropertyDesc) {}
/*      */           
/*      */           public void visitPackageDesc(PackageDesc param1PackageDesc) {}
/*      */           
/*      */           public void visitJREDesc(JREDesc param1JREDesc) {} }
/*      */       );
/*      */   }
/*      */   
/*      */   public static long getCachedSize(LaunchDesc paramLaunchDesc) {
/*  809 */     long l = 0L;
/*  810 */     ResourcesDesc resourcesDesc = paramLaunchDesc.getResources();
/*  811 */     if (resourcesDesc == null) return l; 
/*  812 */     JARDesc[] arrayOfJARDesc = resourcesDesc.getEagerOrAllJarDescs(true);
/*      */ 
/*      */     
/*  815 */     for (byte b = 0; b < arrayOfJARDesc.length; b++) {
/*  816 */       boolean bool = arrayOfJARDesc[b].isNativeLib() ? true : false;
/*      */       
/*  818 */       l += DownloadProtocol.getCachedSize(arrayOfJARDesc[b].getLocation(), arrayOfJARDesc[b].getVersion(), 0);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  823 */     return l;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String getMainClassName(LaunchDesc paramLaunchDesc, boolean paramBoolean) throws IOException, JNLPException, LaunchDescException {
/*  833 */     String str1 = null;
/*      */ 
/*      */     
/*  836 */     ApplicationDesc applicationDesc = paramLaunchDesc.getApplicationDescriptor();
/*  837 */     if (applicationDesc != null) {
/*  838 */       str1 = applicationDesc.getMainClass();
/*      */     }
/*  840 */     InstallerDesc installerDesc = paramLaunchDesc.getInstallerDescriptor();
/*  841 */     if (installerDesc != null) {
/*  842 */       str1 = installerDesc.getMainClass();
/*      */     }
/*  844 */     AppletDesc appletDesc = paramLaunchDesc.getAppletDescriptor();
/*  845 */     if (appletDesc != null) {
/*  846 */       str1 = appletDesc.getAppletClass();
/*      */     }
/*  848 */     if (str1 != null && str1.length() == 0) str1 = null;
/*      */ 
/*      */     
/*  851 */     if (paramLaunchDesc.getResources() == null) return null; 
/*  852 */     JARDesc jARDesc = paramLaunchDesc.getResources().getMainJar(paramBoolean);
/*  853 */     if (jARDesc == null) return null;
/*      */ 
/*      */     
/*  856 */     DiskCacheEntry diskCacheEntry = DownloadProtocol.getResource(jARDesc.getLocation(), jARDesc.getVersion(), 0, true, null);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  861 */     JarFile jarFile = new JarFile(diskCacheEntry.getFile());
/*      */ 
/*      */     
/*  864 */     if (str1 == null && paramLaunchDesc.getLaunchType() != 2) {
/*  865 */       Manifest manifest = jarFile.getManifest();
/*  866 */       str1 = (manifest != null) ? manifest.getMainAttributes().getValue("Main-Class") : null;
/*      */     } 
/*      */ 
/*      */     
/*  870 */     if (str1 == null) {
/*  871 */       throw new LaunchDescException(paramLaunchDesc, ResourceManager.getString("launch.error.nomainclassspec"), null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  877 */     String str2 = str1.replace('.', '/') + ".class";
/*  878 */     if (jarFile.getEntry(str2) == null) {
/*  879 */       throw new LaunchDescException(paramLaunchDesc, ResourceManager.getString("launch.error.nomainclass", str1, jARDesc.getLocation().toString()), null);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  884 */     return str1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void checkSignedLaunchDesc(LaunchDesc paramLaunchDesc) throws IOException, JNLPException {
/*  895 */     ArrayList arrayList = new ArrayList();
/*      */     
/*  897 */     addExtensions(arrayList, paramLaunchDesc);
/*      */     
/*  899 */     for (byte b = 0; b < arrayList.size(); b++) {
/*  900 */       LaunchDesc launchDesc = arrayList.get(b);
/*  901 */       checkSignedLaunchDescHelper(launchDesc);
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
/*      */   static void checkSignedResources(LaunchDesc paramLaunchDesc) throws IOException, JNLPException {
/*  913 */     ArrayList arrayList = new ArrayList();
/*      */     
/*  915 */     addExtensions(arrayList, paramLaunchDesc);
/*      */     
/*  917 */     for (byte b = 0; b < arrayList.size(); b++) {
/*  918 */       LaunchDesc launchDesc = arrayList.get(b);
/*  919 */       checkSignedResourcesHelper(launchDesc);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void addExtensions(ArrayList paramArrayList, LaunchDesc paramLaunchDesc) {
/*  925 */     paramArrayList.add(paramLaunchDesc);
/*  926 */     ResourcesDesc resourcesDesc = paramLaunchDesc.getResources();
/*  927 */     if (resourcesDesc != null)
/*  928 */       resourcesDesc.visit(new ResourceVisitor(paramArrayList) {
/*      */             private final ArrayList val$list;
/*      */             
/*      */             public void visitJARDesc(JARDesc param1JARDesc) {}
/*      */             
/*      */             public void visitExtensionDesc(ExtensionDesc param1ExtensionDesc) {
/*  934 */               if (!param1ExtensionDesc.isInstaller())
/*  935 */                 LaunchDownload.addExtensions(this.val$list, param1ExtensionDesc.getExtensionDesc()); 
/*      */             }
/*      */             public void visitPropertyDesc(PropertyDesc param1PropertyDesc) {}
/*      */             public void visitPackageDesc(PackageDesc param1PackageDesc) {}
/*      */             public void visitJREDesc(JREDesc param1JREDesc) {}
/*      */           }); 
/*      */   }
/*      */   private static void checkSignedLaunchDescHelper(LaunchDesc paramLaunchDesc) throws IOException, JNLPException {
/*  943 */     boolean bool = paramLaunchDesc.isApplicationDescriptor();
/*      */     
/*  945 */     byte[] arrayOfByte = null;
/*      */     try {
/*  947 */       arrayOfByte = getSignedJNLPFile(paramLaunchDesc, bool);
/*      */       
/*  949 */       if (arrayOfByte != null) {
/*      */         
/*  951 */         LaunchDesc launchDesc = LaunchDescFactory.buildDescriptor(arrayOfByte);
/*      */ 
/*      */ 
/*      */         
/*  955 */         Trace.println("Signed JNLP file: ", TraceLevel.BASIC);
/*  956 */         Trace.println(launchDesc.toString(), TraceLevel.BASIC);
/*      */         
/*  958 */         paramLaunchDesc.checkSigning(launchDesc);
/*  959 */         arrayOfByte = null;
/*      */       } 
/*  961 */     } catch (LaunchDescException launchDescException) {
/*      */ 
/*      */       
/*  964 */       launchDescException.setIsSignedLaunchDesc();
/*  965 */       throw launchDescException;
/*  966 */     } catch (IOException iOException) {
/*      */       
/*  968 */       throw iOException;
/*  969 */     } catch (JNLPException jNLPException) {
/*  970 */       throw jNLPException;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void checkSignedResourcesHelper(LaunchDesc paramLaunchDesc) throws IOException, JNLPException {
/*  976 */     ResourcesDesc resourcesDesc = paramLaunchDesc.getResources();
/*  977 */     if (resourcesDesc == null)
/*      */       return; 
/*  979 */     JARDesc[] arrayOfJARDesc = resourcesDesc.getLocalJarDescs();
/*      */ 
/*      */ 
/*      */     
/*  983 */     boolean bool = true;
/*  984 */     Certificate[] arrayOfCertificate = null;
/*  985 */     CodeSource codeSource = null;
/*  986 */     URL uRL1 = paramLaunchDesc.getCanonicalHome();
/*      */     
/*  988 */     byte b1 = 0;
/*  989 */     URL uRL2 = null;
/*  990 */     for (byte b2 = 0; b2 < arrayOfJARDesc.length; b2++) {
/*  991 */       JARDesc jARDesc = arrayOfJARDesc[b2];
/*  992 */       boolean bool1 = jARDesc.isJavaFile() ? false : true;
/*      */       
/*  994 */       DiskCacheEntry diskCacheEntry = DownloadProtocol.getCachedVersion(jARDesc.getLocation(), jARDesc.getVersion(), bool1);
/*      */ 
/*      */       
/*  997 */       if (diskCacheEntry != null) {
/*  998 */         b1++;
/*  999 */         JarFile jarFile = new JarFile(diskCacheEntry.getFile());
/* 1000 */         CodeSource codeSource1 = SigningInfo.getCodeSource(uRL1, jarFile);
/* 1001 */         if (codeSource1 != null) {
/* 1002 */           Certificate[] arrayOfCertificate1 = codeSource1.getCertificates();
/*      */           
/* 1004 */           if (arrayOfCertificate1 == null) {
/* 1005 */             Trace.println("getCertChain returned null for: " + diskCacheEntry.getFile(), TraceLevel.BASIC);
/*      */             
/* 1007 */             bool = false;
/* 1008 */             uRL2 = jARDesc.getLocation();
/*      */           } 
/* 1010 */           if (arrayOfCertificate == null) {
/*      */             
/* 1012 */             arrayOfCertificate = arrayOfCertificate1;
/* 1013 */             codeSource = codeSource1;
/* 1014 */           } else if (arrayOfCertificate1 != null) {
/*      */             
/* 1016 */             if (!SigningInfo.equalChains(arrayOfCertificate, arrayOfCertificate1)) {
/* 1017 */               throw new LaunchDescException(paramLaunchDesc, ResourceManager.getString("launch.error.singlecertviolation"), null);
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1027 */     if (paramLaunchDesc.getSecurityModel() != 0) {
/*      */       
/* 1029 */       if (!bool) {
/* 1030 */         throw new UnsignedAccessViolationException(paramLaunchDesc, uRL2, true);
/*      */       }
/*      */       
/* 1033 */       if (b1 > 0) {
/* 1034 */         AppPolicy.getInstance().grantUnrestrictedAccess(paramLaunchDesc, codeSource);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] getSignedJNLPFile(LaunchDesc paramLaunchDesc, boolean paramBoolean) throws IOException, JNLPException {
/* 1045 */     if (paramLaunchDesc.getResources() == null) return null; 
/* 1046 */     JARDesc jARDesc = paramLaunchDesc.getResources().getMainJar(paramBoolean);
/* 1047 */     if (jARDesc == null) return null;
/*      */     
/* 1049 */     DiskCacheEntry diskCacheEntry = DownloadProtocol.getResource(jARDesc.getLocation(), jARDesc.getVersion(), 0, true, null);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1054 */     JarFile jarFile = new JarFile(diskCacheEntry.getFile());
/* 1055 */     JarEntry jarEntry = jarFile.getJarEntry("JNLP-INF/APPLICATION.JNLP");
/* 1056 */     if (jarEntry == null) {
/*      */       
/* 1058 */       Enumeration enumeration = jarFile.entries();
/* 1059 */       while (enumeration.hasMoreElements() && jarEntry == null) {
/* 1060 */         JarEntry jarEntry1 = enumeration.nextElement();
/* 1061 */         if (jarEntry1.getName().equalsIgnoreCase("JNLP-INF/APPLICATION.JNLP")) {
/* 1062 */           jarEntry = jarEntry1;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1067 */     if (jarEntry == null) {
/* 1068 */       if (jarFile != null) jarFile.close(); 
/* 1069 */       return null;
/*      */     } 
/*      */ 
/*      */     
/* 1073 */     byte[] arrayOfByte = new byte[(int)jarEntry.getSize()];
/* 1074 */     DataInputStream dataInputStream = new DataInputStream(jarFile.getInputStream(jarEntry));
/* 1075 */     dataInputStream.readFully(arrayOfByte, 0, (int)jarEntry.getSize());
/* 1076 */     dataInputStream.close();
/* 1077 */     jarFile.close();
/*      */     
/* 1079 */     return arrayOfByte;
/*      */   }
/*      */   
/*      */   public static interface DownloadProgress {
/*      */     void jreDownload(String param1String, URL param1URL);
/*      */     
/*      */     void extensionDownload(String param1String, int param1Int);
/*      */     
/*      */     void progress(URL param1URL, String param1String, long param1Long1, long param1Long2, int param1Int);
/*      */     
/*      */     void validating(URL param1URL, String param1String, long param1Long1, long param1Long2, int param1Int);
/*      */     
/*      */     void patching(URL param1URL, String param1String, int param1Int1, int param1Int2);
/*      */     
/*      */     void downloadFailed(URL param1URL, String param1String);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\LaunchDownload.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */