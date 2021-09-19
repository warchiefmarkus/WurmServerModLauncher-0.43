/*     */ package com.sun.jnlp;
/*     */ 
/*     */ import com.sun.deploy.config.Config;
/*     */ import com.sun.deploy.resources.ResourceManager;
/*     */ import com.sun.deploy.util.URLUtil;
/*     */ import com.sun.javaws.cache.Cache;
/*     */ import com.sun.javaws.cache.DiskCacheEntry;
/*     */ import com.sun.javaws.jnl.LaunchDesc;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Vector;
/*     */ import javax.jnlp.FileContents;
/*     */ import javax.jnlp.PersistenceService;
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
/*     */ public final class PersistenceServiceImpl
/*     */   implements PersistenceService
/*     */ {
/*  35 */   private long _globalLimit = -1L;
/*  36 */   private long _appLimit = -1L;
/*  37 */   private long _size = -1L;
/*  38 */   private static PersistenceServiceImpl _sharedInstance = null;
/*  39 */   private final SmartSecurityDialog _securityDialog = new SmartSecurityDialog();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized PersistenceServiceImpl getInstance() {
/*  45 */     initialize();
/*  46 */     return _sharedInstance;
/*     */   }
/*     */   
/*     */   public static synchronized void initialize() {
/*  50 */     if (_sharedInstance == null) {
/*  51 */       _sharedInstance = new PersistenceServiceImpl();
/*     */     }
/*  53 */     if (_sharedInstance != null)
/*     */     {
/*     */ 
/*     */       
/*  57 */       _sharedInstance._appLimit = Config.getIntProperty("deployment.javaws.muffin.max") * 1024L;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   long getLength(URL paramURL) throws MalformedURLException, IOException {
/*  63 */     checkAccess(paramURL);
/*  64 */     return Cache.getMuffinSize(paramURL);
/*     */   }
/*     */   
/*     */   long getMaxLength(URL paramURL) throws MalformedURLException, IOException {
/*  68 */     checkAccess(paramURL);
/*     */     
/*  70 */     Long long_ = null;
/*     */     try {
/*  72 */       long_ = AccessController.<Long>doPrivileged(new PrivilegedExceptionAction(this, paramURL) { private final URL val$url; private final PersistenceServiceImpl this$0;
/*     */             
/*     */             public Object run() throws IOException {
/*  75 */               long[] arrayOfLong = Cache.getMuffinAttributes(this.val$url);
/*  76 */               if (arrayOfLong == null) return new Long(-1L);
/*     */ 
/*     */               
/*  79 */               return new Long(arrayOfLong[1]);
/*     */             } }
/*     */         );
/*  82 */     } catch (PrivilegedActionException privilegedActionException) {
/*  83 */       throw (IOException)privilegedActionException.getException();
/*     */     } 
/*  85 */     return long_.longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   long setMaxLength(URL paramURL, long paramLong) throws MalformedURLException, IOException {
/*  91 */     long l1 = 0L;
/*  92 */     checkAccess(paramURL);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  97 */     if ((l1 = checkSetMaxSize(paramURL, paramLong)) < 0L) return -1L;
/*     */     
/*  99 */     long l2 = l1;
/*     */     try {
/* 101 */       AccessController.doPrivileged(new PrivilegedExceptionAction(this, paramURL, l2) { private final URL val$url;
/*     */             public Object run() throws MalformedURLException, IOException {
/* 103 */               Cache.putMuffinAttributes(this.val$url, this.this$0.getTag(this.val$url), this.val$f_newmaxsize);
/*     */               
/* 105 */               return null;
/*     */             } private final long val$f_newmaxsize; private final PersistenceServiceImpl this$0; }
/*     */         );
/* 108 */     } catch (PrivilegedActionException privilegedActionException) {
/* 109 */       Exception exception = privilegedActionException.getException();
/* 110 */       if (exception instanceof IOException)
/* 111 */         throw (IOException)exception; 
/* 112 */       if (exception instanceof MalformedURLException) {
/* 113 */         throw (MalformedURLException)exception;
/*     */       }
/*     */     } 
/*     */     
/* 117 */     return l1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long checkSetMaxSize(URL paramURL, long paramLong) throws IOException {
/* 128 */     URL[] arrayOfURL = null;
/*     */     try {
/* 130 */       arrayOfURL = AccessController.<URL[]>doPrivileged(new PrivilegedExceptionAction(this, paramURL) { private final URL val$url;
/*     */             private final PersistenceServiceImpl this$0;
/*     */             
/*     */             public Object run() throws IOException {
/* 134 */               return Cache.getAccessibleMuffins(this.val$url);
/*     */             } }
/*     */         );
/* 137 */     } catch (PrivilegedActionException privilegedActionException) {
/* 138 */       throw (IOException)privilegedActionException.getException();
/*     */     } 
/*     */     
/* 141 */     long l1 = 0L;
/* 142 */     if (arrayOfURL != null) {
/* 143 */       for (byte b = 0; b < arrayOfURL.length; b++) {
/* 144 */         if (arrayOfURL[b] != null) {
/* 145 */           URL uRL = arrayOfURL[b];
/* 146 */           Long long_ = null;
/*     */           try {
/* 148 */             long_ = AccessController.<Long>doPrivileged(new PrivilegedExceptionAction(this, uRL) { private final URL val$friendMuffin;
/*     */                   private final PersistenceServiceImpl this$0;
/*     */                   
/*     */                   public Object run() throws IOException {
/* 152 */                     return new Long(Cache.getMuffinSize(this.val$friendMuffin));
/*     */                   } }
/*     */               );
/*     */           }
/* 156 */           catch (PrivilegedActionException privilegedActionException) {
/* 157 */             throw (IOException)privilegedActionException.getException();
/*     */           } 
/* 159 */           l1 += long_.longValue();
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 165 */     long l2 = paramLong + l1;
/*     */     
/* 167 */     if (l2 > this._appLimit) {
/* 168 */       return reconcileMaxSize(paramLong, l1, this._appLimit);
/*     */     }
/*     */     
/* 171 */     return paramLong;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private long reconcileMaxSize(long paramLong1, long paramLong2, long paramLong3) {
/* 177 */     long l = paramLong1 + paramLong2;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 182 */     boolean bool = CheckServicePermission.hasFileAccessPermissions();
/*     */ 
/*     */     
/* 185 */     if (bool || askUser(l, paramLong3)) {
/* 186 */       this._appLimit = l;
/* 187 */       return paramLong1;
/*     */     } 
/* 189 */     return paramLong3 - paramLong2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private URL[] getAccessibleMuffins(URL paramURL) throws IOException {
/* 196 */     return Cache.getAccessibleMuffins(paramURL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long create(URL paramURL, long paramLong) throws MalformedURLException, IOException {
/* 204 */     checkAccess(paramURL);
/* 205 */     Long long_ = null;
/*     */     
/* 207 */     long l1 = -1L;
/* 208 */     if ((l1 = checkSetMaxSize(paramURL, paramLong)) < 0L) return -1L;
/*     */     
/* 210 */     long l2 = l1;
/*     */     
/*     */     try {
/* 213 */       long_ = AccessController.<Long>doPrivileged(new PrivilegedExceptionAction(this, paramURL, l2) { private final URL val$url; private final long val$pass_newmaxsize; private final PersistenceServiceImpl this$0;
/*     */             
/*     */             public Object run() throws MalformedURLException, IOException {
/* 216 */               File file = Cache.getTempCacheFile(this.val$url, null);
/* 217 */               if (file == null) return new Long(-1L); 
/* 218 */               Cache.insertMuffinEntry(this.val$url, file, 0, this.val$pass_newmaxsize);
/*     */               
/* 220 */               return new Long(this.val$pass_newmaxsize);
/*     */             } }
/*     */         );
/* 223 */     } catch (PrivilegedActionException privilegedActionException) {
/* 224 */       Exception exception = privilegedActionException.getException();
/* 225 */       if (exception instanceof IOException)
/* 226 */         throw (IOException)exception; 
/* 227 */       if (exception instanceof MalformedURLException) {
/* 228 */         throw (MalformedURLException)exception;
/*     */       }
/*     */     } 
/*     */     
/* 232 */     return long_.longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FileContents get(URL paramURL) throws MalformedURLException, IOException {
/* 238 */     checkAccess(paramURL);
/* 239 */     File file = Cache.getMuffinFileForURL(paramURL);
/* 240 */     if (file == null) throw new FileNotFoundException(paramURL.toString());
/*     */     
/* 242 */     return new FileContentsImpl(file, this, paramURL, getMaxLength(paramURL));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete(URL paramURL) throws MalformedURLException, IOException {
/* 248 */     checkAccess(paramURL);
/*     */     
/*     */     try {
/* 251 */       AccessController.doPrivileged(new PrivilegedExceptionAction(this, paramURL) { private final URL val$url; private final PersistenceServiceImpl this$0;
/*     */             public Object run() throws MalformedURLException, IOException {
/* 253 */               DiskCacheEntry diskCacheEntry = Cache.getMuffinEntry('P', this.val$url);
/*     */               
/* 255 */               if (diskCacheEntry == null) {
/* 256 */                 throw new FileNotFoundException(this.val$url.toString());
/*     */               }
/* 258 */               Cache.removeMuffinEntry(diskCacheEntry);
/* 259 */               return null;
/*     */             } }
/*     */         );
/* 262 */     } catch (PrivilegedActionException privilegedActionException) {
/* 263 */       Exception exception = privilegedActionException.getException();
/* 264 */       if (exception instanceof IOException)
/* 265 */         throw (IOException)exception; 
/* 266 */       if (exception instanceof MalformedURLException) {
/* 267 */         throw (MalformedURLException)exception;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getNames(URL paramURL) throws MalformedURLException, IOException {
/* 275 */     String[] arrayOfString = null;
/* 276 */     URL uRL = URLUtil.asPathURL(paramURL);
/* 277 */     checkAccess(uRL);
/*     */     try {
/* 279 */       arrayOfString = AccessController.<String[]>doPrivileged(new PrivilegedExceptionAction(this, uRL) { private final URL val$pathUrl; private final PersistenceServiceImpl this$0;
/*     */             
/*     */             public Object run() throws MalformedURLException, IOException {
/* 282 */               File file = Cache.getMuffinFileForURL(this.val$pathUrl);
/* 283 */               if (!file.isDirectory()) file = file.getParentFile(); 
/* 284 */               File[] arrayOfFile = file.listFiles();
/* 285 */               Vector vector = new Vector();
/* 286 */               for (byte b = 0; b < arrayOfFile.length; b++) {
/* 287 */                 if (Cache.isMainMuffinFile(arrayOfFile[b])) {
/* 288 */                   DiskCacheEntry diskCacheEntry = Cache.getMuffinCacheEntryFromFile(arrayOfFile[b]);
/*     */                   
/* 290 */                   URL uRL = diskCacheEntry.getLocation();
/* 291 */                   File file1 = new File(uRL.getFile());
/* 292 */                   vector.addElement(file1.getName());
/*     */                 } 
/* 294 */               }  return vector.<String>toArray(new String[0]);
/*     */             } }
/*     */         );
/* 297 */     } catch (PrivilegedActionException privilegedActionException) {
/* 298 */       Exception exception = privilegedActionException.getException();
/* 299 */       if (exception instanceof IOException)
/* 300 */         throw (IOException)exception; 
/* 301 */       if (exception instanceof MalformedURLException) {
/* 302 */         throw (MalformedURLException)exception;
/*     */       }
/*     */     } 
/*     */     
/* 306 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTag(URL paramURL) throws MalformedURLException, IOException {
/* 312 */     Integer integer = null;
/* 313 */     checkAccess(paramURL);
/*     */     try {
/* 315 */       integer = AccessController.<Integer>doPrivileged(new PrivilegedExceptionAction(this, paramURL) { private final URL val$url; private final PersistenceServiceImpl this$0;
/*     */             
/*     */             public Object run() throws MalformedURLException, IOException {
/* 318 */               long[] arrayOfLong = Cache.getMuffinAttributes(this.val$url);
/* 319 */               if (arrayOfLong == null) throw new MalformedURLException(); 
/* 320 */               return new Integer((int)arrayOfLong[0]);
/*     */             } }
/*     */         );
/* 323 */     } catch (PrivilegedActionException privilegedActionException) {
/* 324 */       Exception exception = privilegedActionException.getException();
/* 325 */       if (exception instanceof IOException)
/* 326 */         throw (IOException)exception; 
/* 327 */       if (exception instanceof MalformedURLException) {
/* 328 */         throw (MalformedURLException)exception;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 333 */     return integer.intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTag(URL paramURL, int paramInt) throws MalformedURLException, IOException {
/* 339 */     checkAccess(paramURL);
/*     */     try {
/* 341 */       AccessController.doPrivileged(new PrivilegedExceptionAction(this, paramURL, paramInt) { private final URL val$url; private final int val$tag; private final PersistenceServiceImpl this$0;
/*     */             public Object run() throws MalformedURLException, IOException {
/* 343 */               Cache.putMuffinAttributes(this.val$url, this.val$tag, this.this$0.getMaxLength(this.val$url));
/* 344 */               return null;
/*     */             } }
/*     */         );
/* 347 */     } catch (PrivilegedActionException privilegedActionException) {
/* 348 */       Exception exception = privilegedActionException.getException();
/* 349 */       if (exception instanceof IOException)
/* 350 */         throw (IOException)exception; 
/* 351 */       if (exception instanceof MalformedURLException) {
/* 352 */         throw (MalformedURLException)exception;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean askUser(long paramLong1, long paramLong2) {
/* 360 */     Boolean bool = AccessController.<Boolean>doPrivileged(new PrivilegedAction(this, paramLong1, paramLong2) { private final long val$requested; private final long val$currentLimit; private final PersistenceServiceImpl this$0;
/*     */           
/*     */           public Object run() {
/* 363 */             String str = ResourceManager.getString("APIImpl.persistence.message", new Long(this.val$requested), new Long(this.val$currentLimit));
/*     */ 
/*     */             
/* 366 */             boolean bool = this.this$0._securityDialog.showDialog(str);
/* 367 */             if (bool) {
/* 368 */               long l = Math.min(2147483647L, (this.val$requested + 1023L) / 1024L);
/*     */               
/* 370 */               Config.setIntProperty("deployment.javaws.muffin.max", (int)l);
/*     */               
/* 372 */               Config.storeIfDirty();
/*     */             } 
/* 374 */             return new Boolean(bool);
/*     */           } }
/*     */       );
/*     */     
/* 378 */     return bool.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkAccess(URL paramURL) throws MalformedURLException {
/* 389 */     LaunchDesc launchDesc = JNLPClassLoader.getInstance().getLaunchDesc();
/* 390 */     if (launchDesc != null) {
/* 391 */       URL uRL = launchDesc.getCodebase();
/* 392 */       if (uRL != null) {
/* 393 */         if (paramURL == null || !uRL.getHost().equals(paramURL.getHost())) {
/* 394 */           throwAccessDenied(paramURL);
/*     */         }
/* 396 */         String str = paramURL.getFile();
/* 397 */         if (str == null) throwAccessDenied(paramURL); 
/* 398 */         int i = str.lastIndexOf('/');
/* 399 */         if (i == -1)
/* 400 */           return;  if (!uRL.getFile().startsWith(str.substring(0, i + 1))) {
/* 401 */           throwAccessDenied(paramURL);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void throwAccessDenied(URL paramURL) throws MalformedURLException {
/* 408 */     throw new MalformedURLException(ResourceManager.getString("APIImpl.persistence.accessdenied", paramURL.toString()));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\jnlp\PersistenceServiceImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */