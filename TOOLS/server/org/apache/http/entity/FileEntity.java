/*     */ package org.apache.http.entity;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.http.annotation.NotThreadSafe;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class FileEntity
/*     */   extends AbstractHttpEntity
/*     */   implements Cloneable
/*     */ {
/*     */   protected final File file;
/*     */   
/*     */   @Deprecated
/*     */   public FileEntity(File file, String contentType) {
/*  54 */     if (file == null) {
/*  55 */       throw new IllegalArgumentException("File may not be null");
/*     */     }
/*  57 */     this.file = file;
/*  58 */     setContentType(contentType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileEntity(File file, ContentType contentType) {
/*  66 */     if (file == null) {
/*  67 */       throw new IllegalArgumentException("File may not be null");
/*     */     }
/*  69 */     this.file = file;
/*  70 */     if (contentType != null) {
/*  71 */       setContentType(contentType.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileEntity(File file) {
/*  80 */     if (file == null) {
/*  81 */       throw new IllegalArgumentException("File may not be null");
/*     */     }
/*  83 */     this.file = file;
/*     */   }
/*     */   
/*     */   public boolean isRepeatable() {
/*  87 */     return true;
/*     */   }
/*     */   
/*     */   public long getContentLength() {
/*  91 */     return this.file.length();
/*     */   }
/*     */   
/*     */   public InputStream getContent() throws IOException {
/*  95 */     return new FileInputStream(this.file);
/*     */   }
/*     */   
/*     */   public void writeTo(OutputStream outstream) throws IOException {
/*  99 */     if (outstream == null) {
/* 100 */       throw new IllegalArgumentException("Output stream may not be null");
/*     */     }
/* 102 */     InputStream instream = new FileInputStream(this.file);
/*     */     try {
/* 104 */       byte[] tmp = new byte[4096];
/*     */       int l;
/* 106 */       while ((l = instream.read(tmp)) != -1) {
/* 107 */         outstream.write(tmp, 0, l);
/*     */       }
/* 109 */       outstream.flush();
/*     */     } finally {
/* 111 */       instream.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStreaming() {
/* 121 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 128 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\entity\FileEntity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */