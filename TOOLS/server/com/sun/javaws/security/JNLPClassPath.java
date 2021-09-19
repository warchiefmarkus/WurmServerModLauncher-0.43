/*     */ package com.sun.javaws.security;
/*     */ 
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.deploy.util.TraceLevel;
/*     */ import com.sun.deploy.util.URLUtil;
/*     */ import com.sun.javaws.Globals;
/*     */ import com.sun.javaws.cache.DiskCacheEntry;
/*     */ import com.sun.javaws.cache.DownloadProtocol;
/*     */ import com.sun.javaws.exceptions.JNLPException;
/*     */ import com.sun.javaws.jnl.JARDesc;
/*     */ import com.sun.javaws.jnl.LaunchDesc;
/*     */ import com.sun.javaws.jnl.ResourcesDesc;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.security.AccessControlException;
/*     */ import java.security.AccessController;
/*     */ import java.security.CodeSigner;
/*     */ import java.security.Permission;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.ListIterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Stack;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
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
/*     */ public class JNLPClassPath
/*     */ {
/*  51 */   private Stack _pendingJarDescs = new Stack();
/*     */ 
/*     */   
/*  54 */   private ArrayList _loaders = new ArrayList();
/*     */ 
/*     */   
/*  57 */   private Loader _appletLoader = null;
/*     */ 
/*     */   
/*  60 */   private LaunchDesc _launchDesc = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   private HashMap _fileToUrls = new HashMap();
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
/*     */   public JNLPClassPath(LaunchDesc paramLaunchDesc, boolean paramBoolean) {
/*  78 */     this._launchDesc = paramLaunchDesc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  84 */     if (paramBoolean) {
/*  85 */       URL uRL = URLUtil.getBase(paramLaunchDesc.getCanonicalHome());
/*  86 */       Trace.println("Classpath: " + uRL, TraceLevel.BASIC);
/*  87 */       if ("file".equals(uRL.getProtocol())) {
/*  88 */         this._appletLoader = new FileDirectoryLoader(uRL);
/*     */       } else {
/*  90 */         this._appletLoader = new URLDirectoryLoader(uRL);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  96 */     ResourcesDesc resourcesDesc = paramLaunchDesc.getResources();
/*  97 */     if (resourcesDesc != null) {
/*     */       
/*  99 */       JARDesc[] arrayOfJARDesc = resourcesDesc.getEagerOrAllJarDescs(true);
/*     */       
/* 101 */       for (int i = arrayOfJARDesc.length - 1; i >= 0; i--) {
/* 102 */         if (arrayOfJARDesc[i].isJavaFile()) {
/* 103 */           Trace.println("Classpath: " + arrayOfJARDesc[i].getLocation() + ":" + arrayOfJARDesc[i].getVersion(), TraceLevel.BASIC);
/* 104 */           this._pendingJarDescs.add(arrayOfJARDesc[i]);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized JARDesc getJarDescFromFileURL(URL paramURL) {
/* 112 */     return (JARDesc)this._fileToUrls.get(paramURL.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadAllResources() {
/*     */     try {
/* 118 */       JARDesc jARDesc = getNextPendingJarDesc();
/* 119 */       while (jARDesc != null) {
/* 120 */         createLoader(jARDesc);
/* 121 */         jARDesc = getNextPendingJarDesc();
/*     */       } 
/* 123 */     } catch (IOException iOException) {
/* 124 */       Trace.ignoredException(iOException);
/*     */     } 
/*     */ 
/*     */     
/* 128 */     if (this._appletLoader != null) {
/* 129 */       synchronized (this._loaders) { this._loaders.add(this._appletLoader); }
/*     */     
/*     */     }
/*     */   }
/*     */   
/*     */   private synchronized JARDesc getNextPendingJarDesc() {
/* 135 */     return this._pendingJarDescs.isEmpty() ? null : this._pendingJarDescs.pop();
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized JARDesc getIfPendingJarDesc(JARDesc paramJARDesc) {
/* 140 */     if (this._pendingJarDescs.contains(paramJARDesc)) {
/* 141 */       this._pendingJarDescs.remove(paramJARDesc);
/* 142 */       return paramJARDesc;
/*     */     } 
/* 144 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private Loader createLoader(JARDesc paramJARDesc) throws IOException {
/*     */     try {
/* 150 */       return AccessController.<Loader>doPrivileged(new PrivilegedExceptionAction(this, paramJARDesc) { private final JARDesc val$jd; private final JNLPClassPath this$0;
/*     */             public Object run() throws IOException {
/* 152 */               return this.this$0.createLoaderHelper(this.val$jd);
/*     */             } }
/*     */         );
/* 155 */     } catch (PrivilegedActionException privilegedActionException) {
/* 156 */       Trace.println("Failed to create loader for: " + paramJARDesc + " (" + privilegedActionException.getException() + ")", TraceLevel.BASIC);
/* 157 */       throw (IOException)privilegedActionException.getException();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Loader createLoaderHelper(JARDesc paramJARDesc) throws IOException {
/* 164 */     URL uRL = paramJARDesc.getLocation();
/* 165 */     String str = paramJARDesc.getVersion();
/*     */     
/*     */     try {
/* 168 */       DiskCacheEntry diskCacheEntry = DownloadProtocol.getResource(uRL, str, 0, true, null);
/*     */       
/* 170 */       if (diskCacheEntry == null || !diskCacheEntry.getFile().exists()) {
/* 171 */         throw new IOException("Resource not found: " + paramJARDesc.getLocation() + ":" + paramJARDesc.getVersion());
/*     */       }
/*     */       
/* 174 */       String str1 = URLUtil.getEncodedPath(diskCacheEntry.getFile());
/* 175 */       URL uRL1 = new URL("file", "", str1);
/*     */ 
/*     */       
/* 178 */       Trace.println("Creating loader for: " + uRL1, TraceLevel.BASIC);
/* 179 */       JarLoader jarLoader = new JarLoader(uRL1);
/*     */       
/* 181 */       synchronized (this) {
/* 182 */         this._loaders.add(jarLoader);
/*     */         
/* 184 */         this._fileToUrls.put(uRL1.toString(), paramJARDesc);
/*     */       } 
/*     */       
/* 187 */       return jarLoader;
/*     */     }
/* 189 */     catch (JNLPException jNLPException) {
/* 190 */       Trace.println("Failed to download: " + jNLPException + " (" + jNLPException + ")", TraceLevel.BASIC);
/* 191 */       Trace.ignoredException((Exception)jNLPException);
/* 192 */       throw new IOException(jNLPException.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Resource findNamedResource(String paramString, boolean paramBoolean) throws IOException {
/* 199 */     Resource resource = findNamedResourceInLoaders(paramString, paramBoolean);
/* 200 */     if (resource != null) return resource;
/*     */ 
/*     */     
/* 203 */     synchronized (this) {
/* 204 */       if (this._pendingJarDescs.isEmpty()) return null;
/*     */     
/*     */     } 
/*     */     
/* 208 */     ResourcesDesc.PackageInformation packageInformation = this._launchDesc.getResources().getPackageInformation(paramString);
/* 209 */     if (packageInformation != null) {
/*     */       
/* 211 */       JARDesc[] arrayOfJARDesc = packageInformation.getLaunchDesc().getResources().getPart(packageInformation.getPart());
/*     */       
/* 213 */       for (byte b = 0; b < arrayOfJARDesc.length; b++) {
/*     */         
/* 215 */         JARDesc jARDesc = getIfPendingJarDesc(arrayOfJARDesc[b]);
/* 216 */         if (jARDesc != null) createLoader(jARDesc);
/*     */       
/*     */       } 
/* 219 */       resource = findNamedResourceInLoaders(paramString, paramBoolean);
/* 220 */       if (resource != null) return resource;
/*     */     
/*     */     } 
/* 223 */     synchronized (this) {
/*     */       
/* 225 */       ListIterator listIterator = this._pendingJarDescs.listIterator(this._pendingJarDescs.size());
/*     */       
/* 227 */       while (listIterator.hasPrevious()) {
/* 228 */         JARDesc jARDesc = listIterator.previous();
/*     */         
/* 230 */         if (!this._launchDesc.getResources().isPackagePart(jARDesc.getPartName())) {
/*     */           
/* 232 */           listIterator.remove();
/* 233 */           Loader loader = createLoader(jARDesc);
/* 234 */           resource = loader.getResource(paramString, paramBoolean);
/* 235 */           if (resource != null) {
/* 236 */             return resource;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 242 */     if (this._appletLoader != null) {
/* 243 */       resource = this._appletLoader.getResource(paramString, paramBoolean);
/*     */     }
/*     */     
/* 246 */     return resource;
/*     */   }
/*     */   
/*     */   private Resource findNamedResourceInLoaders(String paramString, boolean paramBoolean) throws IOException {
/* 250 */     int i = 0;
/*     */     
/* 252 */     synchronized (this) { i = this._loaders.size(); }
/* 253 */      for (byte b = 0; b < i; b++) {
/* 254 */       Loader loader = null;
/* 255 */       synchronized (this) { loader = this._loaders.get(b); }
/* 256 */        Resource resource = loader.getResource(paramString, paramBoolean);
/* 257 */       if (resource != null) return resource; 
/*     */     } 
/* 259 */     return null;
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
/*     */   public Resource getResource(String paramString, boolean paramBoolean) {
/* 271 */     Trace.println("getResource: " + paramString + " (check: " + paramBoolean + ")", TraceLevel.BASIC);
/*     */     
/*     */     try {
/* 274 */       return findNamedResource(paramString, paramBoolean);
/* 275 */     } catch (IOException iOException) {
/* 276 */       Trace.ignoredException(iOException);
/* 277 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Resource getResource(String paramString) {
/* 282 */     return getResource(paramString, true);
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
/*     */   public Enumeration getResources(String paramString, boolean paramBoolean) {
/*     */     int i;
/* 295 */     loadAllResources();
/*     */ 
/*     */     
/* 298 */     synchronized (this) { i = this._loaders.size(); }
/* 299 */      int j = i;
/*     */ 
/*     */     
/* 302 */     return new Enumeration(this, j, paramString, paramBoolean) { private int index; private Resource res; private final int val$size; private final String val$name;
/*     */         private final boolean val$check;
/*     */         private final JNLPClassPath this$0;
/*     */         
/*     */         private boolean next() {
/* 307 */           if (this.res != null) {
/* 308 */             return true;
/*     */           }
/*     */           
/* 311 */           while (this.index < this.val$size) {
/* 312 */             JNLPClassPath.Loader loader = this.this$0._loaders.get(this.index++);
/* 313 */             this.res = loader.getResource(this.val$name, this.val$check);
/* 314 */             if (this.res != null) {
/* 315 */               return true;
/*     */             }
/*     */           } 
/* 318 */           return false;
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean hasMoreElements() {
/* 323 */           return next();
/*     */         }
/*     */         
/*     */         public Object nextElement() {
/* 327 */           if (!next()) {
/* 328 */             throw new NoSuchElementException();
/*     */           }
/* 330 */           Resource resource = this.res;
/* 331 */           this.res = null;
/* 332 */           return resource;
/*     */         } }
/*     */       ;
/*     */   }
/*     */   
/*     */   public Enumeration getResources(String paramString) {
/* 338 */     return getResources(paramString, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL checkURL(URL paramURL) {
/*     */     try {
/* 348 */       check(paramURL);
/* 349 */     } catch (Exception exception) {
/* 350 */       return null;
/*     */     } 
/*     */     
/* 353 */     return paramURL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void check(URL paramURL) throws IOException {
/* 362 */     SecurityManager securityManager = System.getSecurityManager();
/* 363 */     if (securityManager != null) {
/* 364 */       Permission permission = paramURL.openConnection().getPermission();
/* 365 */       if (permission != null) {
/* 366 */         securityManager.checkPermission(permission);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class Loader
/*     */   {
/*     */     private final URL base;
/*     */ 
/*     */     
/*     */     Loader(URL param1URL) {
/* 379 */       this.base = param1URL;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Resource getResource(String param1String) {
/* 388 */       return getResource(param1String, true);
/*     */     }
/*     */     
/*     */     abstract Resource getResource(String param1String, boolean param1Boolean);
/*     */     
/*     */     URL getBaseURL() {
/* 394 */       return this.base;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class URLDirectoryLoader
/*     */     extends Loader
/*     */   {
/*     */     URLDirectoryLoader(URL param1URL) {
/* 404 */       super(param1URL);
/*     */     }
/*     */     
/*     */     Resource getResource(String param1String, boolean param1Boolean) {
/*     */       URL uRL;
/*     */       URLConnection uRLConnection;
/*     */       try {
/* 411 */         uRL = new URL(getBaseURL(), param1String);
/* 412 */       } catch (MalformedURLException malformedURLException) {
/* 413 */         throw new IllegalArgumentException("name");
/*     */       } 
/*     */       
/*     */       try {
/* 417 */         if (param1Boolean) {
/* 418 */           JNLPClassPath.check(uRL);
/*     */         }
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
/* 432 */         uRLConnection = uRL.openConnection();
/* 433 */         if (uRLConnection instanceof HttpURLConnection) {
/* 434 */           HttpURLConnection httpURLConnection = (HttpURLConnection)uRLConnection;
/* 435 */           int i = httpURLConnection.getResponseCode();
/* 436 */           httpURLConnection.disconnect();
/* 437 */           if (i >= 400) {
/* 438 */             return null;
/*     */           }
/*     */         } else {
/*     */           
/* 442 */           InputStream inputStream = uRL.openStream();
/* 443 */           inputStream.close();
/*     */         } 
/* 445 */       } catch (Exception exception) {
/* 446 */         return null;
/*     */       } 
/* 448 */       return new Resource(this, param1String, uRL, uRLConnection) {
/* 449 */           private final String val$name; private final URL val$url; public String getName() { return this.val$name; } private final URLConnection val$uc; private final JNLPClassPath.URLDirectoryLoader this$0; public URL getURL() {
/* 450 */             return this.val$url; } public URL getCodeSourceURL() {
/* 451 */             return this.this$0.getBaseURL();
/*     */           } public InputStream getInputStream() throws IOException {
/* 453 */             return this.val$uc.getInputStream();
/*     */           }
/*     */           public int getContentLength() throws IOException {
/* 456 */             return this.val$uc.getContentLength();
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class JarLoader
/*     */     extends Loader
/*     */   {
/*     */     private JarFile jar;
/*     */ 
/*     */     
/*     */     private URL csu;
/*     */ 
/*     */     
/*     */     JarLoader(URL param1URL) throws IOException {
/* 474 */       super(new URL("jar", "", -1, param1URL + "!/"));
/* 475 */       this.jar = getJarFile(param1URL);
/* 476 */       this.csu = param1URL;
/*     */     }
/*     */ 
/*     */     
/*     */     private JarFile getJarFile(URL param1URL) throws IOException {
/* 481 */       if ("file".equals(param1URL.getProtocol())) {
/* 482 */         String str = URLUtil.getPathFromURL(param1URL);
/* 483 */         File file = new File(str);
/* 484 */         if (!file.exists()) {
/* 485 */           throw new FileNotFoundException(str);
/*     */         }
/* 487 */         return new JarFile(str);
/*     */       } 
/* 489 */       throw new IOException("Must be file URL");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Resource getResource(String param1String, boolean param1Boolean) {
/* 496 */       JarEntry jarEntry = this.jar.getJarEntry(param1String);
/*     */ 
/*     */       
/* 499 */       if (jarEntry != null) {
/*     */         URL uRL;
/*     */         try {
/* 502 */           uRL = new URL(getBaseURL(), param1String);
/* 503 */           if (param1Boolean) {
/* 504 */             JNLPClassPath.check(uRL);
/*     */           }
/* 506 */         } catch (MalformedURLException malformedURLException) {
/* 507 */           Trace.ignoredException(malformedURLException);
/* 508 */           return null;
/* 509 */         } catch (IOException iOException) {
/* 510 */           Trace.ignoredException(iOException);
/* 511 */           return null;
/* 512 */         } catch (AccessControlException accessControlException) {
/* 513 */           Trace.ignoredException(accessControlException);
/* 514 */           return null;
/*     */         } 
/*     */         
/* 517 */         return new Resource(this, param1String, uRL, jarEntry) { private final String val$name; private final URL val$url; private final JarEntry val$entry; private final JNLPClassPath.JarLoader this$0;
/* 518 */             public String getName() { return this.val$name; }
/* 519 */             public URL getURL() { return this.val$url; } public URL getCodeSourceURL() {
/* 520 */               return this.this$0.csu;
/*     */             } public InputStream getInputStream() throws IOException {
/* 522 */               return this.this$0.jar.getInputStream(this.val$entry);
/*     */             } public int getContentLength() {
/* 524 */               return (int)this.val$entry.getSize();
/*     */             } public Manifest getManifest() throws IOException {
/* 526 */               return this.this$0.jar.getManifest();
/*     */             } public Certificate[] getCertificates() {
/* 528 */               return this.val$entry.getCertificates();
/*     */             } public CodeSigner[] getCodeSigners() {
/* 530 */               if (Globals.isJavaVersionAtLeast15()) {
/* 531 */                 return this.val$entry.getCodeSigners();
/*     */               }
/*     */               
/* 534 */               return null;
/*     */             } }
/*     */           ;
/*     */       } 
/*     */ 
/*     */       
/* 540 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class FileDirectoryLoader
/*     */     extends Loader
/*     */   {
/*     */     private File dir;
/*     */ 
/*     */     
/*     */     FileDirectoryLoader(URL param1URL) {
/* 552 */       super(param1URL);
/* 553 */       if (!"file".equals(param1URL.getProtocol())) {
/* 554 */         throw new IllegalArgumentException("must be FILE URL");
/*     */       }
/* 556 */       this.dir = new File(URLUtil.getPathFromURL(param1URL));
/*     */     }
/*     */ 
/*     */     
/*     */     Resource getResource(String param1String, boolean param1Boolean) {
/*     */       try {
/* 562 */         URL uRL = new URL(getBaseURL(), param1String);
/* 563 */         if (!uRL.getFile().startsWith(getBaseURL().getFile()))
/*     */         {
/* 565 */           return null;
/*     */         }
/*     */         
/* 568 */         if (param1Boolean)
/* 569 */           JNLPClassPath.check(uRL); 
/* 570 */         File file = new File(this.dir, param1String.replace('/', File.separatorChar));
/* 571 */         if (file.exists())
/* 572 */           return new Resource(this, param1String, uRL, file) { private final String val$name; private final URL val$url; private final File val$file; private final JNLPClassPath.FileDirectoryLoader this$0;
/* 573 */               public String getName() { return this.val$name; }
/* 574 */               public URL getURL() { return this.val$url; } public URL getCodeSourceURL() {
/* 575 */                 return this.this$0.getBaseURL();
/*     */               } public InputStream getInputStream() throws IOException {
/* 577 */                 return new FileInputStream(this.val$file);
/*     */               } public int getContentLength() throws IOException {
/* 579 */                 return (int)this.val$file.length();
/*     */               } }
/*     */             ; 
/* 582 */       } catch (Exception exception) {
/* 583 */         return null;
/*     */       } 
/* 585 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\security\JNLPClassPath.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */