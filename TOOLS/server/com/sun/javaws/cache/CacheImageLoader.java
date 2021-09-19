/*     */ package com.sun.javaws.cache;
/*     */ 
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.javaws.exceptions.JNLPException;
/*     */ import com.sun.javaws.jnl.IconDesc;
/*     */ import java.awt.Image;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CacheImageLoader
/*     */   implements Runnable
/*     */ {
/*  42 */   private static CacheImageLoader _instance = null;
/*     */ 
/*     */   
/*  45 */   private final Object _imageLoadingLock = new Object();
/*     */ 
/*     */   
/*     */   private boolean _running = false;
/*     */ 
/*     */   
/*  51 */   private ArrayList _toLoad = new ArrayList();
/*     */   
/*     */   private class LoadEntry { public IconDesc _id;
/*     */     public URL _url;
/*     */     
/*     */     public LoadEntry(CacheImageLoader this$0, IconDesc param1IconDesc, CacheImageLoaderCallback param1CacheImageLoaderCallback) {
/*  57 */       this.this$0 = this$0;
/*  58 */       this._id = param1IconDesc;
/*  59 */       this._cb = param1CacheImageLoaderCallback;
/*  60 */       this._url = null;
/*     */     } public CacheImageLoaderCallback _cb; private final CacheImageLoader this$0; public LoadEntry(CacheImageLoader this$0, URL param1URL, CacheImageLoaderCallback param1CacheImageLoaderCallback) {
/*  62 */       this.this$0 = this$0;
/*  63 */       this._url = param1URL;
/*  64 */       this._cb = param1CacheImageLoaderCallback;
/*  65 */       this._id = null;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CacheImageLoader getInstance() {
/*  75 */     if (_instance == null) {
/*  76 */       _instance = new CacheImageLoader();
/*     */     }
/*  78 */     return _instance;
/*     */   }
/*     */   
/*     */   public void loadImage(IconDesc paramIconDesc, CacheImageLoaderCallback paramCacheImageLoaderCallback) {
/*  82 */     boolean bool = false;
/*  83 */     synchronized (this._imageLoadingLock) {
/*  84 */       if (!this._running) {
/*  85 */         this._running = true;
/*  86 */         bool = true;
/*     */       } 
/*     */       
/*  89 */       this._toLoad.add(new LoadEntry(this, paramIconDesc, paramCacheImageLoaderCallback));
/*     */     } 
/*  91 */     if (bool) {
/*  92 */       (new Thread(this)).start();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadImage(URL paramURL, CacheImageLoaderCallback paramCacheImageLoaderCallback) {
/*  98 */     boolean bool = false;
/*  99 */     synchronized (this._imageLoadingLock) {
/* 100 */       if (!this._running) {
/* 101 */         this._running = true;
/* 102 */         bool = true;
/*     */       } 
/*     */       
/* 105 */       this._toLoad.add(new LoadEntry(this, paramURL, paramCacheImageLoaderCallback));
/*     */     } 
/* 107 */     if (bool) {
/* 108 */       (new Thread(this)).start();
/*     */     }
/*     */   }
/*     */   
/*     */   public void run() {
/* 113 */     boolean bool = false;
/* 114 */     while (!bool) {
/* 115 */       LoadEntry loadEntry = null;
/* 116 */       synchronized (this._imageLoadingLock) {
/* 117 */         if (this._toLoad.size() > 0) {
/* 118 */           loadEntry = this._toLoad.remove(0);
/*     */         } else {
/*     */           
/* 121 */           bool = true;
/* 122 */           this._running = false;
/*     */         } 
/*     */       } 
/* 125 */       if (!bool)
/*     */         try {
/* 127 */           DiskCacheEntry diskCacheEntry = null;
/* 128 */           Image image = null;
/* 129 */           File file = null;
/* 130 */           URL uRL = loadEntry._url;
/* 131 */           if (uRL == null) {
/* 132 */             diskCacheEntry = DownloadProtocol.getCachedVersion(loadEntry._id.getLocation(), loadEntry._id.getVersion(), 2);
/*     */ 
/*     */ 
/*     */             
/* 136 */             if (diskCacheEntry != null) {
/*     */               try {
/* 138 */                 file = diskCacheEntry.getFile();
/* 139 */                 uRL = file.toURL();
/* 140 */               } catch (Exception exception) {}
/*     */             }
/*     */           } 
/*     */           
/* 144 */           if (uRL != null) {
/* 145 */             image = CacheUtilities.getSharedInstance().loadImage(uRL);
/*     */           }
/*     */           
/* 148 */           if (image != null) {
/* 149 */             publish(loadEntry, image, file, false);
/*     */           }
/* 151 */           if (loadEntry._id != null)
/*     */           {
/* 153 */             (new DelayedImageLoader(this, loadEntry, image, diskCacheEntry)).start();
/*     */           }
/* 155 */         } catch (MalformedURLException malformedURLException) {
/* 156 */           Trace.ignoredException(malformedURLException);
/* 157 */         } catch (IOException iOException) {
/* 158 */           Trace.ignoredException(iOException);
/*     */         }  
/*     */     } 
/*     */   }
/*     */   
/*     */   private class DelayedImageLoader
/*     */     extends Thread {
/*     */     private CacheImageLoader.LoadEntry _entry;
/*     */     private Image _image;
/*     */     private DiskCacheEntry _dce;
/*     */     private final CacheImageLoader this$0;
/*     */     
/*     */     public DelayedImageLoader(CacheImageLoader this$0, CacheImageLoader.LoadEntry param1LoadEntry, Image param1Image, DiskCacheEntry param1DiskCacheEntry) {
/* 171 */       this.this$0 = this$0;
/* 172 */       this._entry = param1LoadEntry;
/* 173 */       this._image = param1Image;
/* 174 */       this._dce = param1DiskCacheEntry;
/*     */     }
/*     */     
/*     */     public void run() {
/*     */       
/* 179 */       try { File file = null;
/* 180 */         if (DownloadProtocol.isUpdateAvailable(this._entry._id.getLocation(), this._entry._id.getVersion(), 2)) {
/*     */           
/* 182 */           this._dce = DownloadProtocol.getResource(this._entry._id.getLocation(), this._entry._id.getVersion(), 2, false, null);
/*     */ 
/*     */           
/* 185 */           if (this._dce != null)
/*     */           {
/* 187 */             file = this._dce.getFile();
/*     */           }
/* 189 */           if (file != null) {
/* 190 */             this._image = CacheUtilities.getSharedInstance().loadImage(file.getPath());
/*     */           }
/*     */           
/* 193 */           CacheImageLoader.publish(this._entry, this._image, file, false);
/* 194 */         } else if (this._dce != null) {
/* 195 */           file = this._dce.getFile();
/*     */         } 
/* 197 */         CacheImageLoader.publish(this._entry, this._image, file, true); }
/*     */       catch (MalformedURLException malformedURLException)
/* 199 */       { Trace.ignoredException(malformedURLException); }
/* 200 */       catch (IOException iOException) { Trace.ignoredException(iOException); }
/* 201 */       catch (JNLPException jNLPException) { Trace.ignoredException((Exception)jNLPException); }
/*     */     
/*     */     }
/*     */   }
/*     */   
/*     */   private static void publish(LoadEntry paramLoadEntry, Image paramImage, File paramFile, boolean paramBoolean) {
/* 207 */     SwingUtilities.invokeLater(new Runnable(paramBoolean, paramLoadEntry, paramImage, paramFile) { private final boolean val$isComplete; private final CacheImageLoader.LoadEntry val$entry;
/*     */           public void run() {
/* 209 */             if (this.val$isComplete) {
/* 210 */               this.val$entry._cb.finalImageAvailable(this.val$entry._id, this.val$image, this.val$file);
/*     */             } else {
/* 212 */               this.val$entry._cb.imageAvailable(this.val$entry._id, this.val$image, this.val$file);
/*     */             } 
/*     */           }
/*     */           
/*     */           private final Image val$image;
/*     */           private final File val$file; }
/*     */       );
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\cache\CacheImageLoader.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */