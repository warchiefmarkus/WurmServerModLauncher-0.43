/*     */ package com.sun.jnlp;
/*     */ 
/*     */ import com.sun.deploy.config.Config;
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.deploy.util.TraceLevel;
/*     */ import com.sun.javaws.Globals;
/*     */ import com.sun.javaws.LaunchDownload;
/*     */ import com.sun.javaws.Main;
/*     */ import com.sun.javaws.cache.Cache;
/*     */ import com.sun.javaws.exceptions.JNLPException;
/*     */ import com.sun.javaws.jnl.JARDesc;
/*     */ import com.sun.javaws.jnl.LaunchDesc;
/*     */ import com.sun.javaws.security.AppPolicy;
/*     */ import com.sun.javaws.security.JNLPClassPath;
/*     */ import com.sun.javaws.security.Resource;
/*     */ import java.awt.AWTPermission;
/*     */ import java.io.File;
/*     */ import java.io.FilePermission;
/*     */ import java.io.IOException;
/*     */ import java.net.SocketPermission;
/*     */ import java.net.URL;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.CodeSource;
/*     */ import java.security.Permission;
/*     */ import java.security.PermissionCollection;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.security.SecureClassLoader;
/*     */ import java.util.Enumeration;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.Manifest;
/*     */ import sun.awt.AppContext;
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
/*     */ public final class JNLPClassLoader
/*     */   extends SecureClassLoader
/*     */ {
/*  56 */   private static JNLPClassLoader _instance = null;
/*     */ 
/*     */   
/*  59 */   private LaunchDesc _launchDesc = null;
/*     */ 
/*     */   
/*  62 */   private JNLPClassPath _jcp = null;
/*     */ 
/*     */   
/*     */   private AppPolicy _appPolicy;
/*     */ 
/*     */   
/*  68 */   private AccessControlContext _acc = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean _initialized = false;
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
/*     */   public JNLPClassLoader(ClassLoader paramClassLoader) {
/*  90 */     super(paramClassLoader);
/*     */     
/*  92 */     SecurityManager securityManager = System.getSecurityManager();
/*  93 */     if (securityManager != null) {
/*  94 */       securityManager.checkCreateClassLoader();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initialize(LaunchDesc paramLaunchDesc, boolean paramBoolean, AppPolicy paramAppPolicy) {
/* 102 */     this._launchDesc = paramLaunchDesc;
/* 103 */     this._jcp = new JNLPClassPath(paramLaunchDesc, paramBoolean);
/* 104 */     this._acc = AccessController.getContext();
/* 105 */     this._appPolicy = paramAppPolicy;
/* 106 */     this._initialized = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static synchronized JNLPClassLoader createClassLoader() {
/* 111 */     if (_instance == null) {
/*     */ 
/*     */       
/* 114 */       ClassLoader classLoader = ClassLoader.getSystemClassLoader();
/* 115 */       if (classLoader instanceof JNLPClassLoader) {
/* 116 */         _instance = (JNLPClassLoader)classLoader;
/*     */       } else {
/* 118 */         _instance = new JNLPClassLoader(classLoader);
/*     */       } 
/*     */     } 
/* 121 */     return _instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized JNLPClassLoader createClassLoader(LaunchDesc paramLaunchDesc, AppPolicy paramAppPolicy) {
/* 128 */     JNLPClassLoader jNLPClassLoader = createClassLoader();
/*     */     
/* 130 */     if (!jNLPClassLoader._initialized) {
/* 131 */       jNLPClassLoader.initialize(paramLaunchDesc, paramLaunchDesc.isApplet(), paramAppPolicy);
/*     */     }
/* 133 */     return jNLPClassLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public static synchronized JNLPClassLoader getInstance() {
/* 138 */     return _instance;
/*     */   }
/*     */ 
/*     */   
/*     */   public LaunchDesc getLaunchDesc() {
/* 143 */     return this._launchDesc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void downloadResource(URL paramURL, String paramString, LaunchDownload.DownloadProgress paramDownloadProgress, boolean paramBoolean) throws JNLPException, IOException {
/* 151 */     LaunchDownload.downloadResource(this._launchDesc, paramURL, paramString, paramDownloadProgress, paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public void downloadParts(String[] paramArrayOfString, LaunchDownload.DownloadProgress paramDownloadProgress, boolean paramBoolean) throws JNLPException, IOException {
/* 156 */     LaunchDownload.downloadParts(this._launchDesc, paramArrayOfString, paramDownloadProgress, paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public void downloadExtensionParts(URL paramURL, String paramString, String[] paramArrayOfString, LaunchDownload.DownloadProgress paramDownloadProgress, boolean paramBoolean) throws JNLPException, IOException {
/* 161 */     LaunchDownload.downloadExtensionPart(this._launchDesc, paramURL, paramString, paramArrayOfString, paramDownloadProgress, paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public void downloadEager(LaunchDownload.DownloadProgress paramDownloadProgress, boolean paramBoolean) throws JNLPException, IOException {
/* 166 */     LaunchDownload.downloadEagerorAll(this._launchDesc, false, paramDownloadProgress, paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JARDesc getJarDescFromFileURL(URL paramURL) {
/* 172 */     return this._jcp.getJarDescFromFileURL(paramURL);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultSecurityModel() {
/* 177 */     return this._launchDesc.getSecurityModel();
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
/*     */   public URL getResource(String paramString) {
/* 189 */     URL uRL = null;
/* 190 */     for (byte b = 0; uRL == null && b < 3; b++) {
/*     */       
/* 192 */       Trace.println("Looking up resource: " + paramString + " (attempt: " + b + ")", TraceLevel.BASIC);
/*     */       
/* 194 */       uRL = super.getResource(paramString);
/*     */     } 
/* 196 */     return uRL;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String findLibrary(String paramString) {
/* 202 */     if (!this._initialized) return super.findLibrary(paramString);
/*     */ 
/*     */     
/* 205 */     paramString = Config.getInstance().getLibraryPrefix() + paramString + Config.getInstance().getLibrarySufix();
/*     */ 
/*     */     
/* 208 */     Trace.println("Looking up native library: " + paramString, TraceLevel.BASIC);
/*     */ 
/*     */     
/* 211 */     File[] arrayOfFile = LaunchDownload.getNativeDirectories(this._launchDesc);
/* 212 */     for (byte b = 0; b < arrayOfFile.length; b++) {
/* 213 */       File file = new File(arrayOfFile[b], paramString);
/* 214 */       if (file.exists()) {
/*     */         
/* 216 */         Trace.println("Native library found: " + file.getAbsolutePath(), TraceLevel.BASIC);
/*     */         
/* 218 */         return file.getAbsolutePath();
/*     */       } 
/*     */     } 
/*     */     
/* 222 */     Trace.println("Native library not found", TraceLevel.BASIC);
/*     */     
/* 224 */     return super.findLibrary(paramString);
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
/*     */   
/*     */   protected Class findClass(String paramString) throws ClassNotFoundException {
/* 241 */     if (!this._initialized) return super.findClass(paramString);
/*     */ 
/*     */     
/* 244 */     Trace.println("Loading class " + paramString, TraceLevel.BASIC);
/*     */     
/*     */     try {
/* 247 */       return AccessController.<Class>doPrivileged(new PrivilegedExceptionAction(this, paramString) { private final String val$name; private final JNLPClassLoader this$0;
/*     */             
/*     */             public Object run() throws ClassNotFoundException {
/* 250 */               String str = this.val$name.replace('.', '/').concat(".class");
/* 251 */               Resource resource = this.this$0._jcp.getResource(str, false);
/* 252 */               if (resource != null) {
/*     */                 try {
/* 254 */                   return this.this$0.defineClass(this.val$name, resource);
/* 255 */                 } catch (IOException iOException) {
/* 256 */                   throw new ClassNotFoundException(this.val$name, iOException);
/*     */                 } 
/*     */               }
/* 259 */               throw new ClassNotFoundException(this.val$name);
/*     */             } }
/*     */           this._acc);
/*     */     }
/* 263 */     catch (PrivilegedActionException privilegedActionException) {
/* 264 */       throw (ClassNotFoundException)privilegedActionException.getException();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Class defineClass(String paramString, Resource paramResource) throws IOException {
/*     */     CodeSource codeSource;
/* 274 */     int i = paramString.lastIndexOf('.');
/* 275 */     URL uRL = paramResource.getCodeSourceURL();
/* 276 */     if (i != -1) {
/* 277 */       String str = paramString.substring(0, i);
/*     */       
/* 279 */       Package package_ = getPackage(str);
/* 280 */       Manifest manifest = paramResource.getManifest();
/* 281 */       if (package_ != null) {
/*     */         boolean bool;
/*     */         
/* 284 */         if (package_.isSealed()) {
/*     */           
/* 286 */           bool = package_.isSealed(uRL);
/*     */         }
/*     */         else {
/*     */           
/* 290 */           bool = (manifest == null || !isSealed(str, manifest)) ? true : false;
/*     */         } 
/* 292 */         if (!bool) {
/* 293 */           throw new SecurityException("sealing violation");
/*     */         }
/*     */       }
/* 296 */       else if (manifest != null) {
/* 297 */         definePackage(str, manifest, uRL);
/*     */       } else {
/* 299 */         definePackage(str, null, null, null, null, null, null, null);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 304 */     byte[] arrayOfByte = paramResource.getBytes();
/*     */ 
/*     */     
/* 307 */     if (Globals.isJavaVersionAtLeast15()) {
/*     */       
/* 309 */       codeSource = new CodeSource(uRL, paramResource.getCodeSigners());
/*     */     } else {
/*     */       
/* 312 */       codeSource = new CodeSource(uRL, paramResource.getCertificates());
/*     */     } 
/* 314 */     return defineClass(paramString, arrayOfByte, 0, arrayOfByte.length, codeSource);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Package definePackage(String paramString, Manifest paramManifest, URL paramURL) throws IllegalArgumentException {
/* 335 */     String str1 = paramString.replace('.', '/').concat("/");
/* 336 */     String str2 = null, str3 = null, str4 = null;
/* 337 */     String str5 = null, str6 = null, str7 = null;
/* 338 */     String str8 = null;
/* 339 */     URL uRL = null;
/*     */     
/* 341 */     Attributes attributes = paramManifest.getAttributes(str1);
/* 342 */     if (attributes != null) {
/* 343 */       str2 = attributes.getValue(Attributes.Name.SPECIFICATION_TITLE);
/* 344 */       str3 = attributes.getValue(Attributes.Name.SPECIFICATION_VERSION);
/* 345 */       str4 = attributes.getValue(Attributes.Name.SPECIFICATION_VENDOR);
/* 346 */       str5 = attributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
/* 347 */       str6 = attributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
/* 348 */       str7 = attributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
/* 349 */       str8 = attributes.getValue(Attributes.Name.SEALED);
/*     */     } 
/* 351 */     attributes = paramManifest.getMainAttributes();
/* 352 */     if (attributes != null) {
/* 353 */       if (str2 == null) {
/* 354 */         str2 = attributes.getValue(Attributes.Name.SPECIFICATION_TITLE);
/*     */       }
/* 356 */       if (str3 == null) {
/* 357 */         str3 = attributes.getValue(Attributes.Name.SPECIFICATION_VERSION);
/*     */       }
/* 359 */       if (str4 == null) {
/* 360 */         str4 = attributes.getValue(Attributes.Name.SPECIFICATION_VENDOR);
/*     */       }
/* 362 */       if (str5 == null) {
/* 363 */         str5 = attributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
/*     */       }
/* 365 */       if (str6 == null) {
/* 366 */         str6 = attributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
/*     */       }
/* 368 */       if (str7 == null) {
/* 369 */         str7 = attributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
/*     */       }
/* 371 */       if (str8 == null) {
/* 372 */         str8 = attributes.getValue(Attributes.Name.SEALED);
/*     */       }
/*     */     } 
/* 375 */     if ("true".equalsIgnoreCase(str8)) {
/* 376 */       uRL = paramURL;
/*     */     }
/* 378 */     return definePackage(paramString, str2, str3, str4, str5, str6, str7, uRL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isSealed(String paramString, Manifest paramManifest) {
/* 387 */     String str1 = paramString.replace('.', '/').concat("/");
/* 388 */     Attributes attributes = paramManifest.getAttributes(str1);
/* 389 */     String str2 = null;
/* 390 */     if (attributes != null) {
/* 391 */       str2 = attributes.getValue(Attributes.Name.SEALED);
/*     */     }
/* 393 */     if (str2 == null && (
/* 394 */       attributes = paramManifest.getMainAttributes()) != null) {
/* 395 */       str2 = attributes.getValue(Attributes.Name.SEALED);
/*     */     }
/*     */     
/* 398 */     return "true".equalsIgnoreCase(str2);
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
/*     */   public URL findResource(String paramString) {
/* 410 */     if (!this._initialized) return super.findResource(paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 415 */     Resource resource = AccessController.<Resource>doPrivileged(new PrivilegedAction(this, paramString) { private final String val$name; private final JNLPClassLoader this$0;
/*     */           
/*     */           public Object run() {
/* 418 */             return this.this$0._jcp.getResource(this.val$name, true);
/*     */           } }
/*     */         this._acc);
/* 421 */     return (resource != null) ? this._jcp.checkURL(resource.getURL()) : null;
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
/*     */   public Enumeration findResources(String paramString) throws IOException {
/* 434 */     if (!this._initialized) return super.findResources(paramString);
/*     */     
/* 436 */     Enumeration enumeration = AccessController.<Enumeration>doPrivileged(new PrivilegedAction(this, paramString) { private final String val$name; private final JNLPClassLoader this$0;
/*     */           public Object run() {
/* 438 */             return this.this$0._jcp.getResources(this.val$name, true);
/*     */           } }
/*     */         this._acc);
/*     */     
/* 442 */     return new Enumeration(this, enumeration) { private URL res; private final Enumeration val$e;
/*     */         private final JNLPClassLoader this$0;
/*     */         
/*     */         public Object nextElement() {
/* 446 */           if (this.res == null)
/* 447 */             throw new NoSuchElementException(); 
/* 448 */           URL uRL = this.res;
/* 449 */           this.res = null;
/* 450 */           return uRL;
/*     */         }
/*     */         
/*     */         public boolean hasMoreElements() {
/* 454 */           if (Thread.currentThread().getThreadGroup() == Main.getSecurityThreadGroup())
/*     */           {
/* 456 */             return false;
/*     */           }
/* 458 */           if (this.res != null)
/* 459 */             return true; 
/*     */           do {
/* 461 */             Resource resource = AccessController.<Resource>doPrivileged(new PrivilegedAction(this) { private final JNLPClassLoader.null this$1;
/*     */                   
/*     */                   public Object run() {
/* 464 */                     if (!this.this$1.val$e.hasMoreElements())
/* 465 */                       return null; 
/* 466 */                     return this.this$1.val$e.nextElement();
/*     */                   }
/*     */                 },  this.this$0._acc);
/* 469 */             if (resource == null)
/*     */               break; 
/* 471 */             this.res = this.this$0._jcp.checkURL(resource.getURL());
/* 472 */           } while (this.res == null);
/* 473 */           return (this.res != null);
/*     */         } }
/*     */       ;
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
/*     */   protected PermissionCollection getPermissions(CodeSource paramCodeSource) {
/*     */     Permission permission;
/* 506 */     PermissionCollection permissionCollection = super.getPermissions(paramCodeSource);
/*     */ 
/*     */     
/* 509 */     this._appPolicy.addPermissions(permissionCollection, paramCodeSource);
/*     */     
/* 511 */     URL uRL = paramCodeSource.getLocation();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 516 */       permission = uRL.openConnection().getPermission();
/* 517 */     } catch (IOException iOException) {
/* 518 */       permission = null;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 524 */     JARDesc jARDesc = this._jcp.getJarDescFromFileURL(uRL);
/* 525 */     if (jARDesc != null) {
/* 526 */       String[] arrayOfString = Cache.getBaseDirsForHost(jARDesc.getLocation());
/* 527 */       for (byte b = 0; b < arrayOfString.length; b++) {
/* 528 */         String str = arrayOfString[b];
/* 529 */         if (str != null) {
/* 530 */           if (str.endsWith(File.separator)) {
/* 531 */             str = str + '-';
/*     */           } else {
/* 533 */             str = str + File.separator + '-';
/*     */           } 
/* 535 */           permissionCollection.add(new FilePermission(str, "read"));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 540 */     if (permission instanceof FilePermission) {
/*     */ 
/*     */ 
/*     */       
/* 544 */       String str = permission.getName();
/* 545 */       if (str.endsWith(File.separator)) {
/* 546 */         str = str + "-";
/* 547 */         permission = new FilePermission(str, "read");
/*     */       } 
/* 549 */     } else if (permission == null && uRL.getProtocol().equals("file")) {
/* 550 */       String str = uRL.getFile().replace('/', File.separatorChar);
/* 551 */       if (str.endsWith(File.separator))
/* 552 */         str = str + "-"; 
/* 553 */       permission = new FilePermission(str, "read");
/*     */     } else {
/* 555 */       String str = uRL.getHost();
/* 556 */       if (str == null)
/* 557 */         str = "localhost"; 
/* 558 */       permission = new SocketPermission(str, "connect, accept");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 563 */     if (permission != null) {
/* 564 */       SecurityManager securityManager = System.getSecurityManager();
/* 565 */       if (securityManager != null) {
/* 566 */         Permission permission1 = permission;
/* 567 */         AccessController.doPrivileged(new PrivilegedAction(this, securityManager, permission1) { private final SecurityManager val$sm;
/*     */               public Object run() throws SecurityException {
/* 569 */                 this.val$sm.checkPermission(this.val$fp);
/* 570 */                 return null;
/*     */               } private final Permission val$fp; private final JNLPClassLoader this$0; }
/*     */             this._acc);
/*     */       } 
/* 574 */       permissionCollection.add(permission);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 581 */     if (!permissionCollection.implies(new AWTPermission("accessClipboard"))) {
/* 582 */       AppContext.getAppContext().put("UNTRUSTED_CLIPBOARD_ACCESS_KEY", Boolean.TRUE);
/*     */     }
/* 584 */     return permissionCollection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized Class loadClass(String paramString, boolean paramBoolean) throws ClassNotFoundException {
/* 593 */     SecurityManager securityManager = System.getSecurityManager();
/* 594 */     if (securityManager != null) {
/* 595 */       int i = paramString.lastIndexOf('.');
/* 596 */       if (i != -1) {
/* 597 */         securityManager.checkPackageAccess(paramString.substring(0, i));
/*     */       }
/*     */     } 
/* 600 */     return super.loadClass(paramString, paramBoolean);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\jnlp\JNLPClassLoader.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */