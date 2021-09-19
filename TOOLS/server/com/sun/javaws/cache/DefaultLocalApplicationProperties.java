/*     */ package com.sun.javaws.cache;
/*     */ 
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.javaws.LocalApplicationProperties;
/*     */ import com.sun.javaws.jnl.AssociationDesc;
/*     */ import com.sun.javaws.jnl.LaunchDesc;
/*     */ import com.sun.javaws.jnl.ShortcutDesc;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
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
/*     */ public class DefaultLocalApplicationProperties
/*     */   implements LocalApplicationProperties
/*     */ {
/*     */   private static final String REBOOT_NEEDED_KEY = "_default.rebootNeeded";
/*     */   private static final String UPDATE_CHECK_KEY = "_default.forcedUpdateCheck";
/*     */   private static final String NATIVELIB_DIR_KEY = "_default.nativeLibDir";
/*     */   private static final String INSTALL_DIR_KEY = "_default.installDir";
/*     */   private static final String LAST_ACCESSED_KEY = "_default.lastAccessed";
/*     */   private static final String LAUNCH_COUNT_KEY = "_default.launchCount";
/*     */   private static final String ASK_INSTALL_KEY = "_default.askedInstall";
/*     */   private static final String SHORTCUT_KEY = "_default.locallyInstalled";
/*     */   private static final String INDIRECT_PATH_KEY = "_default.indirectPath";
/*     */   private static final String ASSOCIATION_MIME_KEY = "_default.mime.types.";
/*     */   private static final String REGISTERED_TITLE_KEY = "_default.title";
/*     */   private static final String ASSOCIATION_EXTENSIONS_KEY = "_default.extensions.";
/*  44 */   private static final DateFormat _df = DateFormat.getDateTimeInstance();
/*     */ 
/*     */ 
/*     */   
/*     */   private LaunchDesc _descriptor;
/*     */ 
/*     */ 
/*     */   
/*     */   private Properties _properties;
/*     */ 
/*     */ 
/*     */   
/*     */   private URL _location;
/*     */ 
/*     */ 
/*     */   
/*     */   private String _versionId;
/*     */ 
/*     */ 
/*     */   
/*     */   private long _lastAccessed;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean _isApplicationDescriptor;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean _dirty;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean _isLocallyInstalledSystem;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultLocalApplicationProperties(URL paramURL, String paramString, LaunchDesc paramLaunchDesc, boolean paramBoolean) {
/*  82 */     this._descriptor = paramLaunchDesc;
/*  83 */     this._location = paramURL;
/*  84 */     this._versionId = paramString;
/*  85 */     this._isApplicationDescriptor = paramBoolean;
/*  86 */     this._properties = getLocalApplicationPropertiesStorage(this);
/*  87 */     this._isLocallyInstalledSystem = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getLocation() {
/*  95 */     return this._location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getVersionId() {
/* 102 */     return this._versionId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LaunchDesc getLaunchDescriptor() {
/* 109 */     return this._descriptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastAccessed(Date paramDate) {
/* 116 */     put("_default.lastAccessed", _df.format(paramDate));
/*     */   }
/*     */   
/*     */   public Date getLastAccessed() {
/* 120 */     return getDate("_default.lastAccessed");
/*     */   }
/*     */   
/*     */   public void incrementLaunchCount() {
/* 124 */     int i = getLaunchCount();
/*     */     
/* 126 */     put("_default.launchCount", Integer.toString(++i));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLaunchCount() {
/* 134 */     return getInteger("_default.launchCount");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAskedForInstall(boolean paramBoolean) {
/* 142 */     put("_default.askedInstall", (new Boolean(paramBoolean)).toString());
/*     */   }
/*     */   
/*     */   public boolean getAskedForInstall() {
/* 146 */     return getBoolean("_default.askedInstall");
/*     */   }
/*     */   
/*     */   public void setRebootNeeded(boolean paramBoolean) {
/* 150 */     put("_default.rebootNeeded", (new Boolean(paramBoolean)).toString());
/*     */   }
/*     */   
/*     */   public boolean isRebootNeeded() {
/* 154 */     return getBoolean("_default.rebootNeeded");
/*     */   }
/*     */   
/*     */   public void setLocallyInstalled(boolean paramBoolean) {
/* 158 */     put("_default.locallyInstalled", (new Boolean(paramBoolean)).toString());
/*     */   }
/*     */   
/*     */   public boolean isLocallyInstalled() {
/* 162 */     return getBoolean("_default.locallyInstalled");
/*     */   }
/*     */   
/*     */   public boolean isLocallyInstalledSystem() {
/* 166 */     return this._isLocallyInstalledSystem;
/*     */   }
/*     */   
/*     */   public boolean forceUpdateCheck() {
/* 170 */     return getBoolean("_default.forcedUpdateCheck");
/*     */   }
/*     */   
/*     */   public void setForceUpdateCheck(boolean paramBoolean) {
/* 174 */     put("_default.forcedUpdateCheck", (new Boolean(paramBoolean)).toString());
/*     */   }
/*     */   
/*     */   public boolean isApplicationDescriptor() {
/* 178 */     return this._isApplicationDescriptor;
/*     */   }
/*     */   
/*     */   public boolean isExtensionDescriptor() {
/* 182 */     return !this._isApplicationDescriptor;
/*     */   }
/*     */   
/*     */   public String getInstallDirectory() {
/* 186 */     return get("_default.installDir");
/*     */   }
/*     */   
/*     */   public void setInstallDirectory(String paramString) {
/* 190 */     put("_default.installDir", paramString);
/*     */   }
/*     */   
/*     */   public String getNativeLibDirectory() {
/* 194 */     return get("_default.nativeLibDir");
/*     */   }
/*     */   
/*     */   public String getRegisteredTitle() {
/* 198 */     return get("_default.title");
/*     */   }
/*     */   
/*     */   public void setRegisteredTitle(String paramString) {
/* 202 */     put("_default.title", paramString);
/*     */   }
/*     */   
/*     */   public void setNativeLibDirectory(String paramString) {
/* 206 */     put("_default.nativeLibDir", paramString);
/*     */   }
/*     */   
/*     */   public void setAssociations(AssociationDesc[] paramArrayOfAssociationDesc) {
/* 210 */     byte b = 0;
/* 211 */     if (paramArrayOfAssociationDesc == null) {
/* 212 */       AssociationDesc[] arrayOfAssociationDesc = getAssociations();
/* 213 */       if (arrayOfAssociationDesc != null) {
/* 214 */         put("_default.mime.types." + b, null);
/* 215 */         put("_default.extensions." + b, null);
/*     */       } 
/*     */     } else {
/* 218 */       for (b = 0; b < paramArrayOfAssociationDesc.length; b++) {
/* 219 */         put("_default.mime.types." + b, paramArrayOfAssociationDesc[b].getMimeType());
/*     */         
/* 221 */         put("_default.extensions." + b, paramArrayOfAssociationDesc[b].getExtensions());
/*     */       } 
/*     */       
/* 224 */       put("_default.mime.types." + b, null);
/* 225 */       put("_default.extensions." + b, null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAssociation(AssociationDesc paramAssociationDesc) {
/* 231 */     AssociationDesc[] arrayOfAssociationDesc1, arrayOfAssociationDesc2 = getAssociations();
/* 232 */     byte b = 0;
/* 233 */     if (arrayOfAssociationDesc2 == null) {
/* 234 */       arrayOfAssociationDesc1 = new AssociationDesc[1];
/*     */     } else {
/* 236 */       arrayOfAssociationDesc1 = new AssociationDesc[arrayOfAssociationDesc2.length + 1];
/* 237 */       while (b < arrayOfAssociationDesc2.length) {
/* 238 */         arrayOfAssociationDesc1[b] = arrayOfAssociationDesc2[b];
/* 239 */         b++;
/*     */       } 
/*     */     } 
/* 242 */     arrayOfAssociationDesc1[b] = paramAssociationDesc;
/* 243 */     setAssociations(arrayOfAssociationDesc1);
/*     */   }
/*     */   
/*     */   public AssociationDesc[] getAssociations() {
/* 247 */     ArrayList arrayList = new ArrayList();
/* 248 */     byte b = 0; while (true) {
/* 249 */       String str1 = get("_default.mime.types." + b);
/* 250 */       String str2 = get("_default.extensions." + b);
/* 251 */       if (str1 != null || str2 != null) {
/* 252 */         arrayList.add(new AssociationDesc(str2, str1));
/*     */         b++;
/*     */       } 
/*     */       break;
/*     */     } 
/* 257 */     return arrayList.<AssociationDesc>toArray(new AssociationDesc[0]);
/*     */   }
/*     */   
/*     */   public void put(String paramString1, String paramString2) {
/* 261 */     synchronized (this) {
/* 262 */       if (paramString2 == null) {
/* 263 */         this._properties.remove(paramString1);
/*     */       } else {
/*     */         
/* 266 */         this._properties.put(paramString1, paramString2);
/*     */       } 
/* 268 */       this._dirty = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String get(String paramString) {
/* 273 */     synchronized (this) {
/* 274 */       return (String)this._properties.get(paramString);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getInteger(String paramString) {
/* 283 */     String str = get(paramString);
/*     */     
/* 285 */     if (str == null) {
/* 286 */       return 0;
/*     */     }
/* 288 */     int i = 0;
/*     */     try {
/* 290 */       i = Integer.parseInt(str);
/* 291 */     } catch (NumberFormatException numberFormatException) {
/* 292 */       i = 0;
/*     */     } 
/* 294 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getBoolean(String paramString) {
/* 302 */     String str = get(paramString);
/*     */     
/* 304 */     if (str == null) {
/* 305 */       return false;
/*     */     }
/* 307 */     return Boolean.valueOf(str).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getDate(String paramString) {
/* 316 */     String str = get(paramString);
/*     */     
/* 318 */     if (str == null) {
/* 319 */       return null;
/*     */     }
/*     */     try {
/* 322 */       return _df.parse(str);
/* 323 */     } catch (ParseException parseException) {
/* 324 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean doesNewVersionExist() {
/* 329 */     synchronized (this) {
/* 330 */       long l = Cache.getLastAccessed();
/*     */       
/* 332 */       if (l == 0L) {
/* 333 */         return false;
/*     */       }
/* 335 */       if (l > this._lastAccessed) {
/* 336 */         return true;
/*     */       }
/*     */     } 
/* 339 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void store() throws IOException {
/* 346 */     putLocalApplicationPropertiesStorage(this, this._properties);
/* 347 */     this._dirty = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void refreshIfNecessary() {
/* 354 */     synchronized (this) {
/* 355 */       if (!this._dirty && doesNewVersionExist()) {
/* 356 */         refresh();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void refresh() {
/* 365 */     synchronized (this) {
/* 366 */       Properties properties = getLocalApplicationPropertiesStorage(this);
/* 367 */       this._properties = properties;
/* 368 */       this._dirty = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isShortcutSupported() {
/* 374 */     DiskCacheEntry diskCacheEntry = null;
/*     */     try {
/* 376 */       diskCacheEntry = Cache.getCacheEntry('A', this._location, null);
/* 377 */     } catch (IOException iOException) {
/* 378 */       Trace.ignoredException(iOException);
/* 379 */       return false;
/*     */     } 
/* 381 */     if (diskCacheEntry == null || diskCacheEntry.isEmpty()) {
/* 382 */       return false;
/*     */     }
/* 384 */     ShortcutDesc shortcutDesc = this._descriptor.getInformation().getShortcut();
/* 385 */     return (shortcutDesc == null || shortcutDesc.getDesktop() || shortcutDesc.getMenu());
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
/*     */   private Properties getLocalApplicationPropertiesStorage(DefaultLocalApplicationProperties paramDefaultLocalApplicationProperties) {
/* 397 */     Properties properties = new Properties();
/*     */     try {
/* 399 */       URL uRL = paramDefaultLocalApplicationProperties.getLocation();
/* 400 */       String str = paramDefaultLocalApplicationProperties.getVersionId();
/* 401 */       if (uRL != null) {
/* 402 */         byte b = paramDefaultLocalApplicationProperties.isApplicationDescriptor() ? 65 : 69;
/*     */         
/* 404 */         byte[] arrayOfByte = Cache.getLapData(b, uRL, str, true);
/* 405 */         if (arrayOfByte != null) {
/* 406 */           properties.load(new ByteArrayInputStream(arrayOfByte));
/* 407 */           String str1 = (String)properties.get("_default.locallyInstalled");
/* 408 */           if (str1 != null) {
/* 409 */             this._isLocallyInstalledSystem = Boolean.valueOf(str1).booleanValue();
/*     */           }
/*     */         } 
/*     */         
/* 413 */         arrayOfByte = Cache.getLapData(b, uRL, str, false);
/* 414 */         if (arrayOfByte != null) {
/* 415 */           properties.load(new ByteArrayInputStream(arrayOfByte));
/*     */         }
/*     */       } 
/* 418 */     } catch (IOException iOException) {
/* 419 */       Trace.ignoredException(iOException);
/*     */     } 
/* 421 */     return properties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void putLocalApplicationPropertiesStorage(DefaultLocalApplicationProperties paramDefaultLocalApplicationProperties, Properties paramProperties) throws IOException {
/* 430 */     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/*     */     try {
/* 432 */       paramProperties.store(byteArrayOutputStream, "LAP");
/* 433 */     } catch (IOException iOException) {}
/* 434 */     byteArrayOutputStream.close();
/* 435 */     byte b = paramDefaultLocalApplicationProperties.isApplicationDescriptor() ? 65 : 69;
/*     */     
/* 437 */     Cache.putLapData(b, paramDefaultLocalApplicationProperties.getLocation(), paramDefaultLocalApplicationProperties.getVersionId(), byteArrayOutputStream.toByteArray());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\cache\DefaultLocalApplicationProperties.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */