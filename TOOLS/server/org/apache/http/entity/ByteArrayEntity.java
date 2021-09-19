/*     */ package org.apache.http.entity;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
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
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class ByteArrayEntity
/*     */   extends AbstractHttpEntity
/*     */   implements Cloneable
/*     */ {
/*     */   @Deprecated
/*     */   protected final byte[] content;
/*     */   private final byte[] b;
/*     */   private final int off;
/*     */   private final int len;
/*     */   
/*     */   public ByteArrayEntity(byte[] b, ContentType contentType) {
/*  58 */     if (b == null) {
/*  59 */       throw new IllegalArgumentException("Source byte array may not be null");
/*     */     }
/*  61 */     this.content = b;
/*  62 */     this.b = b;
/*  63 */     this.off = 0;
/*  64 */     this.len = this.b.length;
/*  65 */     if (contentType != null) {
/*  66 */       setContentType(contentType.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteArrayEntity(byte[] b, int off, int len, ContentType contentType) {
/*  75 */     if (b == null) {
/*  76 */       throw new IllegalArgumentException("Source byte array may not be null");
/*     */     }
/*  78 */     if (off < 0 || off > b.length || len < 0 || off + len < 0 || off + len > b.length)
/*     */     {
/*  80 */       throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
/*     */     }
/*  82 */     this.content = b;
/*  83 */     this.b = b;
/*  84 */     this.off = off;
/*  85 */     this.len = len;
/*  86 */     if (contentType != null) {
/*  87 */       setContentType(contentType.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   public ByteArrayEntity(byte[] b) {
/*  92 */     this(b, (ContentType)null);
/*     */   }
/*     */   
/*     */   public ByteArrayEntity(byte[] b, int off, int len) {
/*  96 */     this(b, off, len, (ContentType)null);
/*     */   }
/*     */   
/*     */   public boolean isRepeatable() {
/* 100 */     return true;
/*     */   }
/*     */   
/*     */   public long getContentLength() {
/* 104 */     return this.len;
/*     */   }
/*     */   
/*     */   public InputStream getContent() {
/* 108 */     return new ByteArrayInputStream(this.b, this.off, this.len);
/*     */   }
/*     */   
/*     */   public void writeTo(OutputStream outstream) throws IOException {
/* 112 */     if (outstream == null) {
/* 113 */       throw new IllegalArgumentException("Output stream may not be null");
/*     */     }
/* 115 */     outstream.write(this.b, this.off, this.len);
/* 116 */     outstream.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStreaming() {
/* 126 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 131 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\entity\ByteArrayEntity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */