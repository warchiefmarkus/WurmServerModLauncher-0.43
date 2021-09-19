/*     */ package org.apache.http.client.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.entity.HttpEntityWrapper;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class DecompressingEntity
/*     */   extends HttpEntityWrapper
/*     */ {
/*     */   private static final int BUFFER_SIZE = 2048;
/*     */   private InputStream content;
/*     */   
/*     */   public DecompressingEntity(HttpEntity wrapped) {
/*  60 */     super(wrapped);
/*     */   }
/*     */   
/*     */   abstract InputStream decorate(InputStream paramInputStream) throws IOException;
/*     */   
/*     */   private InputStream getDecompressingStream() throws IOException {
/*  66 */     InputStream in = this.wrappedEntity.getContent();
/*     */     try {
/*  68 */       return decorate(in);
/*  69 */     } catch (IOException ex) {
/*  70 */       in.close();
/*  71 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getContent() throws IOException {
/*  80 */     if (this.wrappedEntity.isStreaming()) {
/*  81 */       if (this.content == null) {
/*  82 */         this.content = getDecompressingStream();
/*     */       }
/*  84 */       return this.content;
/*     */     } 
/*  86 */     return getDecompressingStream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream outstream) throws IOException {
/*  95 */     if (outstream == null) {
/*  96 */       throw new IllegalArgumentException("Output stream may not be null");
/*     */     }
/*  98 */     InputStream instream = getContent();
/*     */     try {
/* 100 */       byte[] buffer = new byte[2048];
/*     */       int l;
/* 102 */       while ((l = instream.read(buffer)) != -1) {
/* 103 */         outstream.write(buffer, 0, l);
/*     */       }
/*     */     } finally {
/* 106 */       instream.close();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\client\entity\DecompressingEntity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */