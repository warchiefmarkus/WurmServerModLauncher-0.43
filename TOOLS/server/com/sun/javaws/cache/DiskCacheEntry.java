/*     */ package com.sun.javaws.cache;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.net.URL;
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
/*     */ public class DiskCacheEntry
/*     */ {
/*     */   private char _type;
/*     */   private URL _location;
/*     */   private String _versionId;
/*     */   private long _timestamp;
/*     */   private File _file;
/*     */   private File _directory;
/*     */   private File _mappedBitmap;
/*     */   private File _muffinTag;
/*     */   
/*     */   public DiskCacheEntry() {
/*  35 */     this(false, null, null, null, 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public DiskCacheEntry(char paramChar, URL paramURL, String paramString, File paramFile, long paramLong) {
/*  40 */     this(paramChar, paramURL, paramString, paramFile, paramLong, null, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public DiskCacheEntry(char paramChar, URL paramURL, String paramString, File paramFile1, long paramLong, File paramFile2, File paramFile3) {
/*  45 */     this(paramChar, paramURL, paramString, paramFile1, paramLong, paramFile2, paramFile3, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DiskCacheEntry(char paramChar, URL paramURL, String paramString, File paramFile1, long paramLong, File paramFile2, File paramFile3, File paramFile4) {
/*  52 */     this._type = paramChar;
/*  53 */     this._location = paramURL;
/*  54 */     this._versionId = paramString;
/*  55 */     this._timestamp = paramLong;
/*  56 */     this._file = paramFile1;
/*  57 */     this._directory = paramFile2;
/*  58 */     this._mappedBitmap = paramFile3;
/*  59 */     this._muffinTag = paramFile4;
/*     */   }
/*     */   
/*     */   public char getType() {
/*  63 */     return this._type; } public void setType(char paramChar) {
/*  64 */     this._type = paramChar;
/*     */   }
/*  66 */   public URL getLocation() { return this._location; } public void setLocataion(URL paramURL) {
/*  67 */     this._location = paramURL;
/*     */   }
/*  69 */   public long getTimeStamp() { return this._timestamp; } public void setTimeStamp(long paramLong) {
/*  70 */     this._timestamp = paramLong;
/*     */   }
/*     */   
/*  73 */   public File getMuffinTagFile() { return this._muffinTag; } public void setMuffinTagFile(File paramFile) {
/*  74 */     this._muffinTag = paramFile;
/*     */   }
/*  76 */   public String getVersionId() { return this._versionId; } public void setVersionId(String paramString) {
/*  77 */     this._versionId = paramString;
/*     */   }
/*  79 */   public File getFile() { return this._file; } public void setFile(File paramFile) {
/*  80 */     this._file = paramFile;
/*     */   }
/*  82 */   public File getDirectory() { return this._directory; } public void setDirectory(File paramFile) {
/*  83 */     this._directory = paramFile;
/*     */   }
/*  85 */   public File getMappedBitmap() { return this._mappedBitmap; } public void setMappedBitmap(File paramFile) {
/*  86 */     this._mappedBitmap = paramFile;
/*     */   }
/*     */   public long getLastAccess() {
/*  89 */     return (this._file == null) ? 0L : this._file.lastModified();
/*     */   }
/*     */   
/*     */   public void setLastAccess(long paramLong) {
/*  93 */     if (this._file != null) {
/*  94 */       this._file.setLastModified(paramLong);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  99 */     return (this._location == null);
/*     */   }
/*     */   
/*     */   public long getSize() {
/* 103 */     if (this._directory != null && this._directory.isDirectory()) {
/*     */       
/* 105 */       long l = 0L;
/* 106 */       File[] arrayOfFile = this._directory.listFiles();
/* 107 */       for (byte b = 0; b < arrayOfFile.length; b++) {
/* 108 */         l += arrayOfFile[b].length();
/*     */       }
/* 110 */       return l;
/*     */     } 
/* 112 */     return this._file.length();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean newerThan(DiskCacheEntry paramDiskCacheEntry) {
/* 117 */     if (paramDiskCacheEntry == null || getVersionId() != null) {
/* 118 */       return true;
/*     */     }
/* 120 */     return (getTimeStamp() > paramDiskCacheEntry.getTimeStamp());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 125 */     if (isEmpty()) {
/* 126 */       return "DisckCacheEntry[<empty>]";
/*     */     }
/* 128 */     return "DisckCacheEntry[" + this._type + ";" + this._location + ";" + this._versionId + ";" + this._timestamp + ";" + this._file + ";" + this._directory + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\cache\DiskCacheEntry.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */