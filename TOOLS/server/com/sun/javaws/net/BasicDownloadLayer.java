/*     */ package com.sun.javaws.net;
/*     */ 
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.deploy.util.TraceLevel;
/*     */ import com.sun.javaws.Globals;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.jar.JarOutputStream;
/*     */ import java.util.jar.Pack200;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicDownloadLayer
/*     */   implements HttpDownload
/*     */ {
/*     */   private static final int BUF_SIZE = 32768;
/*     */   private HttpRequest _httpRequest;
/*     */   
/*     */   public BasicDownloadLayer(HttpRequest paramHttpRequest) {
/*  30 */     this._httpRequest = paramHttpRequest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void download(HttpResponse paramHttpResponse, File paramFile, HttpDownloadListener paramHttpDownloadListener) throws CanceledDownloadException, IOException {
/*  37 */     int i = paramHttpResponse.getContentLength();
/*  38 */     if (paramHttpDownloadListener != null) paramHttpDownloadListener.downloadProgress(0, i);
/*     */     
/*  40 */     Trace.println("Doing download", TraceLevel.NETWORK);
/*     */ 
/*     */     
/*  43 */     BufferedInputStream bufferedInputStream = paramHttpResponse.getInputStream();
/*  44 */     BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(paramFile));
/*  45 */     String str = paramHttpResponse.getContentEncoding();
/*     */     try {
/*  47 */       if (str != null && str.compareTo("pack200-gzip") == 0 && Globals.havePack200()) {
/*  48 */         Trace.println("download:encoding Pack200: = " + str, TraceLevel.NETWORK);
/*  49 */         Pack200.Unpacker unpacker = Pack200.newUnpacker();
/*     */         
/*  51 */         unpacker.addPropertyChangeListener(new PropertyChangeListener(this, paramHttpDownloadListener, i) { private final HttpDownloadListener val$dl;
/*     */               public void propertyChange(PropertyChangeEvent param1PropertyChangeEvent) {
/*  53 */                 if (this.val$dl != null && param1PropertyChangeEvent.getPropertyName().compareTo("unpack.progress") == 0) {
/*  54 */                   String str = (String)param1PropertyChangeEvent.getNewValue();
/*  55 */                   byte b = (str != null) ? Integer.parseInt(str) : 0;
/*  56 */                   this.val$dl.downloadProgress(b * this.val$length / 100, this.val$length);
/*     */                 } 
/*     */               } private final int val$length; private final BasicDownloadLayer this$0; }
/*     */           );
/*  60 */         JarOutputStream jarOutputStream = new JarOutputStream(bufferedOutputStream);
/*  61 */         unpacker.unpack(bufferedInputStream, jarOutputStream);
/*  62 */         jarOutputStream.close();
/*     */       } else {
/*  64 */         Trace.println("download:encoding GZIP/Plain = " + str, TraceLevel.NETWORK);
/*  65 */         int j = 0;
/*  66 */         int k = 0;
/*  67 */         byte[] arrayOfByte = new byte[32768];
/*  68 */         while ((j = bufferedInputStream.read(arrayOfByte)) != -1) {
/*  69 */           bufferedOutputStream.write(arrayOfByte, 0, j);
/*     */           
/*  71 */           k += j;
/*  72 */           if (k > i && i != 0) k = i; 
/*  73 */           if (paramHttpDownloadListener != null) paramHttpDownloadListener.downloadProgress(k, i); 
/*     */         } 
/*     */       } 
/*  76 */       Trace.println("Wrote URL " + paramHttpResponse.getRequest() + " to file " + paramFile, TraceLevel.NETWORK);
/*     */       
/*  78 */       bufferedInputStream.close(); bufferedInputStream = null;
/*  79 */       bufferedOutputStream.close(); bufferedOutputStream = null;
/*  80 */     } catch (IOException iOException) {
/*     */       
/*  82 */       Trace.println("Got exception while downloading resource: " + iOException, TraceLevel.NETWORK);
/*     */ 
/*     */       
/*  85 */       if (bufferedInputStream != null) { bufferedInputStream.close(); bufferedInputStream = null; }
/*  86 */        if (bufferedOutputStream != null) { bufferedOutputStream.close(); bufferedOutputStream = null; }
/*  87 */        if (paramFile != null) paramFile.delete();
/*     */       
/*  89 */       throw iOException;
/*     */     } 
/*     */ 
/*     */     
/*  93 */     if (paramHttpDownloadListener != null) paramHttpDownloadListener.downloadProgress(i, i);
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void download(URL paramURL, File paramFile, HttpDownloadListener paramHttpDownloadListener) throws CanceledDownloadException, IOException {
/*  99 */     HttpResponse httpResponse = this._httpRequest.doGetRequest(paramURL);
/* 100 */     download(httpResponse, paramFile, paramHttpDownloadListener);
/* 101 */     httpResponse.disconnect();
/*     */   }
/*     */   class PropertyChangeListenerTask implements PropertyChangeListener { HttpDownloadListener _dl; private final BasicDownloadLayer this$0;
/*     */     
/*     */     PropertyChangeListenerTask(BasicDownloadLayer this$0, HttpDownloadListener param1HttpDownloadListener) {
/* 106 */       this.this$0 = this$0; this._dl = null;
/* 107 */       this._dl = param1HttpDownloadListener;
/*     */     }
/*     */ 
/*     */     
/*     */     public void propertyChange(PropertyChangeEvent param1PropertyChangeEvent) {
/* 112 */       if (param1PropertyChangeEvent.getPropertyName().compareTo("unpack.progress") == 0) {
/* 113 */         String str = (String)param1PropertyChangeEvent.getNewValue();
/* 114 */         if (this._dl != null && str != null)
/* 115 */           this._dl.downloadProgress(Integer.parseInt(str), 100); 
/*     */       } 
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\net\BasicDownloadLayer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */