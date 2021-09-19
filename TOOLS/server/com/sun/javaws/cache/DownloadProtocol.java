/*     */ package com.sun.javaws.cache;
/*     */ 
/*     */ import com.sun.deploy.config.Config;
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.deploy.util.TraceLevel;
/*     */ import com.sun.javaws.Globals;
/*     */ import com.sun.javaws.JavawsFactory;
/*     */ import com.sun.javaws.exceptions.BadJARFileException;
/*     */ import com.sun.javaws.exceptions.BadMimeTypeResponseException;
/*     */ import com.sun.javaws.exceptions.BadVersionResponseException;
/*     */ import com.sun.javaws.exceptions.ErrorCodeResponseException;
/*     */ import com.sun.javaws.exceptions.FailedDownloadingResourceException;
/*     */ import com.sun.javaws.exceptions.InvalidJarDiffException;
/*     */ import com.sun.javaws.exceptions.JNLPException;
/*     */ import com.sun.javaws.exceptions.MissingVersionResponseException;
/*     */ import com.sun.javaws.jardiff.JarDiffPatcher;
/*     */ import com.sun.javaws.net.CanceledDownloadException;
/*     */ import com.sun.javaws.net.HttpDownloadListener;
/*     */ import com.sun.javaws.net.HttpRequest;
/*     */ import com.sun.javaws.net.HttpResponse;
/*     */ import com.sun.javaws.security.SigningInfo;
/*     */ import com.sun.javaws.util.VersionID;
/*     */ import com.sun.javaws.util.VersionString;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.Date;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.zip.ZipException;
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
/*     */ public class DownloadProtocol
/*     */ {
/*     */   public static final int JAR_DOWNLOAD = 0;
/*     */   public static final int NATIVE_DOWNLOAD = 1;
/*     */   public static final int IMAGE_DOWNLOAD = 2;
/*     */   public static final int APPLICATION_JNLP_DOWNLOAD = 3;
/*     */   public static final int EXTENSION_JNLP_DOWNLOAD = 4;
/*     */   private static final String JNLP_MIME_TYPE = "application/x-java-jnlp-file";
/*     */   private static final String ERROR_MIME_TYPE = "application/x-java-jnlp-error";
/*     */   private static final String JAR_MIME_TYPE = "application/x-java-archive";
/*     */   private static final String JARDIFF_MIME_TYPE = "application/x-java-archive-diff";
/*     */   private static final String GIF_MIME_TYPE = "image/gif";
/*     */   private static final String JPEG_MIME_TYPE = "image/jpeg";
/*     */   private static final String ARG_ARCH = "arch";
/*     */   private static final String ARG_OS = "os";
/*     */   private static final String ARG_LOCALE = "locale";
/*     */   private static final String ARG_VERSION_ID = "version-id";
/*     */   private static final String ARG_CURRENT_VERSION_ID = "current-version-id";
/*     */   private static final String ARG_PLATFORM_VERSION_ID = "platform-version-id";
/*     */   private static final String ARG_KNOWN_PLATFORMS = "known-platforms";
/*     */   private static final String REPLY_JNLP_VERSION = "x-java-jnlp-version-id";
/*     */   
/*     */   static class DownloadInfo
/*     */   {
/*     */     private URL _location;
/*     */     private String _version;
/*     */     private int _kind;
/*     */     private boolean _isCacheOk;
/*  95 */     private String _knownPlatforms = null;
/*     */     private boolean _isPlatformVersion = false;
/*     */     
/*     */     public DownloadInfo(URL param1URL, String param1String, int param1Int, boolean param1Boolean) {
/*  99 */       this._location = param1URL;
/* 100 */       this._version = param1String;
/* 101 */       this._kind = param1Int;
/* 102 */       this._isCacheOk = param1Boolean;
/*     */     }
/*     */ 
/*     */     
/*     */     public DownloadInfo(URL param1URL, String param1String1, boolean param1Boolean1, String param1String2, boolean param1Boolean2) {
/* 107 */       this._location = param1URL;
/* 108 */       this._version = param1String1;
/* 109 */       this._kind = 4;
/* 110 */       this._isCacheOk = param1Boolean1;
/* 111 */       this._knownPlatforms = param1String2;
/* 112 */       this._isPlatformVersion = param1Boolean2;
/*     */     }
/*     */     
/* 115 */     URL getLocation() { return this._location; }
/* 116 */     String getVersion() { return this._version; } int getKind() {
/* 117 */       return this._kind;
/*     */     }
/*     */     char getEntryType() {
/* 120 */       switch (this._kind) { case 0:
/* 121 */           return 'R';
/* 122 */         case 2: return 'R';
/* 123 */         case 1: return 'R';
/* 124 */         case 3: return 'A';
/* 125 */         case 4: return 'E'; }
/*     */       
/* 127 */       return 'a';
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isCacheOk(DiskCacheEntry param1DiskCacheEntry, boolean param1Boolean) {
/* 132 */       return (param1Boolean && (this._version != null || this._isCacheOk) && param1DiskCacheEntry.getTimeStamp() != 0L);
/*     */     }
/*     */ 
/*     */     
/*     */     URL getRequestURL(DiskCacheEntry param1DiskCacheEntry) {
/* 137 */       StringBuffer stringBuffer = new StringBuffer();
/*     */ 
/*     */       
/* 140 */       if (this._version != null && this._kind != 4) {
/* 141 */         addURLArgument(stringBuffer, "version-id", this._version);
/*     */ 
/*     */         
/* 144 */         if ((this._kind == 0 || this._kind == 1) && param1DiskCacheEntry != null && param1DiskCacheEntry.getVersionId() != null)
/*     */         {
/* 146 */           addURLArgument(stringBuffer, "current-version-id", param1DiskCacheEntry.getVersionId());
/*     */         }
/*     */       } 
/*     */       
/* 150 */       if (this._kind == 4 && this._version != null) {
/* 151 */         if (this._isPlatformVersion) {
/* 152 */           addURLArgument(stringBuffer, "platform-version-id", this._version);
/*     */         } else {
/* 154 */           addURLArgument(stringBuffer, "version-id", this._version);
/*     */         } 
/*     */         
/* 157 */         addURLArgument(stringBuffer, "arch", Config.getOSArch());
/* 158 */         addURLArgument(stringBuffer, "os", Config.getOSName());
/* 159 */         addURLArgument(stringBuffer, "locale", Globals.getDefaultLocaleString());
/* 160 */         if (this._knownPlatforms != null) {
/* 161 */           addURLArgument(stringBuffer, "known-platforms", this._knownPlatforms);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 166 */       if (stringBuffer.length() > 0) stringBuffer.setLength(stringBuffer.length() - 1); 
/* 167 */       if (stringBuffer.length() > 0) stringBuffer.insert(0, '?');
/*     */       
/*     */       try {
/* 170 */         if (Globals.getCodebaseOverride() != null && Globals.getCodebase() != null) {
/* 171 */           return new URL(Globals.getCodebaseOverride() + this._location.getFile().substring(Globals.getCodebase().getFile().length()) + stringBuffer);
/*     */         }
/* 173 */         return new URL(this._location.getProtocol(), this._location.getHost(), this._location.getPort(), this._location.getFile() + stringBuffer);
/*     */ 
/*     */       
/*     */       }
/* 177 */       catch (MalformedURLException malformedURLException) {
/* 178 */         Trace.ignoredException(malformedURLException);
/* 179 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private void addURLArgument(StringBuffer param1StringBuffer, String param1String1, String param1String2) {
/* 185 */       param1StringBuffer.append(URLEncoder.encode(param1String1)); param1StringBuffer.append('=');
/* 186 */       param1StringBuffer.append(URLEncoder.encode(param1String2)); param1StringBuffer.append('&');
/*     */     }
/*     */ 
/*     */     
/*     */     boolean needsReplyVersion(DiskCacheEntry param1DiskCacheEntry) {
/* 191 */       return (this._version != null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isPlatformRequest() {
/* 198 */       return this._isPlatformVersion;
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isValidMimeType(String param1String, DiskCacheEntry param1DiskCacheEntry) {
/* 203 */       if (param1String == null) return false;
/*     */       
/* 205 */       if (this._kind == 0 || this._kind == 1) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 210 */         if (param1String.equalsIgnoreCase("application/x-java-archive-diff")) {
/* 211 */           return (param1DiskCacheEntry != null && param1DiskCacheEntry.getVersionId() != null);
/*     */         }
/* 213 */         return true;
/* 214 */       }  if (this._kind == 2) {
/* 215 */         return (param1String.equalsIgnoreCase("image/jpeg") || param1String.equalsIgnoreCase("image/gif"));
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 221 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isWebNewer(DiskCacheEntry param1DiskCacheEntry, long param1Long1, long param1Long2, String param1String) {
/* 227 */       if (this._version == null) {
/* 228 */         return ((param1Long2 == 0L && param1Long1 > 0L) || param1Long2 > param1DiskCacheEntry.getTimeStamp());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 233 */       return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class UpdateAvailableAction
/*     */     implements DownloadAction
/*     */   {
/*     */     private boolean _result = false;
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
/*     */     public boolean getResult() {
/* 266 */       return this._result;
/*     */     }
/*     */     
/*     */     public void actionInCache(DiskCacheEntry param1DiskCacheEntry) throws IOException, JNLPException {
/* 270 */       this._result = false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void actionOffline(DiskCacheEntry param1DiskCacheEntry, boolean param1Boolean) throws IOException, JNLPException {
/* 275 */       this._result = false;
/*     */     }
/*     */     
/*     */     public boolean skipDownloadStep() {
/* 279 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void actionDownload(DiskCacheEntry param1DiskCacheEntry, DownloadProtocol.DownloadInfo param1DownloadInfo, long param1Long, int param1Int, String param1String1, String param1String2, HttpResponse param1HttpResponse) throws IOException, JNLPException {
/* 284 */       this._result = true;
/*     */     }
/*     */     
/*     */     public boolean useHeadRequest() {
/* 288 */       return true;
/*     */     }
/*     */     
/*     */     private UpdateAvailableAction() {}
/*     */   }
/*     */   
/*     */   private static class IsInCacheAction
/*     */     implements DownloadAction {
/* 296 */     private DiskCacheEntry _dce = null;
/*     */     
/*     */     public DiskCacheEntry getResult() {
/* 299 */       return this._dce;
/*     */     }
/*     */     
/*     */     public void actionInCache(DiskCacheEntry param1DiskCacheEntry) throws IOException, JNLPException {
/* 303 */       this._dce = param1DiskCacheEntry;
/*     */     }
/*     */ 
/*     */     
/*     */     public void actionOffline(DiskCacheEntry param1DiskCacheEntry, boolean param1Boolean) throws IOException, JNLPException {
/* 308 */       this._dce = param1Boolean ? param1DiskCacheEntry : null;
/*     */     }
/*     */     
/*     */     public boolean skipDownloadStep() {
/* 312 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void actionDownload(DiskCacheEntry param1DiskCacheEntry, DownloadProtocol.DownloadInfo param1DownloadInfo, long param1Long, int param1Int, String param1String1, String param1String2, HttpResponse param1HttpResponse) throws IOException, JNLPException {}
/*     */ 
/*     */     
/*     */     public boolean useHeadRequest() {
/* 320 */       return false;
/*     */     }
/*     */     
/*     */     private IsInCacheAction() {}
/*     */   }
/*     */   
/*     */   private static class DownloadSizeAction
/*     */     implements DownloadAction {
/*     */     private long _result;
/*     */     
/*     */     private DownloadSizeAction() {
/* 331 */       this._result = -1L;
/*     */     }
/*     */     public long getResult() {
/* 334 */       return this._result;
/*     */     }
/*     */     
/*     */     public void actionInCache(DiskCacheEntry param1DiskCacheEntry) throws IOException, JNLPException {
/* 338 */       this._result = 0L;
/*     */     }
/*     */ 
/*     */     
/*     */     public void actionOffline(DiskCacheEntry param1DiskCacheEntry, boolean param1Boolean) throws IOException, JNLPException {
/* 343 */       this._result = param1Boolean ? 0L : -1L;
/*     */     }
/*     */     
/*     */     public boolean skipDownloadStep() {
/* 347 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void actionDownload(DiskCacheEntry param1DiskCacheEntry, DownloadProtocol.DownloadInfo param1DownloadInfo, long param1Long, int param1Int, String param1String1, String param1String2, HttpResponse param1HttpResponse) throws IOException, JNLPException {
/* 352 */       this._result = param1Int;
/*     */     }
/*     */     
/*     */     public boolean useHeadRequest() {
/* 356 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class RetrieveAction
/*     */     implements DownloadAction
/*     */   {
/* 364 */     private DiskCacheEntry _result = null;
/* 365 */     private DownloadProtocol.DownloadDelegate _delegate = null;
/*     */     
/*     */     public DiskCacheEntry getResult() {
/* 368 */       return this._result;
/*     */     }
/*     */     public RetrieveAction(DownloadProtocol.DownloadDelegate param1DownloadDelegate) {
/* 371 */       this._delegate = param1DownloadDelegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public void actionInCache(DiskCacheEntry param1DiskCacheEntry) throws IOException, JNLPException {
/* 376 */       this._result = param1DiskCacheEntry;
/*     */     }
/*     */ 
/*     */     
/*     */     public void actionOffline(DiskCacheEntry param1DiskCacheEntry, boolean param1Boolean) throws IOException, JNLPException {
/* 381 */       this._result = param1Boolean ? param1DiskCacheEntry : null;
/*     */     }
/*     */     
/*     */     public boolean skipDownloadStep() {
/* 385 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void actionDownload(DiskCacheEntry param1DiskCacheEntry, DownloadProtocol.DownloadInfo param1DownloadInfo, long param1Long, int param1Int, String param1String1, String param1String2, HttpResponse param1HttpResponse) throws IOException, JNLPException {
/* 390 */       URL uRL = param1DownloadInfo.getLocation();
/*     */       
/* 392 */       boolean bool = param1String2.equalsIgnoreCase("application/x-java-archive-diff");
/* 393 */       String str1 = param1DownloadInfo.getVersion();
/* 394 */       String str2 = (str1 != null) ? param1String1 : null;
/*     */       
/* 396 */       Trace.println("Doing download", TraceLevel.NETWORK);
/*     */ 
/*     */ 
/*     */       
/* 400 */       HttpDownloadListener httpDownloadListener = (this._delegate == null) ? null : new HttpDownloadListener(this, uRL, param1String1, bool) { private final URL val$location; private final String val$responseVersion;
/*     */           public boolean downloadProgress(int param2Int1, int param2Int2) {
/* 402 */             this.this$0._delegate.downloading(this.val$location, this.val$responseVersion, param2Int1, param2Int2, this.val$willPatch);
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 407 */             return true;
/*     */           }
/*     */           private final boolean val$willPatch; private final DownloadProtocol.RetrieveAction this$0; }
/*     */         ;
/* 411 */       File file = null;
/*     */       
/*     */       try {
/* 414 */         file = Cache.getTempCacheFile(uRL, str2);
/* 415 */         JavawsFactory.getHttpDownloadImpl().download(param1HttpResponse, file, httpDownloadListener);
/* 416 */       } catch (IOException iOException) {
/*     */         
/* 418 */         Trace.println("Got exception while downloading resource: " + iOException, TraceLevel.NETWORK);
/*     */ 
/*     */         
/* 421 */         if (this._delegate != null) this._delegate.downloadFailed(uRL, param1String1);
/*     */         
/* 423 */         throw new FailedDownloadingResourceException(uRL, param1String1, iOException);
/* 424 */       } catch (CanceledDownloadException canceledDownloadException) {
/*     */         
/* 426 */         Trace.ignoredException((Exception)canceledDownloadException);
/*     */       } 
/*     */ 
/*     */       
/* 430 */       if (bool) {
/* 431 */         file = DownloadProtocol.applyPatch(param1DiskCacheEntry.getFile(), file, uRL, param1String1, this._delegate);
/*     */       }
/*     */ 
/*     */       
/* 435 */       if (param1DownloadInfo.getKind() == 3 || param1DownloadInfo.getKind() == 4 || param1DownloadInfo.getKind() == 2) {
/*     */ 
/*     */         
/* 438 */         Cache.insertEntry(param1DownloadInfo.getEntryType(), uRL, str2, file, param1Long);
/*     */         
/* 440 */         file = null;
/* 441 */       } else if (param1DownloadInfo.getKind() == 0 || param1DownloadInfo.getKind() == 1) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 446 */         File file1 = (param1DownloadInfo.getKind() == 1) ? Cache.createNativeLibDir(uRL, str2) : null;
/*     */ 
/*     */ 
/*     */         
/* 450 */         JarFile jarFile = new JarFile(file);
/*     */ 
/*     */         
/*     */         try {
/* 454 */           SigningInfo.checkSigning(uRL, str2, jarFile, this._delegate, file1);
/*     */           
/* 456 */           jarFile.close(); jarFile = null;
/* 457 */           Cache.insertEntry(param1DownloadInfo.getEntryType(), uRL, str2, file, param1Long);
/*     */           
/* 459 */           file = null;
/*     */         } finally {
/* 461 */           if (jarFile != null) jarFile.close(); 
/* 462 */           if (file != null) file.delete();
/*     */         
/*     */         } 
/*     */       } 
/*     */       
/* 467 */       this._result = Cache.getCacheEntry(param1DownloadInfo.getEntryType(), uRL, str2);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean useHeadRequest() {
/* 472 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void doDownload(DownloadInfo paramDownloadInfo, DownloadAction paramDownloadAction) throws JNLPException {
/*     */     try {
/* 479 */       boolean[] arrayOfBoolean = new boolean[1];
/* 480 */       DiskCacheEntry diskCacheEntry = findBestDiskCacheEntry(paramDownloadInfo.getEntryType(), paramDownloadInfo.getLocation(), paramDownloadInfo.getVersion(), arrayOfBoolean);
/*     */ 
/*     */ 
/*     */       
/* 484 */       boolean bool = arrayOfBoolean[0];
/*     */ 
/*     */       
/* 487 */       if (diskCacheEntry != null && paramDownloadInfo.isCacheOk(diskCacheEntry, bool)) {
/*     */         
/* 489 */         Trace.println("Found in cache: " + diskCacheEntry, TraceLevel.NETWORK);
/*     */         
/* 491 */         paramDownloadAction.actionInCache(diskCacheEntry);
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 497 */       if (Globals.isOffline()) {
/*     */         
/* 499 */         Trace.println("Offline mode. No Web check. Cache lookup: " + diskCacheEntry, TraceLevel.NETWORK);
/*     */         
/* 501 */         paramDownloadAction.actionOffline(diskCacheEntry, bool);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 506 */       if (paramDownloadAction.skipDownloadStep()) {
/*     */         
/* 508 */         Trace.println("Skipping download step", TraceLevel.NETWORK);
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 514 */       URL uRL = paramDownloadInfo.getRequestURL(diskCacheEntry);
/*     */       
/* 516 */       Trace.println("Connection to: " + uRL, TraceLevel.NETWORK);
/*     */ 
/*     */ 
/*     */       
/* 520 */       HttpRequest httpRequest = JavawsFactory.getHttpRequestImpl();
/*     */       
/* 522 */       HttpResponse httpResponse = null;
/*     */ 
/*     */       
/*     */       try {
/* 526 */         httpResponse = paramDownloadAction.useHeadRequest() ? httpRequest.doHeadRequest(uRL) : httpRequest.doGetRequest(uRL);
/*     */       
/*     */       }
/* 529 */       catch (IOException iOException) {
/* 530 */         httpResponse = paramDownloadAction.useHeadRequest() ? httpRequest.doHeadRequest(uRL, false) : httpRequest.doGetRequest(uRL, false);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 536 */       if (httpResponse.getStatusCode() == 404) {
/* 537 */         throw new FailedDownloadingResourceException(paramDownloadInfo.getLocation(), paramDownloadInfo.getVersion(), new IOException("HTTP response 404"));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 542 */       int i = httpResponse.getContentLength();
/* 543 */       long l = httpResponse.getLastModified();
/* 544 */       String str1 = httpResponse.getResponseHeader("x-java-jnlp-version-id");
/* 545 */       String str2 = paramDownloadInfo.getVersion();
/*     */       
/* 547 */       if (str2 != null && str1 == null && Globals.getCodebaseOverride() != null && (new VersionID(str2)).isSimpleVersion())
/*     */       {
/*     */         
/* 550 */         str1 = str2;
/*     */       }
/*     */       
/* 553 */       String str3 = httpResponse.getContentType();
/*     */ 
/*     */       
/* 556 */       Trace.println("Sever response: (length: " + i + ", lastModified: " + new Date(l) + ", downloadVersion " + str1 + ", mimeType: " + str3 + ")", TraceLevel.NETWORK);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 562 */       if (str3 != null && str3.equalsIgnoreCase("application/x-java-jnlp-error")) {
/* 563 */         BufferedInputStream bufferedInputStream = httpResponse.getInputStream();
/*     */         
/* 565 */         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));
/* 566 */         String str = bufferedReader.readLine();
/* 567 */         throw new ErrorCodeResponseException(paramDownloadInfo.getLocation(), paramDownloadInfo.getVersion(), str);
/*     */       } 
/*     */ 
/*     */       
/* 571 */       if (!paramDownloadInfo.isValidMimeType(str3, diskCacheEntry)) {
/* 572 */         throw new BadMimeTypeResponseException(paramDownloadInfo.getLocation(), paramDownloadInfo.getVersion(), str3);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 577 */       if (paramDownloadInfo.needsReplyVersion(diskCacheEntry)) {
/* 578 */         if (str1 == null) {
/* 579 */           throw new MissingVersionResponseException(paramDownloadInfo.getLocation(), paramDownloadInfo.getVersion());
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 585 */         if (!paramDownloadInfo.isPlatformRequest()) {
/* 586 */           if (!(new VersionString(paramDownloadInfo.getVersion())).contains(str1))
/*     */           {
/* 588 */             throw new BadVersionResponseException(paramDownloadInfo.getLocation(), paramDownloadInfo.getVersion(), str1);
/*     */           }
/*     */           
/* 591 */           VersionID versionID = new VersionID(str1);
/*     */           
/* 593 */           if (!versionID.isSimpleVersion()) {
/* 594 */             throw new BadVersionResponseException(paramDownloadInfo.getLocation(), paramDownloadInfo.getVersion(), str1);
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 602 */       if (diskCacheEntry != null && !paramDownloadInfo.isWebNewer(diskCacheEntry, i, l, str1)) {
/* 603 */         paramDownloadAction.actionInCache(diskCacheEntry);
/* 604 */         httpResponse.disconnect();
/*     */         
/*     */         return;
/*     */       } 
/* 608 */       paramDownloadAction.actionDownload(diskCacheEntry, paramDownloadInfo, l, i, str1, str3, httpResponse);
/* 609 */       httpResponse.disconnect();
/* 610 */     } catch (ZipException zipException) {
/* 611 */       throw new BadJARFileException(paramDownloadInfo.getLocation(), paramDownloadInfo.getVersion(), zipException);
/* 612 */     } catch (JNLPException jNLPException) {
/*     */       
/* 614 */       throw jNLPException;
/* 615 */     } catch (Exception exception) {
/*     */       
/* 617 */       throw new FailedDownloadingResourceException(paramDownloadInfo.getLocation(), paramDownloadInfo.getVersion(), exception);
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
/*     */   
/*     */   private static File applyPatch(File paramFile1, File paramFile2, URL paramURL, String paramString, DownloadDelegate paramDownloadDelegate) throws JNLPException {
/* 631 */     JarDiffPatcher jarDiffPatcher = new JarDiffPatcher();
/*     */     
/* 633 */     File file = null;
/* 634 */     FileOutputStream fileOutputStream = null;
/* 635 */     boolean bool = false;
/*     */     try {
/* 637 */       file = Cache.getTempCacheFile(paramURL, paramString);
/* 638 */       fileOutputStream = new FileOutputStream(file);
/* 639 */       Patcher.PatchDelegate patchDelegate = null;
/*     */       
/* 641 */       if (paramDownloadDelegate != null) {
/* 642 */         paramDownloadDelegate.patching(paramURL, paramString, 0);
/* 643 */         patchDelegate = new Patcher.PatchDelegate(paramDownloadDelegate, paramURL, paramString) { private final DownloadProtocol.DownloadDelegate val$delegate; private final URL val$location; private final String val$newVersionId;
/*     */             public void patching(int param1Int) {
/* 645 */               this.val$delegate.patching(this.val$location, this.val$newVersionId, param1Int);
/*     */             } }
/*     */           ;
/*     */       } 
/*     */       try {
/* 650 */         jarDiffPatcher.applyPatch(patchDelegate, paramFile1.getPath(), paramFile2.getPath(), fileOutputStream);
/* 651 */       } catch (IOException iOException) {
/* 652 */         throw new InvalidJarDiffException(paramURL, paramString, iOException);
/*     */       } 
/* 654 */       bool = true;
/* 655 */     } catch (IOException iOException) {
/*     */       
/* 657 */       Trace.println("Got exception while patching: " + iOException, TraceLevel.NETWORK);
/*     */       
/* 659 */       throw new FailedDownloadingResourceException(paramURL, paramString, iOException);
/*     */     } finally {
/*     */       
/* 662 */       try { if (fileOutputStream != null) fileOutputStream.close();  }
/* 663 */       catch (IOException iOException) { Trace.ignoredException(iOException); }
/* 664 */        if (!bool) file.delete(); 
/* 665 */       paramFile2.delete();
/* 666 */       if (paramDownloadDelegate != null && !bool) paramDownloadDelegate.downloadFailed(paramURL, paramString); 
/*     */     } 
/* 668 */     return file;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static DiskCacheEntry getJRE(URL paramURL, String paramString1, boolean paramBoolean, String paramString2) throws JNLPException {
/* 674 */     DownloadInfo downloadInfo = new DownloadInfo(paramURL, paramString1, false, paramString2, paramBoolean);
/* 675 */     RetrieveAction retrieveAction = new RetrieveAction(null);
/* 676 */     doDownload(downloadInfo, retrieveAction);
/* 677 */     DiskCacheEntry diskCacheEntry = retrieveAction.getResult();
/* 678 */     if (diskCacheEntry == null) {
/* 679 */       throw new FailedDownloadingResourceException(paramURL, paramString1, null);
/*     */     }
/* 681 */     return diskCacheEntry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DiskCacheEntry getLaunchFile(URL paramURL, boolean paramBoolean) throws JNLPException {
/* 688 */     DownloadInfo downloadInfo = new DownloadInfo(paramURL, null, 3, false);
/* 689 */     RetrieveAction retrieveAction = new RetrieveAction(null);
/* 690 */     doDownload(downloadInfo, retrieveAction);
/* 691 */     DiskCacheEntry diskCacheEntry = retrieveAction.getResult();
/* 692 */     if (diskCacheEntry == null) {
/* 693 */       throw new FailedDownloadingResourceException(paramURL, null, null);
/*     */     }
/* 695 */     return diskCacheEntry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DiskCacheEntry getCachedLaunchedFile(URL paramURL) throws JNLPException {
/* 702 */     DownloadInfo downloadInfo = new DownloadInfo(paramURL, null, 3, true);
/* 703 */     IsInCacheAction isInCacheAction = new IsInCacheAction();
/* 704 */     doDownload(downloadInfo, isInCacheAction);
/* 705 */     return isInCacheAction.getResult();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isLaunchFileUpdateAvailable(URL paramURL) throws JNLPException {
/* 714 */     if (Globals.isOffline()) return false;
/*     */     
/* 716 */     DownloadInfo downloadInfo = new DownloadInfo(paramURL, null, 3, false);
/* 717 */     UpdateAvailableAction updateAvailableAction = new UpdateAvailableAction();
/* 718 */     doDownload(downloadInfo, updateAvailableAction);
/*     */     
/* 720 */     return updateAvailableAction.getResult();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DiskCacheEntry getExtension(URL paramURL, String paramString1, String paramString2, boolean paramBoolean) throws JNLPException {
/* 727 */     DownloadInfo downloadInfo = new DownloadInfo(paramURL, paramString1, paramBoolean, paramString2, false);
/* 728 */     RetrieveAction retrieveAction = new RetrieveAction(null);
/* 729 */     doDownload(downloadInfo, retrieveAction);
/* 730 */     DiskCacheEntry diskCacheEntry = retrieveAction.getResult();
/* 731 */     if (diskCacheEntry == null) {
/* 732 */       throw new FailedDownloadingResourceException(paramURL, paramString1, null);
/*     */     }
/* 734 */     return diskCacheEntry;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static DiskCacheEntry getCachedExtension(URL paramURL, String paramString1, String paramString2) throws JNLPException {
/* 740 */     DownloadInfo downloadInfo = new DownloadInfo(paramURL, paramString1, true, paramString2, false);
/* 741 */     IsInCacheAction isInCacheAction = new IsInCacheAction();
/* 742 */     doDownload(downloadInfo, isInCacheAction);
/* 743 */     return isInCacheAction.getResult();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isExtensionUpdateAvailable(URL paramURL, String paramString1, String paramString2) throws JNLPException {
/* 750 */     if (Globals.isOffline()) return false; 
/* 751 */     DownloadInfo downloadInfo = new DownloadInfo(paramURL, paramString1, false, paramString2, false);
/* 752 */     UpdateAvailableAction updateAvailableAction = new UpdateAvailableAction();
/* 753 */     doDownload(downloadInfo, updateAvailableAction);
/* 754 */     return updateAvailableAction.getResult();
/*     */   }
/*     */ 
/*     */   
/*     */   public static DiskCacheEntry getResource(URL paramURL, String paramString, int paramInt, boolean paramBoolean, DownloadDelegate paramDownloadDelegate) throws JNLPException {
/* 759 */     DownloadInfo downloadInfo = new DownloadInfo(paramURL, paramString, paramInt, paramBoolean);
/* 760 */     RetrieveAction retrieveAction = new RetrieveAction(paramDownloadDelegate);
/* 761 */     doDownload(downloadInfo, retrieveAction);
/* 762 */     DiskCacheEntry diskCacheEntry = retrieveAction.getResult();
/* 763 */     if (diskCacheEntry == null) {
/* 764 */       throw new FailedDownloadingResourceException(paramURL, paramString, null);
/*     */     }
/* 766 */     return diskCacheEntry;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isInCache(URL paramURL, String paramString, int paramInt) {
/* 771 */     return (getCachedVersion(paramURL, paramString, paramInt) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static long getCachedSize(URL paramURL, String paramString, int paramInt) {
/* 776 */     DiskCacheEntry diskCacheEntry = getCachedVersion(paramURL, paramString, paramInt);
/* 777 */     return (diskCacheEntry != null) ? diskCacheEntry.getSize() : 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public static DiskCacheEntry getCachedVersion(URL paramURL, String paramString, int paramInt) {
/*     */     try {
/* 783 */       DownloadInfo downloadInfo = new DownloadInfo(paramURL, paramString, paramInt, true);
/* 784 */       IsInCacheAction isInCacheAction = new IsInCacheAction();
/* 785 */       doDownload(downloadInfo, isInCacheAction);
/*     */       
/* 787 */       return isInCacheAction.getResult();
/*     */     }
/* 789 */     catch (JNLPException jNLPException) {
/* 790 */       Trace.ignoredException((Exception)jNLPException);
/* 791 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isUpdateAvailable(URL paramURL, String paramString, int paramInt) throws JNLPException {
/* 798 */     if (Globals.isOffline()) return false;
/*     */     
/* 800 */     DownloadInfo downloadInfo = new DownloadInfo(paramURL, paramString, paramInt, false);
/* 801 */     UpdateAvailableAction updateAvailableAction = new UpdateAvailableAction();
/* 802 */     doDownload(downloadInfo, updateAvailableAction);
/* 803 */     return updateAvailableAction.getResult();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getDownloadSize(URL paramURL, String paramString, int paramInt) throws JNLPException {
/* 812 */     DownloadInfo downloadInfo = new DownloadInfo(paramURL, paramString, paramInt, false);
/* 813 */     DownloadSizeAction downloadSizeAction = new DownloadSizeAction();
/* 814 */     doDownload(downloadInfo, downloadSizeAction);
/* 815 */     return downloadSizeAction.getResult();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static DiskCacheEntry findBestDiskCacheEntry(char paramChar, URL paramURL, String paramString, boolean[] paramArrayOfboolean) throws IOException {
/* 826 */     if (paramString == null) {
/* 827 */       paramArrayOfboolean[0] = true;
/* 828 */       return Cache.getCacheEntry(paramChar, paramURL, null);
/*     */     } 
/* 830 */     VersionString versionString = new VersionString(paramString);
/* 831 */     if (versionString.isSimpleVersion()) {
/* 832 */       DiskCacheEntry diskCacheEntry = Cache.getCacheEntry(paramChar, paramURL, paramString);
/* 833 */       if (diskCacheEntry != null) {
/* 834 */         paramArrayOfboolean[0] = true;
/* 835 */         return diskCacheEntry;
/*     */       } 
/*     */     } 
/* 838 */     String str1 = null;
/* 839 */     String str2 = null;
/*     */ 
/*     */     
/* 842 */     String[] arrayOfString = Cache.getCacheVersions(paramChar, paramURL);
/*     */     
/* 844 */     for (byte b = 0; b < arrayOfString.length; b++) {
/* 845 */       if (versionString.contains(arrayOfString[b])) {
/* 846 */         str2 = arrayOfString[b]; break;
/*     */       } 
/* 848 */       if (versionString.containsGreaterThan(arrayOfString[b]) && 
/* 849 */         str1 == null) {
/* 850 */         str1 = arrayOfString[b];
/*     */       }
/*     */     } 
/*     */     
/* 854 */     if (str2 == null) {
/* 855 */       paramArrayOfboolean[0] = false;
/* 856 */       if (str1 == null)
/*     */       {
/* 858 */         return null;
/*     */       }
/* 860 */       str2 = str1;
/*     */     } else {
/* 862 */       paramArrayOfboolean[0] = true;
/*     */     } 
/* 864 */     return Cache.getCacheEntry(paramChar, paramURL, str2);
/*     */   }
/*     */   
/*     */   private static interface DownloadAction {
/*     */     void actionInCache(DiskCacheEntry param1DiskCacheEntry) throws IOException, JNLPException;
/*     */     
/*     */     void actionOffline(DiskCacheEntry param1DiskCacheEntry, boolean param1Boolean) throws IOException, JNLPException;
/*     */     
/*     */     boolean skipDownloadStep();
/*     */     
/*     */     void actionDownload(DiskCacheEntry param1DiskCacheEntry, DownloadProtocol.DownloadInfo param1DownloadInfo, long param1Long, int param1Int, String param1String1, String param1String2, HttpResponse param1HttpResponse) throws IOException, JNLPException;
/*     */     
/*     */     boolean useHeadRequest();
/*     */   }
/*     */   
/*     */   public static interface DownloadDelegate {
/*     */     void downloading(URL param1URL, String param1String, int param1Int1, int param1Int2, boolean param1Boolean);
/*     */     
/*     */     void validating(URL param1URL, int param1Int1, int param1Int2);
/*     */     
/*     */     void patching(URL param1URL, String param1String, int param1Int);
/*     */     
/*     */     void downloadFailed(URL param1URL, String param1String);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\cache\DownloadProtocol.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */