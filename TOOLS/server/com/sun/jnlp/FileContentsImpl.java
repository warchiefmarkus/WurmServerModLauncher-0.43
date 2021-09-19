/*     */ package com.sun.jnlp;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import javax.jnlp.FileContents;
/*     */ import javax.jnlp.JNLPRandomAccessFile;
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
/*     */ public final class FileContentsImpl
/*     */   implements FileContents
/*     */ {
/*  40 */   private String _name = null;
/*  41 */   private File _file = null;
/*  42 */   private long _limit = Long.MAX_VALUE;
/*  43 */   private URL _url = null;
/*  44 */   private JNLPRandomAccessFile _raf = null;
/*  45 */   private PersistenceServiceImpl _psCallback = null;
/*     */   
/*     */   FileContentsImpl(File paramFile, long paramLong) throws IOException {
/*  48 */     this._file = paramFile;
/*  49 */     this._limit = paramLong;
/*  50 */     this._name = this._file.getName();
/*     */   }
/*     */   
/*     */   FileContentsImpl(File paramFile, PersistenceServiceImpl paramPersistenceServiceImpl, URL paramURL, long paramLong) {
/*  54 */     this._file = paramFile;
/*  55 */     this._url = paramURL;
/*  56 */     this._psCallback = paramPersistenceServiceImpl;
/*  57 */     this._limit = paramLong;
/*     */     
/*  59 */     int i = paramURL.getFile().lastIndexOf('/');
/*  60 */     this._name = (i != -1) ? paramURL.getFile().substring(i + 1) : paramURL.getFile();
/*     */   }
/*     */   
/*     */   public String getName() {
/*  64 */     return this._name;
/*     */   }
/*     */   
/*     */   public long getLength() {
/*  68 */     Long long_ = AccessController.<Long>doPrivileged(new PrivilegedAction(this) { private final FileContentsImpl this$0;
/*     */           public Object run() {
/*  70 */             return new Long(this.this$0._file.length());
/*     */           } }
/*     */       );
/*  73 */     return long_.longValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/*     */     try {
/*  79 */       return AccessController.<InputStream>doPrivileged(new PrivilegedExceptionAction(this) { private final FileContentsImpl this$0;
/*     */             public Object run() throws IOException {
/*  81 */               return new FileInputStream(this.this$0._file);
/*     */             } }
/*     */         );
/*     */     }
/*  85 */     catch (PrivilegedActionException privilegedActionException) {
/*  86 */       throw rethrowException(privilegedActionException);
/*     */     } 
/*     */   }
/*     */   
/*     */   public OutputStream getOutputStream(boolean paramBoolean) throws IOException {
/*     */     try {
/*  92 */       return AccessController.<OutputStream>doPrivileged(new PrivilegedExceptionAction(this, paramBoolean) { private final boolean val$append;
/*     */             public Object run() throws IOException {
/*  94 */               return new MeteredFileOutputStream(this.this$0._file, !this.val$append, this.this$0);
/*     */             }
/*     */             private final FileContentsImpl this$0; }
/*     */         );
/*  98 */     } catch (PrivilegedActionException privilegedActionException) {
/*  99 */       throw rethrowException(privilegedActionException);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean canRead() throws IOException {
/* 104 */     Boolean bool = AccessController.<Boolean>doPrivileged(new PrivilegedAction(this) { private final FileContentsImpl this$0;
/*     */           public Object run() {
/* 106 */             return new Boolean(this.this$0._file.canRead());
/*     */           } }
/*     */       );
/* 109 */     return bool.booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWrite() throws IOException {
/* 114 */     Boolean bool = AccessController.<Boolean>doPrivileged(new PrivilegedAction(this) { private final FileContentsImpl this$0;
/*     */           public Object run() {
/* 116 */             return new Boolean(this.this$0._file.canWrite());
/*     */           } }
/*     */       );
/* 119 */     return bool.booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public JNLPRandomAccessFile getRandomAccessFile(String paramString) throws IOException {
/*     */     try {
/* 125 */       return AccessController.<JNLPRandomAccessFile>doPrivileged(new PrivilegedExceptionAction(this, paramString) { private final String val$mode; private final FileContentsImpl this$0;
/*     */             public Object run() throws MalformedURLException, IOException {
/* 127 */               return new JNLPRandomAccessFileImpl(this.this$0._file, this.val$mode, this.this$0);
/*     */             } }
/*     */         );
/* 130 */     } catch (PrivilegedActionException privilegedActionException) {
/* 131 */       throw rethrowException(privilegedActionException);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long getMaxLength() throws IOException {
/* 137 */     return this._limit;
/*     */   }
/*     */ 
/*     */   
/*     */   public long setMaxLength(long paramLong) throws IOException {
/* 142 */     if (this._psCallback != null) {
/* 143 */       this._limit = this._psCallback.setMaxLength(this._url, paramLong);
/* 144 */       return this._limit;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 149 */     this._limit = paramLong;
/* 150 */     return this._limit;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private IOException rethrowException(PrivilegedActionException paramPrivilegedActionException) throws IOException {
/* 156 */     Exception exception = paramPrivilegedActionException.getException();
/* 157 */     if (exception instanceof IOException)
/* 158 */       throw (IOException)exception; 
/* 159 */     if (exception instanceof RuntimeException)
/*     */     {
/* 161 */       throw (RuntimeException)exception;
/*     */     }
/* 163 */     throw new IOException(exception.getMessage());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\jnlp\FileContentsImpl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */