/*     */ package org.apache.http.entity;
/*     */ 
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
/*     */ @NotThreadSafe
/*     */ public class InputStreamEntity
/*     */   extends AbstractHttpEntity
/*     */ {
/*     */   private static final int BUFFER_SIZE = 2048;
/*     */   private final InputStream content;
/*     */   private final long length;
/*     */   
/*     */   public InputStreamEntity(InputStream instream, long length) {
/*  51 */     this(instream, length, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStreamEntity(InputStream instream, long length, ContentType contentType) {
/*  59 */     if (instream == null) {
/*  60 */       throw new IllegalArgumentException("Source input stream may not be null");
/*     */     }
/*  62 */     this.content = instream;
/*  63 */     this.length = length;
/*  64 */     if (contentType != null) {
/*  65 */       setContentType(contentType.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isRepeatable() {
/*  70 */     return false;
/*     */   }
/*     */   
/*     */   public long getContentLength() {
/*  74 */     return this.length;
/*     */   }
/*     */   
/*     */   public InputStream getContent() throws IOException {
/*  78 */     return this.content;
/*     */   }
/*     */   
/*     */   public void writeTo(OutputStream outstream) throws IOException {
/*  82 */     if (outstream == null) {
/*  83 */       throw new IllegalArgumentException("Output stream may not be null");
/*     */     }
/*  85 */     InputStream instream = this.content;
/*     */     try {
/*  87 */       byte[] buffer = new byte[2048];
/*     */       
/*  89 */       if (this.length < 0L) {
/*     */         int l;
/*  91 */         while ((l = instream.read(buffer)) != -1) {
/*  92 */           outstream.write(buffer, 0, l);
/*     */         }
/*     */       } else {
/*     */         
/*  96 */         long remaining = this.length;
/*  97 */         while (remaining > 0L) {
/*  98 */           int l = instream.read(buffer, 0, (int)Math.min(2048L, remaining));
/*  99 */           if (l == -1) {
/*     */             break;
/*     */           }
/* 102 */           outstream.write(buffer, 0, l);
/* 103 */           remaining -= l;
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 107 */       instream.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isStreaming() {
/* 112 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void consumeContent() throws IOException {
/* 124 */     this.content.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\entity\InputStreamEntity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */