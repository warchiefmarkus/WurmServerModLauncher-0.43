/*     */ package com.sun.jnlp;
/*     */ 
/*     */ import com.sun.javaws.LaunchDownload;
/*     */ import com.sun.javaws.cache.Cache;
/*     */ import com.sun.javaws.cache.DiskCacheEntry;
/*     */ import com.sun.javaws.cache.DownloadProtocol;
/*     */ import com.sun.javaws.exceptions.JNLPException;
/*     */ import com.sun.javaws.jnl.JARDesc;
/*     */ import com.sun.javaws.jnl.LaunchDesc;
/*     */ import com.sun.javaws.jnl.ResourcesDesc;
/*     */ import com.sun.javaws.ui.DownloadWindow;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import javax.jnlp.DownloadService;
/*     */ import javax.jnlp.DownloadServiceListener;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DownloadServiceImpl
/*     */   implements DownloadService
/*     */ {
/*  31 */   private static DownloadServiceImpl _sharedInstance = null;
/*  32 */   private DownloadServiceListener _defaultProgress = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized DownloadServiceImpl getInstance() {
/*  40 */     initialize();
/*  41 */     return _sharedInstance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized void initialize() {
/*  49 */     if (_sharedInstance == null) {
/*  50 */       _sharedInstance = new DownloadServiceImpl();
/*     */     }
/*     */   }
/*     */   
/*     */   public DownloadServiceListener getDefaultProgressWindow() {
/*  55 */     if (this._defaultProgress == null) {
/*  56 */       this._defaultProgress = AccessController.<DownloadServiceListener>doPrivileged(new PrivilegedAction(this) { private final DownloadServiceImpl this$0;
/*     */             
/*     */             public Object run() {
/*  59 */               return new DownloadServiceImpl.DefaultProgressImpl(this.this$0, new DownloadWindow(JNLPClassLoader.getInstance().getLaunchDesc(), false));
/*     */             } }
/*     */         );
/*     */     }
/*  63 */     return this._defaultProgress;
/*     */   }
/*     */   
/*     */   private class DefaultProgressImpl implements DownloadServiceListener { private DownloadWindow _dw;
/*     */     private final DownloadServiceImpl this$0;
/*     */     
/*     */     DefaultProgressImpl(DownloadServiceImpl this$0, DownloadWindow param1DownloadWindow) {
/*  70 */       this.this$0 = this$0; this._dw = null;
/*  71 */       AccessController.doPrivileged(new PrivilegedAction(this, this$0, param1DownloadWindow) { private final DownloadServiceImpl val$this$0; private final DownloadWindow val$dw; private final DownloadServiceImpl.DefaultProgressImpl this$1;
/*     */             public Object run() {
/*  73 */               this.this$1._dw = this.val$dw;
/*  74 */               this.this$1._dw.buildIntroScreen();
/*  75 */               this.this$1._dw.showLoadingProgressScreen();
/*  76 */               return null;
/*     */             } }
/*     */         );
/*     */     }
/*     */     
/*     */     public void progress(URL param1URL, String param1String, long param1Long1, long param1Long2, int param1Int) {
/*  82 */       ensureVisible();
/*  83 */       if (param1Long1 == 0L) this._dw.resetDownloadTimer(); 
/*  84 */       this._dw.progress(param1URL, param1String, param1Long1, param1Long2, param1Int);
/*  85 */       if (param1Int >= 100) hideFrame();
/*     */ 
/*     */       
/*  88 */       if (this._dw.isCanceled()) {
/*  89 */         hideFrame();
/*  90 */         throw new RuntimeException("canceled by user");
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void validating(URL param1URL, String param1String, long param1Long1, long param1Long2, int param1Int) {
/*  96 */       ensureVisible();
/*  97 */       this._dw.validating(param1URL, param1String, param1Long1, param1Long2, param1Int);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 105 */       if (param1Long1 >= param1Long2 && (param1Int < 0 || param1Int >= 99)) hideFrame(); 
/*     */     }
/*     */     
/*     */     public void upgradingArchive(URL param1URL, String param1String, int param1Int1, int param1Int2) {
/* 109 */       ensureVisible();
/* 110 */       this._dw.patching(param1URL, param1String, param1Int1, param1Int2);
/* 111 */       if (param1Int2 >= 100) hideFrame();
/*     */     
/*     */     }
/*     */     
/*     */     public void downloadFailed(URL param1URL, String param1String) {
/* 116 */       hideFrame();
/*     */     }
/*     */     
/*     */     private void ensureVisible() {
/* 120 */       if (!this._dw.getFrame().isVisible()) {
/* 121 */         this._dw.getFrame().setVisible(true);
/* 122 */         this._dw.getFrame().toFront();
/*     */       } 
/*     */     }
/*     */     
/*     */     private synchronized void hideFrame() {
/* 127 */       this._dw.resetCancled();
/* 128 */       this._dw.getFrame().hide();
/*     */     } }
/*     */ 
/*     */   
/*     */   public boolean isResourceCached(URL paramURL, String paramString) {
/* 133 */     Boolean bool = AccessController.<Boolean>doPrivileged(new PrivilegedAction(this, paramURL, paramString) { private final URL val$ref; private final String val$version; private final DownloadServiceImpl this$0;
/*     */           public Object run() {
/* 135 */             if (DownloadProtocol.isInCache(this.val$ref, this.val$version, 0) || DownloadProtocol.isInCache(this.val$ref, this.val$version, 1))
/*     */             {
/*     */ 
/*     */               
/* 139 */               return Boolean.TRUE;
/*     */             }
/* 141 */             return Boolean.FALSE;
/*     */           } }
/*     */       );
/* 144 */     return bool.booleanValue();
/*     */   }
/*     */   
/*     */   public boolean isPartCached(String paramString) {
/* 148 */     return isPartCached(new String[] { paramString });
/*     */   }
/*     */   
/*     */   public boolean isPartCached(String[] paramArrayOfString) {
/* 152 */     Boolean bool = AccessController.<Boolean>doPrivileged(new PrivilegedAction(this, paramArrayOfString) { private final String[] val$parts; private final DownloadServiceImpl this$0;
/*     */           public Object run() {
/* 154 */             LaunchDesc launchDesc = JNLPClassLoader.getInstance().getLaunchDesc();
/* 155 */             ResourcesDesc resourcesDesc = launchDesc.getResources();
/* 156 */             if (resourcesDesc == null) return Boolean.FALSE; 
/* 157 */             JARDesc[] arrayOfJARDesc = resourcesDesc.getPartJars(this.val$parts);
/* 158 */             return new Boolean(this.this$0.isJARInCache(arrayOfJARDesc, true));
/*     */           } }
/*     */       );
/* 161 */     return bool.booleanValue();
/*     */   }
/*     */   
/*     */   public boolean isExtensionPartCached(URL paramURL, String paramString1, String paramString2) {
/* 165 */     return isExtensionPartCached(paramURL, paramString1, new String[] { paramString2 });
/*     */   }
/*     */   
/*     */   public boolean isExtensionPartCached(URL paramURL, String paramString, String[] paramArrayOfString) {
/* 169 */     Boolean bool = AccessController.<Boolean>doPrivileged(new PrivilegedAction(this, paramURL, paramString, paramArrayOfString) { private final URL val$ref; private final String val$version; private final String[] val$parts; private final DownloadServiceImpl this$0;
/*     */           public Object run() {
/* 171 */             LaunchDesc launchDesc = JNLPClassLoader.getInstance().getLaunchDesc();
/* 172 */             ResourcesDesc resourcesDesc = launchDesc.getResources();
/* 173 */             if (resourcesDesc == null) return Boolean.FALSE; 
/* 174 */             JARDesc[] arrayOfJARDesc = resourcesDesc.getExtensionPart(this.val$ref, this.val$version, this.val$parts);
/* 175 */             return new Boolean(this.this$0.isJARInCache(arrayOfJARDesc, true));
/*     */           } }
/*     */       );
/* 178 */     return bool.booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadResource(URL paramURL, String paramString, DownloadServiceListener paramDownloadServiceListener) throws IOException {
/* 183 */     if (isResourceCached(paramURL, paramString))
/*     */       return;  try {
/* 185 */       AccessController.doPrivileged(new PrivilegedExceptionAction(this, paramURL, paramString, paramDownloadServiceListener) { private final URL val$ref; private final String val$version; private final DownloadServiceListener val$progress; private final DownloadServiceImpl this$0;
/*     */             public Object run() throws IOException {
/*     */               try {
/* 188 */                 JNLPClassLoader.getInstance().downloadResource(this.val$ref, this.val$version, new DownloadServiceImpl.ProgressHelper(this.this$0, this.val$progress), true);
/* 189 */               } catch (JNLPException jNLPException) {
/* 190 */                 throw new IOException(jNLPException.getMessage());
/*     */               } 
/* 192 */               return null;
/*     */             } }
/*     */         );
/* 195 */     } catch (PrivilegedActionException privilegedActionException) {
/* 196 */       throw (IOException)privilegedActionException.getException();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void loadPart(String paramString, DownloadServiceListener paramDownloadServiceListener) throws IOException {
/* 201 */     loadPart(new String[] { paramString }, paramDownloadServiceListener);
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadPart(String[] paramArrayOfString, DownloadServiceListener paramDownloadServiceListener) throws IOException {
/* 206 */     if (isPartCached(paramArrayOfString))
/*     */       return;  try {
/* 208 */       AccessController.doPrivileged(new PrivilegedExceptionAction(this, paramArrayOfString, paramDownloadServiceListener) { private final String[] val$parts; private final DownloadServiceListener val$progress; private final DownloadServiceImpl this$0;
/*     */             public Object run() throws IOException {
/*     */               try {
/* 211 */                 JNLPClassLoader.getInstance().downloadParts(this.val$parts, new DownloadServiceImpl.ProgressHelper(this.this$0, this.val$progress), true);
/* 212 */               } catch (JNLPException jNLPException) {
/* 213 */                 throw new IOException(jNLPException.getMessage());
/*     */               } 
/* 215 */               return null;
/*     */             } }
/*     */         );
/* 218 */     } catch (PrivilegedActionException privilegedActionException) {
/* 219 */       throw (IOException)privilegedActionException.getException();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void loadExtensionPart(URL paramURL, String paramString1, String paramString2, DownloadServiceListener paramDownloadServiceListener) throws IOException {
/* 224 */     loadExtensionPart(paramURL, paramString1, new String[] { paramString2 }, paramDownloadServiceListener);
/*     */   }
/*     */   
/*     */   public void loadExtensionPart(URL paramURL, String paramString, String[] paramArrayOfString, DownloadServiceListener paramDownloadServiceListener) throws IOException {
/*     */     try {
/* 229 */       AccessController.doPrivileged(new PrivilegedExceptionAction(this, paramURL, paramString, paramArrayOfString, paramDownloadServiceListener) { private final URL val$ref; private final String val$version;
/*     */             public Object run() throws IOException {
/*     */               try {
/* 232 */                 JNLPClassLoader.getInstance().downloadExtensionParts(this.val$ref, this.val$version, this.val$parts, new DownloadServiceImpl.ProgressHelper(this.this$0, this.val$progress), true);
/* 233 */               } catch (JNLPException jNLPException) {
/* 234 */                 throw new IOException(jNLPException.getMessage());
/*     */               } 
/* 236 */               return null;
/*     */             } private final String[] val$parts; private final DownloadServiceListener val$progress; private final DownloadServiceImpl this$0; }
/*     */         );
/* 239 */     } catch (PrivilegedActionException privilegedActionException) {
/* 240 */       throw (IOException)privilegedActionException.getException();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void removeResource(URL paramURL, String paramString) throws IOException {
/*     */     try {
/* 246 */       AccessController.doPrivileged(new PrivilegedExceptionAction(this, paramURL, paramString) { private final URL val$ref; private final String val$version; private final DownloadServiceImpl this$0;
/*     */             public Object run() throws IOException {
/* 248 */               LaunchDesc launchDesc = JNLPClassLoader.getInstance().getLaunchDesc();
/* 249 */               ResourcesDesc resourcesDesc = launchDesc.getResources();
/* 250 */               if (resourcesDesc == null) return null; 
/* 251 */               JARDesc[] arrayOfJARDesc = resourcesDesc.getResource(this.val$ref, this.val$version);
/* 252 */               this.this$0.removeJARFromCache(arrayOfJARDesc);
/* 253 */               return null;
/*     */             } }
/*     */         );
/* 256 */     } catch (PrivilegedActionException privilegedActionException) {
/* 257 */       throw (IOException)privilegedActionException.getException();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void removePart(String paramString) throws IOException {
/* 262 */     removePart(new String[] { paramString });
/*     */   }
/*     */   
/*     */   public void removePart(String[] paramArrayOfString) throws IOException {
/*     */     try {
/* 267 */       AccessController.doPrivileged(new PrivilegedExceptionAction(this, paramArrayOfString) { private final String[] val$parts; private final DownloadServiceImpl this$0;
/*     */             public Object run() throws IOException {
/* 269 */               LaunchDesc launchDesc = JNLPClassLoader.getInstance().getLaunchDesc();
/* 270 */               ResourcesDesc resourcesDesc = launchDesc.getResources();
/* 271 */               if (resourcesDesc == null) return null; 
/* 272 */               JARDesc[] arrayOfJARDesc = resourcesDesc.getPartJars(this.val$parts);
/* 273 */               this.this$0.removeJARFromCache(arrayOfJARDesc);
/* 274 */               return null;
/*     */             } }
/*     */         );
/* 277 */     } catch (PrivilegedActionException privilegedActionException) {
/* 278 */       throw (IOException)privilegedActionException.getException();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void removeExtensionPart(URL paramURL, String paramString1, String paramString2) throws IOException {
/* 283 */     removeExtensionPart(paramURL, paramString1, new String[] { paramString2 });
/*     */   }
/*     */   
/*     */   public void removeExtensionPart(URL paramURL, String paramString, String[] paramArrayOfString) throws IOException {
/*     */     try {
/* 288 */       AccessController.doPrivileged(new PrivilegedExceptionAction(this, paramURL, paramString, paramArrayOfString) { private final URL val$ref; private final String val$version; private final String[] val$parts; private final DownloadServiceImpl this$0;
/*     */             public Object run() throws IOException {
/* 290 */               LaunchDesc launchDesc = JNLPClassLoader.getInstance().getLaunchDesc();
/* 291 */               ResourcesDesc resourcesDesc = launchDesc.getResources();
/* 292 */               if (resourcesDesc == null) return null; 
/* 293 */               JARDesc[] arrayOfJARDesc = resourcesDesc.getExtensionPart(this.val$ref, this.val$version, this.val$parts);
/* 294 */               this.this$0.removeJARFromCache(arrayOfJARDesc);
/* 295 */               return null;
/*     */             } }
/*     */         );
/* 298 */     } catch (PrivilegedActionException privilegedActionException) {
/* 299 */       throw (IOException)privilegedActionException.getException();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void removeJARFromCache(JARDesc[] paramArrayOfJARDesc) throws IOException {
/* 304 */     if (paramArrayOfJARDesc == null)
/* 305 */       return;  if (paramArrayOfJARDesc.length == 0)
/* 306 */       return;  DiskCacheEntry diskCacheEntry = null;
/* 307 */     for (byte b = 0; b < paramArrayOfJARDesc.length; b++) {
/* 308 */       boolean bool = paramArrayOfJARDesc[b].isNativeLib() ? true : false;
/*     */       
/*     */       try {
/* 311 */         diskCacheEntry = DownloadProtocol.getResource(paramArrayOfJARDesc[b].getLocation(), paramArrayOfJARDesc[b].getVersion(), bool, true, null);
/*     */       
/*     */       }
/* 314 */       catch (JNLPException jNLPException) {
/* 315 */         throw new IOException(jNLPException.getMessage());
/*     */       } 
/* 317 */       if (diskCacheEntry != null) {
/* 318 */         Cache.removeEntry(diskCacheEntry);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isJARInCache(JARDesc[] paramArrayOfJARDesc, boolean paramBoolean) {
/* 330 */     if (paramArrayOfJARDesc == null) return false; 
/* 331 */     if (paramArrayOfJARDesc.length == 0) return false; 
/* 332 */     boolean bool = true;
/* 333 */     for (byte b = 0; b < paramArrayOfJARDesc.length; b++) {
/* 334 */       if (paramArrayOfJARDesc[b].isNativeLib()) {
/* 335 */         if (DownloadProtocol.isInCache(paramArrayOfJARDesc[b].getLocation(), paramArrayOfJARDesc[b].getVersion(), 1)) {
/*     */ 
/*     */           
/* 338 */           if (!paramBoolean) return true; 
/*     */         } else {
/* 340 */           bool = false;
/*     */         }
/*     */       
/*     */       }
/* 344 */       else if (DownloadProtocol.isInCache(paramArrayOfJARDesc[b].getLocation(), paramArrayOfJARDesc[b].getVersion(), 0)) {
/*     */ 
/*     */         
/* 347 */         if (!paramBoolean) return true; 
/*     */       } else {
/* 349 */         bool = false;
/*     */       } 
/*     */     } 
/*     */     
/* 353 */     return bool;
/*     */   }
/*     */   
/*     */   private class ProgressHelper implements LaunchDownload.DownloadProgress { DownloadServiceListener _dsp;
/*     */     private final DownloadServiceImpl this$0;
/*     */     
/*     */     public ProgressHelper(DownloadServiceImpl this$0, DownloadServiceListener param1DownloadServiceListener) {
/* 360 */       this.this$0 = this$0; this._dsp = null;
/* 361 */       this._dsp = param1DownloadServiceListener;
/*     */       
/* 363 */       this._dsp.progress(null, null, 0L, 0L, -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void extensionDownload(String param1String, int param1Int) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void jreDownload(String param1String, URL param1URL) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void progress(URL param1URL, String param1String, long param1Long1, long param1Long2, int param1Int) {
/* 377 */       if (this._dsp != null)
/* 378 */         this._dsp.progress(param1URL, param1String, param1Long1, param1Long2, param1Int); 
/*     */     }
/*     */     
/*     */     public void validating(URL param1URL, String param1String, long param1Long1, long param1Long2, int param1Int) {
/* 382 */       if (this._dsp != null)
/* 383 */         this._dsp.validating(param1URL, param1String, param1Long1, param1Long2, param1Int); 
/*     */     }
/*     */     
/*     */     public void patching(URL param1URL, String param1String, int param1Int1, int param1Int2) {
/* 387 */       if (this._dsp != null)
/* 388 */         this._dsp.upgradingArchive(param1URL, param1String, param1Int1, param1Int2); 
/*     */     }
/*     */     
/*     */     public void downloadFailed(URL param1URL, String param1String) {
/* 392 */       if (this._dsp != null)
/* 393 */         this._dsp.downloadFailed(param1URL, param1String); 
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\jnlp\DownloadServiceImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */